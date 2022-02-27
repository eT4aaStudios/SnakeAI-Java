package com.snake.ai;

import static com.snake.ai.SnakeGame.population;
import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.loadBestSnakeEver;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.r;

import com.badlogic.gdx.utils.Array;


public class Snake {

    protected Array<Layer> layerArray;
    protected short score;
    protected double fitness;
    protected Snake parent1Snake, parent2Snake;

    public Snake() {
        if (population > 0 && !loadFromSavedSnake && !loadBestSnakeEver) {
            selectParents3();
        }
        layerArray = null;
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

    public void dispose() {
        if (layerArray != null) {
            for (int i = 0; i < layerArray.size; i++) {
                if (layerArray.get(i).NodeArray != null) {
                    for (int j = 0; j < layerArray.get(i).NodeArray.size; j++) {
                        if (layerArray.get(i).NodeArray.get(j).WeigthArray != null)
                            layerArray.get(i).NodeArray.get(j).WeigthArray.clear();
                        layerArray.get(i).NodeArray.get(j).WeigthArray = null;
                    }
                    layerArray.get(i).NodeArray.clear();
                    layerArray.get(i).NodeArray = null;
                }
            }
            layerArray.clear();
            layerArray = null;
        }
        if (parent1Snake != null)
            parent1Snake = null;
        if (parent2Snake != null)
            parent2Snake = null;
    }

    //Out of the Best
    public void selectParents() {
        Array<Snake> tempArray = new Array<>(bestSnakesArray);

        parent1Snake = tempArray.random();
        tempArray.removeValue(parent1Snake, false);
        parent2Snake = tempArray.random();
    }

    //Roulette
    public void selectParents2() {
        //Parent 1
        double maxFitness = 0d;
        for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
        }
        double choosenId = 0 + r.nextDouble() * (maxFitness - 0);

        double zahlZumChecken = 0;
        for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + allSnakesArrays.get(0).allSnakesArray.get(i).fitness) {
                parent1Snake = allSnakesArrays.get(0).allSnakesArray.get(i);
                break;
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
                break;
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
