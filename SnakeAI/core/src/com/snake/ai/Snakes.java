package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.bestSnakesArray;


public class Snakes {

    protected static Array<Layer> layerArray;
    protected int score;
    protected double fitness;
    protected Snakes parent1Snake, parent2Snake;

    public Snakes() {
        if (Snake.population > 0) {
            selectParents();
            System.out.println("parent1Snake: " + parent1Snake);
            System.out.println("parent2Snake: " + parent2Snake);
        }
        layerArray = new Array<>();

        for (int i = 0; i < LayerMenge; i++) {
            Layer layer;
            if (Snake.population > 0) {
                layer = new Layer(i, true, parent1Snake, parent2Snake);
            } else
                layer = new Layer(i, false, parent1Snake, parent2Snake);
            layerArray.add(layer);
        }

        System.out.println("layerArray: " + layerArray);
    }

    public void selectParents() {
        Array<Snakes> tempArray = new Array<>(bestSnakesArray);
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
                    parent1Snake = choosenId;
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
                    parent2Snake = choosenId;
                }
            }
            j++;
        }
    }
}
