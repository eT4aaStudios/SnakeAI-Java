package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import static com.snake.ai.Snake.population;
import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.bestArrays;


public class Snakes {

    protected Array<Layer> layerArray;
    protected int score;
    protected double fitness;
    protected Snakes parent1Snake, parent2Snake;

    public Snakes() {
        if (population > 0) {
            selectParents();
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

    public void selectParents() {
        Array<Snakes> tempArray = new Array<>(bestArrays.get(population - 1).bestSnakesArray);
        double max = 1;
        for (int i = 0; i < tempArray.size; i++) {
            max += tempArray.get(i).fitness;
        }

        int choosenNumber = (int) ((1d + Math.random() * (max - 1d)));

        int fitnesschecked = 0;
        int choosenId = 0;
        for (int j = 0; j < tempArray.size; ) {
            for (int k = 0; k < tempArray.get(j).fitness; k++) {
                fitnesschecked++;
                if (choosenNumber == fitnesschecked) {
                    choosenId = j;
                    parent1Snake = bestArrays.get(population - 1).bestSnakesArray.get(choosenId);
                }
            }
            j++;
        }

        tempArray.removeIndex(choosenId);
        max = 1;
        for (int i = 0; i < tempArray.size; i++) {
            max += tempArray.get(i).fitness;
        }

        choosenNumber = (int) ((1d + Math.random() * (max - 1d)));

        fitnesschecked = 0;
        choosenId = 0;
        for (int j = 0; j < tempArray.size; ) {
            for (int k = 0; k < tempArray.get(j).fitness; k++) {
                fitnesschecked++;
                if (choosenNumber == fitnesschecked) {
                    choosenId = j;
                    parent2Snake = bestArrays.get(population - 1).bestSnakesArray.get(choosenId);
                }
            }
            j++;
        }
    }
}
