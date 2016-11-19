package ru.hackrussia.SMT.MetricsSamples;

import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;


public class StepCounterMetric implements MetricsInterface {
    static final Double koef = 0.1;
    private Double leftLegMin;
    private Double rightLegMin;
    private Double leftLegMax;
    private Double rightLegMax;
    private Double stepCount;
    private Double leftZero;
    private Double rightZero;
    private Boolean leftRise;
    private Boolean rightRise;

    public StepCounterMetric() {
        stepCount = 0.0;
        leftLegMin = 20000000000.0;
        rightLegMin = 200000000000.0;
        leftLegMax = 0.0;
        rightLegMax = 0.0;
        leftZero = 10000000000.0;
        rightZero = 10000000000.0;
        leftRise = false;
        rightRise = false;
    }

    public Double calculate(BVFContent.Skeleton skeleton) {
        Double rightLeg = skeleton.getByName("RightUpLeg").getRelativeOffset().get(1); //Vertical Axis
        Double leftLeg = skeleton.getByName("LeftUpLeg").getRelativeOffset().get(1); //Vertical Axis
        if (rightZero > rightLeg) {
            rightZero = rightLeg;
        }
        if (leftZero > leftLeg) {
            leftZero = leftLeg;
        }
        rightLeg -= rightZero;
        leftLeg -= leftZero;
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
