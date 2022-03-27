package com.snake.ai;


import static com.snake.ai.main.layerNodeValueArray;

import com.badlogic.gdx.utils.Array;

public class Layer {

    Array<Node> nodeArray;

    public Layer(int LayerNumber, Snake parent1, Snake parent2, int layerSinglePoint, int nodeSinglePoint, int weightSinglePoint) {
        nodeArray = new Array<>();

        for (int i = 0; i < layerNodeValueArray.get(LayerNumber); i++) {
            Node newNode = new Node(i, LayerNumber, parent1, parent2,layerSinglePoint,nodeSinglePoint,weightSinglePoint);
            nodeArray.add(newNode);
        }

    }
}
