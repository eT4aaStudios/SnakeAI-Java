package com.snake.ai;

import static com.snake.ai.main.mutationMax;
import static com.snake.ai.main.mutationMin;
import static com.snake.ai.main.mutationPropability;
import static com.snake.ai.main.r;

public class Evolution {
    public double FitnessFuntction(int steps, int score) {
        return steps + (Math.pow(2d, score) + Math.pow(score, 2.1d) * 500) - (Math.pow(score, 1.2d) * Math.pow((0.25d * steps), 1.3d));
    }


    public double FitnessFuntction2(int steps, int score) {
        return score * score;
    }


    //Crossover
    public double mutation1(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snake parent1, Snake parent2) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        if (r.nextDouble() * (1) == 0) {
            return weigth1;
        } else {
            return weigth2;
        }
    }

    //Crossover
    public double mutation1_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snake parent1, Snake parent2, int weigthNr) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + r.nextDouble() * (mutationMax - mutationMin);
        if (weigthNr == 1) {
            return weigth1 + mutationAmount;
        } else {
            return weigth2 + mutationAmount;
        }
    }

    //Crossover with mutation
    public double mutation2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snake parent1, Snake parent2) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + r.nextDouble() * (mutationMax - mutationMin);
        if (r.nextDouble() * (1) == 0) {
            return weigth1 + mutationAmount;
        } else {
            return weigth2 + mutationAmount;
        }
    }

    //Crossover with mutation
    public double mutation2_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snake parent1, Snake parent2, int weigthNr) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + r.nextDouble() * (mutationMax - mutationMin);

        if (r.nextDouble() * 100 < mutationPropability) {
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
    public double mutation3(double weight) {
        //mutationMax = (float) Math.pow(0.97,hiscoreArray.get(population));
        //mutationMin = -mutationMax;
        double mutationAmount = r.nextGaussian();

        if (r.nextDouble() * 100 <= mutationPropability) {
            //With Mutation
            return Math.max(-1, Math.min(1, weight + mutationAmount));
        }
        //Without Mutation
        return weight;
    }
}
