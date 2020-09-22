package com.ai.two;

public class Evolution {
    public double FitnessFuntction(int steps,int score) {
        return steps+(Math.pow(2,score) + Math.pow(score,2.1d)*500) - (Math.pow(score,1.2d) *Math.pow((0.25d*steps),1.3d));
    }
}
