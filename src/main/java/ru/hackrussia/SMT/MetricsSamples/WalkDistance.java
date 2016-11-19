package ru.hackrussia.SMT.MetricsSamples;

import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;

public class WalkDistance implements MetricsInterface {
    private Double prevX = null;
    private Double prevZ = null;
    private Double distance = 0.0;

    public Double calculate(BVFContent.Skeleton skeleton) {
        Double x = skeleton.getByName("Hips").getAbsolutePoint().get(0);
        Double z = skeleton.getByName("Hips").getAbsolutePoint().get(2);
        if (prevX == null) {
            prevX = x;
        }
        if (prevZ == null) {
            prevZ = z;
        }
        distance += Math.sqrt((x - prevX) * (x - prevX) + (z - prevZ) * (z - prevZ));
        return distance;
    }
}
