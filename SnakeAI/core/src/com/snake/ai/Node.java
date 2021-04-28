package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

import static com.snake.ai.SavedSnakes.getFloat;
import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.SnakeNr;
import static com.snake.ai.main.gameNr;
import static com.snake.ai.main.layerNodeValueArray;
import static com.snake.ai.main.loadBestSnakeEver;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.nextInt;

public class Node {
    public Array<Float> WeigthArray;
    public double value;
    Evolution evo = new Evolution();

    public Node(int NodeNumber, int LayerNumber, boolean populationGreaterZero, Snakes parent1, Snakes parent2) {
        if (LayerNumber + 1 < LayerMenge) {
            WeigthArray = null;
            WeigthArray = new Array<>();
            if (populationGreaterZero) {
                if (loadBestSnakeEver) {
                    for (int m = 0; m < layerNodeValueArray.get(LayerNumber + 1); m++) {
                        WeigthArray.add(
                                getFloat("gameNr " + 0 +
                                        " bestSnakeEver.bestSnakeEver " +
                                        " LayerNr " + LayerNumber +
                                        " NodeNr " + NodeNumber +
                                        " WeightNr " + m)
                        );
                    }
                } else if (loadFromSavedSnake) {
                    for (int m = 0; m < layerNodeValueArray.get(LayerNumber + 1); m++) {
                        WeigthArray.add(
                                getFloat("gameNr " + gameNr +
                                        " SnakeNr " + SnakeNr +
                                        " LayerNr " + LayerNumber +
                                        " NodeNr " + NodeNumber +
                                        " WeightNr " + m)
                        );
                    }
                } else {
                    singlePoint(NodeNumber, LayerNumber, parent1, parent2);
                }
            } else {
                for (int i = 0; i < layerNodeValueArray.get(LayerNumber + 1); i++) {
                    WeigthArray.add((float) (-1f + Math.random() * (1f - -1f)));
                }
            }
        }
    }

    public void singlePoint(int NodeNumber, int LayerNumber, Snakes parent1, Snakes parent2) {
        int WeigthNumber = 0;
        int i = nextInt(layerNodeValueArray.get(LayerNumber + 1));
        for (int j = 0; j < i; j++) {
            //Weight 1
            WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 1));
            WeigthNumber++;
        }
        for (int k = WeigthNumber; k < layerNodeValueArray.get(LayerNumber + 1); k++) {
            //Weight 2
            //TODO Memory Leak
            WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 2));
            WeigthNumber++;
        }
    }

}
