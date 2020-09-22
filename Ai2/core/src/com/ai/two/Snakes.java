package com.ai.two;

import com.badlogic.gdx.utils.Array;

import static com.ai.two.main.LayerMenge;
import static com.ai.two.main.bestSnakesArray;

public class Snakes {

    public static Array<Layer> layerArray;
    int score;
    double fitness;
    Snakes parent1Snake, parent2Snake;

    public Snakes() {
        if (Snake.population > 0) {
            selectParents();
        } else
            layerArray = new Array<>();
        for (int i = 0; i < LayerMenge; i++) {
            Layer layer;
            if (Snake.population > 0)
                layer = new Layer(i, true, parent1Snake, parent2Snake);
            else
                layer = new Layer(i, false, parent1Snake, parent2Snake);
            layerArray.add(layer);
        }
    }

    public void selectParents() {
        Array<Snakes> tempArray = new Array<>(bestSnakesArray);
        double max = 1;
        for (int i = 0; i < bestSnakesArray.size; i++) {
            max += bestSnakesArray.get(i).fitness;
        }

        int choosenNumber = (int) ((1d + Math.random() * (max - 1d)));

        int fitnesschecked = 0;
        int choosenId = 0;
        for(int j = 0;j < bestSnakesArray.size;){
            for(int k = 0;k < bestSnakesArray.get(j).fitness;k++) {
                fitnesschecked++;
                if (choosenNumber == fitnesschecked) {
                    choosenId = j;
                    parent1Snake = bestSnakesArray.get(choosenId);
                }
            }
            j++;
        }

        bestSnakesArray.removeIndex(choosenId);
        max = 1;
        for (int i = 0; i < bestSnakesArray.size; i++) {
            max += bestSnakesArray.get(i).fitness;
        }

        choosenNumber = (int) ((1d + Math.random() * (max - 1d)));

        fitnesschecked = 0;
        choosenId = 0;
        for(int j = 0;j < bestSnakesArray.size;){
            for(int k = 0;k < bestSnakesArray.get(j).fitness;k++) {
                fitnesschecked++;
                if (choosenNumber == fitnesschecked) {
                    choosenId = j;
                    parent2Snake = bestSnakesArray.get(choosenId);
                }
            }
            j++;
        }

        bestSnakesArray = new Array<>(tempArray);
    }
}
