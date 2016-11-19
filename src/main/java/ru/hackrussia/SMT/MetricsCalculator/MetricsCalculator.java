package ru.hackrussia.SMT.MetricsCalculator;

import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.BVFParser.NoSuchFrameException;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MetricsCalculator {
    private MetricsInterface metrics;
    private Integer frameStep;
    private Integer resultStep;

    private MetricsCalculator() {}
    /** Create calculator for metrics */
    public MetricsCalculator(MetricsInterface metrics) {
        this.frameStep = 1;
        this.resultStep = 1;
        this.metrics = metrics;
    }

    /** Create calculator for metrics, skipping (frameStep - 1) frames in one step and ((frameStep -1) * (resultStep - 1)) results*/
    public MetricsCalculator(MetricsInterface metrics, Integer frameStep, Integer resultStep) {
        this.metrics = metrics;
        this.frameStep = frameStep;
        this.resultStep = resultStep;
    }

    /** Return ArrayList of metrics calculation result */
    public ArrayList<Double> calculate(BVFContent bvf) {
        ArrayList<Double> result = new ArrayList<Double>();
        int j = 0;
        for (int i = 0; i < bvf.getFramesCount(); i += frameStep) {
            try {
                Double res = metrics.calculate(bvf.getRelativeSkeleton(i));
                if (j % resultStep == 0) {
                    result.add(res);
                }
            }
            catch (NoSuchFrameException e) {
                //No matter what, it can't be truth
            }
            j++;
        }
        return result;
    }
}
