package ru.hackrussia.SMT.MetricsSamples;

import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;

public class RightLegVerticalAxis  implements MetricsInterface {

    public Double calculate(BVFContent.Skeleton skeleton) {
        return skeleton.getByName("RightUpLeg").getRelativeOffset().get(1);
    }
}
