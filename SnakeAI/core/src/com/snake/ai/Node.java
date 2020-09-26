package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

import static com.snake.ai.main.LayerMenge;
import static com.snake.ai.main.layerNodeValueArray;

public class Node {
    public Array<Double> WeigthArray;
    public double value;

    public Node(int NodeNumber, int LayerNumber, boolean populationGreaterZero, Snakes parent1, Snakes parent2) {
        if (LayerNumber + 1 < LayerMenge) {
            Evolution evo = new Evolution();

            WeigthArray = new Array<>();
            /*for (int WeigthNumber = 0; WeigthNumber < layerNodeValueArray.get(LayerNumber + 1); WeigthNumber++) {
                if (populationGreaterZero)
                    WeigthArray.add(evo.mutation2(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2));
                else
                    WeigthArray.add(-1d + Math.random() * (1d - -1d));
            }*/

            for (int WeigthNumber = 0; WeigthNumber < layerNodeValueArray.get(LayerNumber + 1); WeigthNumber++) {
                Random r = new Random();
                int i = r.nextInt(layerNodeValueArray.get(LayerNumber + 1));
                for(int j = i;j >= 0;j--) {
                    if (populationGreaterZero)
                        WeigthArray.add(evo.mutation2_2(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2,j));
                    else
                        WeigthArray.add(-1d + Math.random() * (1d - -1d));
                }
                for (int k = i; k < layerNodeValueArray.get(LayerNumber + 1); k++) {
                    if (populationGreaterZero)
                        WeigthArray.add(evo.mutation2_2(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2,0));
                    else
                        WeigthArray.add(-1d + Math.random() * (1d - -1d));
                }
                WeigthNumber = layerNodeValueArray.get(LayerNumber + 1);
            }
        }
    }
}
