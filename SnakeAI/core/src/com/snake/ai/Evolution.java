package com.snake.ai;

import java.util.Random;

import static com.snake.ai.Snake.population;
import static com.snake.ai.main.mutationMax;
import static com.snake.ai.main.mutationMin;
import static com.snake.ai.main.mutationPropability;

public class Evolution {
    public int FitnessFuntction(int steps, int score) {
        return (int) (steps + (Math.pow(2d, score) + Math.pow(score, 2.1d) * 500) - (Math.pow(score, 1.2d) * Math.pow((0.25d * steps), 1.3d)));
    }

    public double FitnessFuntction2(int steps, int score) {
        return steps;
    }

    public double FitnessFuntction3(int steps, int score) {
        return steps * 5d / (population + 1) + (score * population / 3d + 1);
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

    //Crossover
    public double mutation1_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int menge) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        if (menge > 0) {
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
    public double mutation2_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int menge) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        Random r = new Random();

        if (r.nextInt(100) < mutationPropability) {
            if (menge > 0) {
                return weigth1 + mutationAmount;
            } else {
                return weigth2 + mutationAmount;
            }
        } else {
            if (menge > 0) {
                return weigth1;
            } else {
                return weigth2;
            }
        }
    }

    //Normal mutation
    public double mutation3(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        return weigth1 + mutationAmount;
    }
}
