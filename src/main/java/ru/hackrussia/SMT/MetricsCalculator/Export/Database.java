package ru.hackrussia.SMT.MetricsCalculator.Export;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ярослав on 19.11.2016.
 */
public class Database {
    InfluxDB influxDB = InfluxDBFactory.connect("http://172.17.0.2:8086", "root", "root");
    String dbName = "smt";

    BatchPoints batchPoints = BatchPoints
            .database(dbName)
            .tag("async", "true")
            .retentionPolicy("autogen")
            .consistency(InfluxDB.ConsistencyLevel.ALL)
            .build();
    Point point1 = Point.measurement("Arm")
            .time(1479556800000L+1, TimeUnit.MILLISECONDS)
            .addField("XD", 90L)
            .addField("YD", 90L)
            .addField("ZD", 90L)
            .addField("XR", 90L)
            .addField("YR", 90L)
            .addField("ZR", 90L)
            .build();
    Point point2 = Point.measurement("Forearm")
            .time(1479556800000L+2, TimeUnit.MILLISECONDS)
            .addField("XD", 90L)
            .addField("YD", 90L)
            .addField("ZD", 90L)
            .addField("XR", 90L)
            .addField("YR", 90L)
            .addField("ZR", 90L)
            .build();
    Point point3 = Point.measurement("Hand")
            .time(1479556800000L+3, TimeUnit.MILLISECONDS)
            .addField("XD", 90L)
            .addField("YD", 90L)
            .addField("ZD", 90L)
            .addField("XR", 90L)
            .addField("YR", 90L)
            .addField("ZR", 90L)
            .build();
    batchPoints.setPoint1(point1);
    batchPoints.setPoint2(point2);
    batchPoints.setPoint3(point3);
    influxDB.write(batchPoints);
    Query query = new Query("SELECT idle FROM cpu", dbName);
    influxDB.query(query);
    influxDB.deleteDatabase(dbName);
}
