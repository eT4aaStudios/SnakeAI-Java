package com.snake.ai;


import static com.snake.ai.main.layerNodeValueArray;

import com.badlogic.gdx.utils.Array;

public class Layer {

    Array<Node> NodeArray;

    public Layer(int LayerNumber, boolean populationGreaterZero, Snake parent1, Snake parent2) {
        NodeArray = new Array<>();

        for (int i = 0; i < layerNodeValueArray.get(LayerNumber); i++) {
            Node newNode = new Node(i, LayerNumber, populationGreaterZero, parent1, parent2);
            NodeArray.add(newNode);
        }
    }
}
