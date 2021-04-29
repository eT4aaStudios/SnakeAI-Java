package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import static com.snake.ai.Snake.population;
import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.loadBestSnakeEver;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.r;


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

        parent1Snake = tempArray.random();
        tempArray.removeValue(parent1Snake,false);
        parent2Snake = tempArray.random();
    }

    //Roulette
    public void selectParents2() {
        //Parent 1
        double maxFitness = 0d;
        for (int i = 0; i <allSnakesArrays.get(0).allSnakesArray.size; i++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
        }
        double choosenId = 0 + r.nextDouble() * (maxFitness - 0);

        double zahlZumChecken = 0;
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

        choosenId = 0 + r.nextDouble() * (maxFitness - 0);


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
