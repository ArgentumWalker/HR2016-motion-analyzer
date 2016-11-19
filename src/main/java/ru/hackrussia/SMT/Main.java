package ru.hackrussia.SMT;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.BVFParser.NoSuchFrameException;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;
import ru.hackrussia.SMT.MetricsCalculator.MultiMetricsCalculator;
import ru.hackrussia.SMT.MetricsSamples.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Main {
    static public void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Все очень плохо :(");
        } else {
            String filename = args[0];
            String id = filename.substring(filename.lastIndexOf("/") + 1);
            try {
                InputStream file = new FileInputStream(filename);
                BVFContent bvf = new BVFContent(file);
                InfluxDB influxDB = InfluxDBFactory.connect("http://159.93.36.125:8086", "smt_writer", "hrrr2016hrrr");
                String dbName = "smt";
                final long time = System.currentTimeMillis();
                //Simple things
                BatchPoints pts = BatchPoints.database(dbName)
                        .retentionPolicy("autogen")
                        .consistency(InfluxDB.ConsistencyLevel.ALL)
                        .build();
               // Point.Builder point = Point.measurement(id);
                for (Integer i = 0; i < bvf.getFramesCount(); i += 2) {
                    BVFContent.Skeleton skl = bvf.getRelativeSkeleton(i);
                //    System.out.println(name);
                //    Point.Builder point2 = point.tag("BodyPart", name);
                    for (String name : bvf.getNames()) {
                        BVFContent.Skeleton sk = skl.getByName(name);
                //        Point.Builder point3 = point2.time(time + i * bvf.getFrameTime(), TimeUnit.MILLISECONDS);
                        pts.point(Point.measurement(id)
                                .tag("BodyPart", name)
                                .time(time + i * bvf.getFrameTime(), TimeUnit.MILLISECONDS)
                                .addField("offsetX", sk.getRelativeOffset().get(0))
                                .addField("offsetY", sk.getRelativeOffset().get(1))
                                .addField("offsetZ", sk.getRelativeOffset().get(2))
                                .addField("rotateX", sk.getRelativeRotation().get(0))
                                .addField("rotateY", sk.getRelativeRotation().get(1))
                                .addField("rotateZ", sk.getRelativeRotation().get(2))
                                .build());
                    }
                }
                //Metrics
                MultiMetricsCalculator calculator = new MultiMetricsCalculator(2, 10);
                calculator.put(FootsDistance.class.getName(), new FootsDistance());
                calculator.put(HandFootSyncronize.class.getName(), new HandFootSyncronize());
                calculator.put(LeftLegVerticalAxis.class.getName(), new LeftLegVerticalAxis());
                calculator.put(RightLegVerticalAxis.class.getName(), new RightLegVerticalAxis());
                calculator.put(StepCounterMetric.class.getName(), new StepCounterMetric());
                HashMap<String, ArrayList<Double>> res = calculator.calculate(bvf);
                for (String s : res.keySet()) {
                    ArrayList<Double> reses = res.get(s);
                    for (Integer i = 0; i < bvf.getFramesCount(); i += 20) {
                        pts.point(Point.measurement(id).time(time + i * bvf.getFrameTime(), TimeUnit.MILLISECONDS)
                                .tag("Metrics", s.substring(s.lastIndexOf(".") + 1))
                                .addField("value", reses.get(i/20)).build());
                    }
                }
                influxDB.write(pts);
            } catch (Exception e) {
                System.out.println("Something wrong!");
            }
        }
    }
}
