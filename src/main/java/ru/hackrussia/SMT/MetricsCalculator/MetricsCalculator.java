package ru.hackrussia.SMT.MetricsCalculator;

import ru.hackrussia.SMT.BVFParser.BVFContent;

import java.util.ArrayList;

public class MetricsCalculator {
    private MetricsInterface metrics;
    private Integer frameStep;
    private Integer resultStep;

    private MetricsCalculator() {}
    /** Create calculator for metrics */
    public MetricsCalculator(MetricsInterface metrics) {}

    /** Create calculator for metrics, skipping (frameStep - 1) frames in one step and ((frameStep -1) * (resultStep - 1)) results*/
    public MetricsCalculator(MetricsInterface metrics, Integer frameStep, Integer resultStep) {}

    /** Return ArrayList of metrics calculation result */
    public ArrayList<Double> calculate(BVFContent bvf) {return null;}
}
