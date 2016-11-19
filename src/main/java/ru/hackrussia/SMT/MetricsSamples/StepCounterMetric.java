package ru.hackrussia.SMT.MetricsSamples;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;


public class StepCounterMetric implements MetricsInterface {
    static final Double angle = 0.16;
    private Double stepCount;
    private Double leftSum;
    private Double rightSum;
    private Double frameCount;
    private Boolean inZone;

    public StepCounterMetric() {
        stepCount = 0.0;
        frameCount = 0.0;
        leftSum = 0.0;
        rightSum = 0.0;
    }

    public Double calculate(BVFContent.Skeleton skeleton) {
        Double rightLeg = skeleton.getByName("RightLeg").getRelativeRotation().get(1); //Vertical Axis
        Double leftLeg = skeleton.getByName("LeftLeg").getRelativeRotation().get(1); //Vertical Axis
        frameCount += 1.0;
        leftSum += leftLeg;
        rightSum += rightLeg;
        rightLeg -= rightSum/frameCount;
        leftLeg -= leftSum/frameCount;
        Boolean haveStep = false;
        if (Math.abs(rightLeg) < angle && Math.abs(leftLeg) < angle) {
            inZone = false;
        } else {
            haveStep = true;
        }
        if (haveStep && !(inZone)) {
            stepCount += 1.0;
            inZone = true;
        }
        return stepCount;
    }
}
