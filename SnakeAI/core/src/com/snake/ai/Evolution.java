package com.snake.ai;

import static com.snake.ai.main.eval;
import static com.snake.ai.main.r;
import static com.snake.ai.main.settings;

public class Evolution {

    public double fitnessFunction(String formula, int steps, int score) {
        formula = formula.replace("Score",""+score);
        formula = formula.replace("Steps",""+steps);
        return eval(formula);
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
