package ru.hackrussia.SMT.MetricsSamples;

import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;

public class FootsDistance implements MetricsInterface {

    public Double calculate(BVFContent.Skeleton skeleton) {
        Double leftX = skeleton.getByName("LeftFoot").getRelativeOffset().get(0);
        Double leftZ = skeleton.getByName("LeftFoot").getRelativeOffset().get(2);
        Double rightX = skeleton.getByName("RightFoot").getRelativeOffset().get(0);
        Double rightZ = skeleton.getByName("RightFoot").getRelativeOffset().get(2);
        return Math.sqrt((leftX - rightX) * (leftX - rightX) + (leftZ - rightZ) * (leftZ - rightZ));
    }
}
