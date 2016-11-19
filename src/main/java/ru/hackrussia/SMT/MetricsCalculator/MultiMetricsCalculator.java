package ru.hackrussia.SMT.MetricsCalculator;

import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.BVFParser.NoSuchFrameException;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiMetricsCalculator {
    HashMap<String, MetricsInterface> metricsHashmap;
    private Integer frameStep;
    private Integer resultStep;
    public MultiMetricsCalculator() {
        frameStep = 1;
        resultStep = 1;
        metricsHashmap = new HashMap<String, MetricsInterface>();
    }

    public MultiMetricsCalculator(Integer frameStep, Integer resultStep) {
        this.frameStep = frameStep;
        this.resultStep = resultStep;
        metricsHashmap = new HashMap<String, MetricsInterface>();
    }

    public void put(String name, MetricsInterface metric) {
        metricsHashmap.put(name, metric);
    }

    /** Return HashMap of ArrayLists metrics calculation result */
    public HashMap<String, ArrayList<Double>> calculate(BVFContent bvf) {
        HashMap<String, ArrayList<Double>> result = new HashMap<String, ArrayList<Double>>();
        for (String s : metricsHashmap.keySet()) {
            result.put(s, new ArrayList<Double>());
        }
        int j = 0;
        for (int i = 0; i < bvf.getFramesCount(); i += frameStep) {
            try {
                BVFContent.Skeleton sk = bvf.getRelativeSkeleton(i);
                for (String name : metricsHashmap.keySet()) {
                    MetricsInterface metrics = metricsHashmap.get(name);
                    Double res = metrics.calculate(sk);
                    if (j % resultStep == 0) {
                        result.get(name).add(res);
                    }
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
