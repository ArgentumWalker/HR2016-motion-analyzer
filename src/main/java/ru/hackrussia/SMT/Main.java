package ru.hackrussia.SMT;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.BVFParser.BVFParseException;
import ru.hackrussia.SMT.MetricsCalculator.MultiMetricsCalculator;
import ru.hackrussia.SMT.MetricsSamples.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Main {
    static public void main(String[] args) {
        if (args.length != 1 && args.length != 4) {
            System.out.println("Wrong number of arguments");
        } else {
            String login;
            String password;
            String ip;
            if (args.length == 4) {
                login = args[1];
                password = args[2];
                ip = args[3];
            } else {
                login = "smt_writer";
                password = "hrrr2016hrrr";
                ip = "http://159.93.36.125:8086";
            }
            String filename = args[0];
            String id = filename.substring(filename.lastIndexOf("/") + 1);
            try {
                InputStream file = new FileInputStream(filename);
                BVFContent bvf = new BVFContent(file);
                InfluxDB influxDB = InfluxDBFactory.connect(ip, login, password);
                if (influxDB == null) {
                    throw new DBAuthorizationException();
                }
                String dbName = "smt";
                final long time = System.currentTimeMillis();
                //Simple things
                BatchPoints pts = BatchPoints.database(dbName)
                        .retentionPolicy("autogen")
                        .consistency(InfluxDB.ConsistencyLevel.ALL)
                        .build();
                for (Integer i = 0; i < bvf.getFramesCount(); i += 2) {
                    BVFContent.Skeleton skl = bvf.getRelativeSkeleton(i);
                    for (String name : bvf.getNames()) {
                        BVFContent.Skeleton sk = skl.getByName(name);
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
                calculator.put(WalkDistance.class.getName(), new WalkDistance());
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
            }
            catch (IOException e) {
                System.out.println("Input file reading error");
            }
            catch (BVFParseException e) {
                System.out.println("Input file parse error");
            }
            catch (DBAuthorizationException e) {
                System.out.println("Authorization error");
            }
            catch (Exception e) {
                System.out.println("Unknown error");
                e.printStackTrace();
            }
        }
    }
}
