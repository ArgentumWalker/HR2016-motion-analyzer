package ru.hackrussia.SMT.MetricsSamples;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;

import java.util.ArrayList;

/**
 * Created by ArgentumWalker on 19.11.16.
 */
public class StepCounterMetric implements MetricsInterface {
    static final Double koef = 0.1;
    private Double leftLegMin;
    private Double leftLegPreviousMin;
    private Double rightLegMin;
    private Double rightLegPreviousMin;
    private Double leftLegPreviousMax;
    private Double leftLegMax;
    private Double rightLegPreviousMax;
    private Double rightLegMax;
    private Double stepCount;
    private Boolean leftRise;
    private Boolean rightRise;

    public StepCounterMetric() {
        stepCount = 0.0;
        leftLegMin = 10000000000.0;
        leftLegPreviousMin = 10000000000.0;
        rightLegMin = 100000000000.0;
        rightLegPreviousMin = 100000000000.0;
        leftLegMax = -10000000000.0;
        leftLegPreviousMax = -100000000000.0;
        rightLegMax = -100000000000.0;
        rightLegPreviousMax = -100000000000.0;
        leftRise = false;
        rightRise = false;
    }

    public Double calculate(BVFContent.Skeleton skeleton) {
        Double rightLeg = skeleton.getByName("RightUpLeg").getRelativeOffset().get(1); //Vertical Axis
        Double leftLeg = skeleton.getByName("LeftUpLeg").getRelativeOffset().get(1); //Vertical Axis
        Boolean haveStep = false;
        if (leftRise) {
            if (leftLegMax > leftLeg * (1 + koef)) {
                leftLegMin = leftLeg;
                haveStep = true;
                leftRise = false;
            } else {
                if (leftLegMax < leftLeg) {
                    leftLegMax = leftLeg;
                }
            }
        } else {
            if (leftLegMin < leftLeg * (1 - koef)) {
                leftLegMax = leftLeg;
                leftRise = true;
            } else {
                if (leftLegMin > leftLeg) {
                    leftLegMin = leftLeg;
                }
            }
        }
        if (rightRise) {
            if (rightLegMax > rightLeg * (1 + koef)) {
                rightLegMin = rightLeg;
                haveStep = true;
                rightRise = false;
            } else {
                if (rightLegMax < rightLeg) {
                    rightLegMax = rightLeg;
                }
            }
        } else {
            if (rightLegMin < rightLeg * (1 - koef)) {
                rightLegMax = rightLeg;
                rightRise = true;
            } else {
                if (rightLegMin > leftLeg) {
                    rightLegMin = leftLeg;
                }
            }
        }
        if (haveStep) {
            stepCount += 1.0;
        }
        return stepCount;
    }
}
