package ru.hackrussia.SMT.MetricsSamples;

import ru.hackrussia.SMT.BVFParser.BVFContent;
import ru.hackrussia.SMT.MetricsCalculator.MetricsInterface;

public class HandFootSyncronize implements MetricsInterface{
    private Double leftPrevHeight = 0.0;
    private Double rightPrevHeight = 0.0;
    
    public Double calculate(BVFContent.Skeleton skeleton) {
        Double leftHand = skeleton.getByName("LeftHand").getAbsoluteOffset().get(1);
        Double leftFoot = skeleton.getByName("LeftFoot").getAbsoluteOffset().get(1);
        Double rightHand = skeleton.getByName("RightHand").getAbsoluteOffset().get(1);
        Double rightFoot = skeleton.getByName("RightFoot").getAbsoluteOffset().get(1);
        Double leftHeight = Math.abs(leftHand - leftFoot);
        Double rightHeight = Math.abs(rightHand - rightFoot);
        Double result = (leftHeight + rightHeight + leftPrevHeight + rightPrevHeight) / 4;
        leftPrevHeight = leftHeight;
        rightPrevHeight = rightHeight;
        return result;
    }
}
