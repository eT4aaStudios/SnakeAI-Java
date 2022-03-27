package com.snake.ai;

import static com.snake.ai.main.r;
import static com.snake.ai.main.settings;

public class Evolution {

    public double fitnessFunction(int steps, int score) {
        return steps + (Math.pow(2d, score) + Math.pow(score, 2.1d) * 500d) - (Math.pow(score, 1.2d) * Math.pow((0.25d * steps), 1.3d));
    }

    public double fitnessFunction2(int steps, int score) {
        return steps;
    }


    //Normal mutation
    public double mutation(double weight) {
        //settings.mutationMax = (float) Math.pow(0.97,hiscoreArray.get(population));

        //double mutationAmount = Math.random();
        double mutationAmount = r.nextGaussian();

        //mutationAmount -= -1 + (mutationAmount * 2);
        mutationAmount *= settings.mutationMax;

        if (r.nextDouble() * 100 <= settings.mutationProbability) {
            return weight + mutationAmount;
        }

        return weight;
    }
}
