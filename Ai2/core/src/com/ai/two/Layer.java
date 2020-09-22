package com.ai.two;


import com.badlogic.gdx.utils.Array;

public class Layer {

    Array<Node> NodeArray;

    public Layer(int LayerNumber,boolean populationGreaterZero,Snakes parent1,Snakes parent2) {
        NodeArray = new Array<>();

                for (int i = 0;i < main.inputLayerNodes;i++) {
                    Node newNode = new Node(i,LayerNumber,populationGreaterZero,parent1,parent2);
                    NodeArray.add(newNode);
                }
    }
}
