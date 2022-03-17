package com.snake.ai;

import static com.snake.ai.Settings.crossover;
import static com.snake.ai.Settings.layerMenge;
import static com.snake.ai.main.bestSnakes;
import static com.snake.ai.main.fitnessDoubleToE;
import static com.snake.ai.main.layerNodeValueArray;
import static com.snake.ai.main.loadBestSnakeEver;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.r;
import static com.snake.ai.main.snakeGameInstance;

import com.badlogic.gdx.utils.Array;


public class Snake {

    protected Array<Layer> layerArray;
    protected short score;
    protected double fitness;
    protected Snake parent1Snake, parent2Snake;

    public String toString() {
        return "Score:" + score + "|Fitness:" + fitnessDoubleToE(fitness);
    }

    public Snake() {
        if (snakeGameInstance.population > 0 && !loadFromSavedSnake && !loadBestSnakeEver) {
            selectParents();
        }
        layerArray = new Array<>();

        int layerSinglePoint = r.nextInt(layerMenge - 1);
        int nodeSinglePoint = r.nextInt(layerNodeValueArray.get(layerSinglePoint));
        int weightSinglePoint = r.nextInt(layerNodeValueArray.get(layerSinglePoint + 1));

        if(!crossover)
            parent2Snake = parent1Snake;

        for (int i = 0; i < layerMenge; i++) {
            Layer layer = new Layer(i, parent1Snake, parent2Snake,layerSinglePoint,nodeSinglePoint,weightSinglePoint);
            layerArray.add(layer);
        }
    }

    //Roulette
    public void selectParents() {
        //Parent 1
        double maxFitness = 0d;
        for (int i = 0; i < bestSnakes.size; i++) {
            maxFitness += bestSnakes.get(i).fitness;
        }
        double choosenId = 0 + r.nextDouble() * (maxFitness - 0);

        double zahlZumChecken = 0;
        for (int i = 0; i < bestSnakes.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + bestSnakes.get(i).fitness) {
                parent1Snake = bestSnakes.get(i);
                break;
            } else {
                zahlZumChecken += bestSnakes.get(i).fitness;
            }
        }

        //Parent 2
        maxFitness = 0;
        for (int i = 0; i < bestSnakes.size; i++) {
            maxFitness += bestSnakes.get(i).fitness;
        }

        choosenId = 0 + r.nextDouble() * (maxFitness - 0);

        zahlZumChecken = 0;
        for (int i = 0; i < bestSnakes.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + bestSnakes.get(i).fitness) {
                parent2Snake = bestSnakes.get(i);
                i = bestSnakes.size;
            } else {
                zahlZumChecken += bestSnakes.get(i).fitness;
            }
        }
    }
}
