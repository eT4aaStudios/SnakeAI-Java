package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.layerNodeValueArray;

public class Node {
    public Array<Double> WeigthArray;
    public double value;

    public Node(int NodeNumber, int LayerNumber, boolean populationGreaterZero, Snakes parent1, Snakes parent2) {
        if (LayerNumber + 1 < LayerMenge) {
            Evolution evo = new Evolution();

            WeigthArray = new Array<>();
            for (int WeigthNumber = 0; WeigthNumber < layerNodeValueArray.get(LayerNumber + 1); WeigthNumber++) {
                if (populationGreaterZero)
                    WeigthArray.add(evo.mutation1(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2));
                else
                    WeigthArray.add(-1d + Math.random() * (1d - -1d));
            }
        }
    }
}
