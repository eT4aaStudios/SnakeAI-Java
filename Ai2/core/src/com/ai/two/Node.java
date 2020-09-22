package com.ai.two;

import com.badlogic.gdx.utils.Array;

import static com.ai.two.main.layerNodeValueArray;
import static com.ai.two.main.mutationMin;
import static com.ai.two.main.mutationMax;

public class Node {
    public Array<Double> WeigthArray;
    public double value;

    public Node(int NodeNumber, int LayerNumber, boolean populationGreaterZero, Snakes parent1, Snakes parent2) {
        WeigthArray = new Array<>();
        for (int WeigthNumber = 0; WeigthNumber < layerNodeValueArray.get(LayerNumber); WeigthNumber++) {
            if (populationGreaterZero)
                WeigthArray.add(combineWeigths(LayerNumber,NodeNumber,WeigthNumber, parent1, parent2));
            else
                WeigthArray.add(-1d + Math.random() * (1d - -1d));
        }
    }

    public double combineWeigths(int connectedTolayerNumber,int NodeNumber,int WeigthNumber, Snakes parent1, Snakes parent2) {
        double weigth1 = parent1.
                layerArray.get(connectedTolayerNumber).
                NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);
        double weigth2 = parent2.layerArray.get(connectedTolayerNumber).NodeArray.get(NodeNumber).WeigthArray.get(WeigthNumber);

        double mutationAmount = mutationMin + Math.random() * (mutationMax - mutationMin);
        if(Math.random() * (1) == 0) {
            return weigth1 + mutationAmount;
        }else {
            return weigth2 + mutationAmount;
        }
    }
}
