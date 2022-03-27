package com.snake.ai;

import static com.snake.ai.main.evo;
import static com.snake.ai.main.layerNodeValueArray;
import static com.snake.ai.main.r;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.snakeGameInstance;

import com.badlogic.gdx.utils.Array;

public class Node {
    public Array<Double> weightArray;
    public double value;
    public double bias;

    public Node(int NodeNumber, int LayerNumber, Snake parent1, Snake parent2, int layerSinglePoint, int nodeSinglePoint, int weightSinglePoint) {
        if (LayerNumber + 1 < settings.layerMenge) {
            weightArray = new Array<>();
            if (snakeGameInstance.population > 0) {
                singlePointBinaryCrossover(NodeNumber, LayerNumber,layerSinglePoint, nodeSinglePoint, weightSinglePoint, parent1, parent2);
            } else {
                for (int i = 0; i < layerNodeValueArray.get(LayerNumber + 1); i++) {
                    weightArray.add(-1f + r.nextDouble() * (1f - -1f));
                }
                bias = -1f + r.nextDouble() * (1f - -1f);
            }
        }
    }

    public void singlePointBinaryCrossover(int nodeNumber, int layerNumber,int layerSinglePoint, int nodeSinglePoint, int weightSinglePoint, Snake parent1, Snake parent2) {
        if (layerNumber < layerSinglePoint && nodeNumber < nodeSinglePoint)
            bias = parent1.layerArray.get(layerNumber).nodeArray.get(nodeNumber).bias;
        else
            bias = parent2.layerArray.get(layerNumber).nodeArray.get(nodeNumber).bias;
        bias = evo.mutation(bias);

        for (int i = 0; i < parent1.layerArray.get(layerNumber).nodeArray.get(nodeNumber).weightArray.size; i++) {
            double weight;
            if (layerNumber < layerSinglePoint && nodeNumber < nodeSinglePoint && i < weightSinglePoint)
                weight = parent1.layerArray.get(layerNumber).nodeArray.get(nodeNumber).weightArray.get(i);
            else
                weight = parent2.layerArray.get(layerNumber).nodeArray.get(nodeNumber).weightArray.get(i);

            weight = evo.mutation(weight);
            weightArray.add(weight);
        }
    }

    public void simulatedBinaryCrossover(int nodeNumber, int layerNumber, int nodeSinglePoint, int weightSinglePoint, Snake parent1, Snake parent2) {
        for (int i = 0; i < parent1.layerArray.get(layerNumber).nodeArray.get(nodeNumber).weightArray.size; i++) {
            double weight;
            if (nodeNumber < nodeSinglePoint && i < weightSinglePoint)
                weight = parent1.layerArray.get(layerNumber).nodeArray.get(nodeNumber).weightArray.get(i);
            else
                weight = parent2.layerArray.get(layerNumber).nodeArray.get(nodeNumber).weightArray.get(i);

            weight = evo.mutation(weight);
            weightArray.add(weight);
        }
    }
}
