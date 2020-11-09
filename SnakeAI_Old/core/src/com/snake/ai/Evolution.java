package com.snake.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Random;

import static com.snake.ai.Snake.population;
import static com.snake.ai.main.hiscoreArray;
import static com.snake.ai.main.mutationMax;
import static com.snake.ai.main.mutationMin;
import static com.snake.ai.main.mutationPropability;

public class Evolution {
    public long FitnessFuntction(long steps, long score) {
        return (long) (steps + (Math.pow(2d, score) + Math.pow(score, 2.1d) * 500) - (Math.pow(score, 1.2d) * Math.pow((0.25d * steps), 1.3d)));
    }

    public long FitnessFuntction2(int steps, int score) {
        return (long) (steps + Math.pow(score,10d));
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
    public double mutation1_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int weigthNr) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        if (weigthNr == 1) {
            return weigth1 + mutationAmount;
        } else {
            return weigth2 + mutationAmount;
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
    public double mutation2_2(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int weigthNr) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        Random r = new Random();

        if (r.nextInt(100) < mutationPropability) {
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
    public double mutation3(int connectedTolayerNumber, int NodeNumber, int WeigthNumber, Snakes parent1, Snakes parent2, int weigthNr) {
        double weigth1 = parent1.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        mutationMax = Math.pow(0.97,hiscoreArray.get(population));
        mutationMin = -mutationMax;

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        //mutationAmount = Math.pow(0.98d, hiScore + 30);
        if(Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            System.out.println(mutationAmount);
        }

        Random r = new Random();
        mutationPropability = Math.pow(0.93,hiscoreArray.get(population) - 30);
        if (r.nextInt(100) < mutationPropability) {
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
