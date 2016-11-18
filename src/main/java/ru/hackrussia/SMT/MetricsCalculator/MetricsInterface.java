package ru.hackrussia.SMT.MetricsCalculator;

import ru.hackrussia.SMT.BVFParser.BVFContent;

public interface MetricsInterface {

    /** Calculate metrics with frame skeleton. You can be sure, that you obtain skeletons in frame order */
    Double calculate(BVFContent.Skeleton skeleton);
}
