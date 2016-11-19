package ru.hackrussia.SMT;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.BVFParser.NoSuchFrameException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
                Point.Builder point = Point.measurement(id);
                for (String name : bvf.getNames()) {
                    point = point.tag("BodyPart", name);
                    for (Integer i = 0; i < bvf.getFramesCount(); i++) {
                        BVFContent.Skeleton sk = bvf.getRelativeSkeleton(i).getByName(name);
                        point.time(time + i * bvf.getFrameTime(), TimeUnit.MILLISECONDS);
                        for (Integer j = 0; j < 3; j++) {
                            point = point.addField("offset " + j.toString(),
                                    sk.getRelativeOffset().get(j));
                        }
                        for (Integer j = 0; j < 3; j++) {
                            point = point.addField("rotate " + j.toString(),
                                    sk.getRelativeOffset().get(j));
                        }
                    }
                    influxDB.write(dbName, "autogen", point.build());
                }
            } catch (Exception e) {
                System.out.println("Something wrong!");
            }
        }
    }
}
