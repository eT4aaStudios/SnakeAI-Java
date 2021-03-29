package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

import static com.snake.ai.Snake.population;
import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.bestSnakesArraySize;
import static com.snake.ai.main.loadBestSnakeEver;
import static com.snake.ai.main.loadFromSavedSnake;


public class Snakes {

    protected Array<Layer> layerArray;
    protected int score;
    protected double fitness;
    protected Snakes parent1Snake, parent2Snake;

    public Snakes() {
        if (population > 0 && !loadFromSavedSnake && !loadBestSnakeEver) {
            selectParents2();
        }
        layerArray = new Array<>();

        for (int i = 0; i < LayerMenge; i++) {
            Layer layer;
            if (population > 0) {
                layer = new Layer(i, true, parent1Snake, parent2Snake);
            } else
                layer = new Layer(i, false, parent1Snake, parent2Snake);
            layerArray.add(layer);
        }
    }

    //Out of the Best
    public void selectParents() {
        Array<Snakes> tempArray = new Array<>(bestSnakesArray);
        float max = 0;

        int choosenNumber = (int) ((1 + Math.random() * (bestSnakesArraySize - 1)));
        parent1Snake = bestSnakesArray.get(choosenNumber);
        tempArray.removeIndex(choosenNumber);
        choosenNumber = (int) ((1 + Math.random() * (bestSnakesArraySize - 2)));
        parent2Snake = bestSnakesArray.get(choosenNumber);
    }

    //Roulette
    public void selectParents2() {
        //Parent 1
        int maxFitness = 0;
        for (int i = 0; i < bestSnakesArray.size; i++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
        }

        Random r = new Random();
        int choosenId = r.nextInt(maxFitness);

        int zahlZumChecken = 0;
        for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + allSnakesArrays.get(0).allSnakesArray.get(i).fitness) {
                parent1Snake = allSnakesArrays.get(0).allSnakesArray.get(i);
                i = allSnakesArrays.get(0).allSnakesArray.size;
            } else {
                zahlZumChecken += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
            }
        }

        //Parent 2
        maxFitness = 0;
        for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
        }

        r = new Random();
        choosenId = r.nextInt(maxFitness);


        zahlZumChecken = 0;
        for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + allSnakesArrays.get(0).allSnakesArray.get(i).fitness) {
                parent2Snake = allSnakesArrays.get(0).allSnakesArray.get(i);
                i = allSnakesArrays.get(0).allSnakesArray.size;
            } else {
                zahlZumChecken += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
            }
        }
    }

    //Roulette out of Bests
    public void selectParents3() {
        //Parent 1
        int maxFitness = 0;
        for (int i = 0; i < bestSnakesArray.size; i++) {
            maxFitness += bestSnakesArray.get(i).fitness;
        }

        Random r = new Random();
        int choosenId = r.nextInt(maxFitness);

        int zahlZumChecken = 0;
        for (int i = 0; i < bestSnakesArray.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + bestSnakesArray.get(i).fitness) {
                parent1Snake = bestSnakesArray.get(i);
                i = bestSnakesArray.size;
            } else {
                zahlZumChecken += bestSnakesArray.get(i).fitness;
            }
        }

        //Parent 2
        maxFitness = 0;
        for (int i = 0; i < bestSnakesArray.size; i++) {
            maxFitness += bestSnakesArray.get(i).fitness;
        }

        r = new Random();
        choosenId = r.nextInt(maxFitness);


        zahlZumChecken = 0;
        for (int i = 0; i < bestSnakesArray.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + bestSnakesArray.get(i).fitness) {
                parent2Snake = bestSnakesArray.get(i);
                i = bestSnakesArray.size;
            } else {
                zahlZumChecken += bestSnakesArray.get(i).fitness;
            }
        }
    }
}
