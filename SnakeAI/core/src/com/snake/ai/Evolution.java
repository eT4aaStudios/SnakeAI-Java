package com.snake.ai;

import java.util.Random;

import static com.snake.ai.Snake.population;
import static com.snake.ai.main.hiscoreArray;
import static com.snake.ai.main.mutationMax;
import static com.snake.ai.main.mutationMin;
import static com.snake.ai.main.mutationPropability;

public class Evolution {
    public double FitnessFuntction(int steps, int score) {
        return steps + (Math.pow(1.1d, score) + Math.pow(score, 2.1d) * 500) - (Math.pow(score, 1.2d) * Math.pow((0.25d * steps), 1.3d));
    }

    public int FitnessFuntction2(int steps, int score) {
        return (int) ((Math.pow(1.1d, score) + Math.pow(score, 2.1d) * 500) - Math.pow(score, 1.2d));
    }

    //Crossover
    public float mutation1(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2) {
        float weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        float weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        if (Math.random() * (1) == 0) {
            return weigth1;
        } else {
            return weigth2;
        }
    }

    //Crossover
    public float mutation1_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int weigthNr) {
        float weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        float weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        float mutationAmount = (float) (mutationMin + Math.random() * (mutationMax - mutationMin));
        if (weigthNr == 1) {
            return weigth1 + mutationAmount;
        } else {
            return weigth2 + mutationAmount;
        }
    }

    //Crossover with mutation
    public float mutation2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2) {
        float weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        float weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        float mutationAmount = (float) (mutationMin + Math.random() * (mutationMax - mutationMin));
        if (Math.random() * (1) == 0) {
            return weigth1 + mutationAmount;
        } else {
            return weigth2 + mutationAmount;
        }
    }

    //Crossover with mutation
    public float mutation2_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int weigthNr) {
        float weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        float weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        float mutationAmount = (float) (mutationMin + Math.random() * (mutationMax - mutationMin));
        Random r = new Random();

        if (r.nextFloat() * 100 < mutationPropability) {
            //With Mutation
            if (weigthNr == 1) {
                if (weigth1 + mutationAmount >= -1 && weigth1 + mutationAmount <= 1)
                    return weigth1 + mutationAmount;
                else if (weigth1 + mutationAmount <= -1)
                    return -1;
                else
                    return 1;
            } else {
                if (weigth2 + mutationAmount > -1 && weigth2 + mutationAmount < 1)
                    return weigth2 + mutationAmount;
                else if (weigth2 + mutationAmount < -1)
                    return -1;
                else
                    return 1;
            }
        } else {
            //Without Mutation
            if (weigthNr == 1) {
                return weigth1;
            } else {
                return weigth2;
            }
        }
    }

    //Normal mutation
    public float mutation3(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int weigthNr) {
        float weigth1 = 0;
        try {
            weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        }catch (Exception ignored) {

        }

        //mutationMax = (float) Math.pow(0.97,hiscoreArray.get(population));
        mutationMin = -mutationMax;

        Random r = new Random();
        float mutationAmount = (float) r.nextGaussian();

        //mutationPropability = (float) Math.pow(0.93,hiscoreArray.get(population) - 30);

        if (r.nextFloat() * 100 < mutationPropability) {
            //With Mutation
            if (weigth1 + mutationAmount > -1 && weigth1 + mutationAmount < 1)
                return weigth1 + mutationAmount;
            else if (weigth1 + mutationAmount < -1)
                return -1;
            else if (weigth1 + mutationAmount > 1)
                return 1;
        }
        //Without Mutation
        return weigth1;
    }
}
