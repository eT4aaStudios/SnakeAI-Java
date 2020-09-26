package com.snake.ai;

import static com.snake.ai.main.mutationMax;
import static com.snake.ai.main.mutationMin;

public class Evolution {
    public double FitnessFuntction(int steps, int score) {
        return steps + (Math.pow(2, score) + Math.pow(score, 2.1d) * 500) - (Math.pow(score, 1.2d) * Math.pow((0.25d * steps), 1.3d));
    }

    public double FitnessFuntction2(int steps, int score) {
        return steps;
    }

    //Crossover
    public double mutation1(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        if (Math.random() * (1) == 0) {
            return weigth1;
        } else {
            return weigth2;
        }
    }

    //Crossover with mutation
    public double mutation2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        if (Math.random() * (1) == 0) {
            return weigth1 + mutationAmount;
        } else {
            return weigth2 + mutationAmount;
        }
    }

    //Crossover with mutation
    public double mutation2_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2,int menge) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        if (menge > 0) {
            return weigth1 + mutationAmount;
        } else {
            return weigth2 + mutationAmount;
        }
    }

    //Normal mutation
    public double mutation3(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        return weigth1 + mutationAmount;
    }
}
