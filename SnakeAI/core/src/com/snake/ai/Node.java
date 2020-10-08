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
            if (populationGreaterZero) {
                Random r = new Random();
                int i = r.nextInt(1);
                /*if (i == 1) {
                    singlePoint(NodeNumber, LayerNumber, parent1, parent2, evo);
                } else {
                    multiplePoint(NodeNumber, LayerNumber, parent1, parent2, evo);
                }*/
                singlePoint(NodeNumber, LayerNumber, parent1, parent2, evo);
            } else {
                for (int i = 0; i < layerNodeValueArray.get(LayerNumber + 1); i++) {
                    WeigthArray.add(-1d + Math.random() * (1d - -1d));
                }
            }
        }
    }

    public void singlePoint(int NodeNumber, int LayerNumber, Snakes parent1, Snakes parent2, Evolution evo) {
        int WeigthNumber = 0;
        Random r = new Random();
        int i = r.nextInt(layerNodeValueArray.get(LayerNumber + 1));
        for (int j = 0; j < i; j++) {
            //Weight 1
            WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 1));
            WeigthNumber++;
        }
        for (int k = WeigthNumber; k < layerNodeValueArray.get(LayerNumber + 1); k++) {
            //Weight 2
            WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 2));
            WeigthNumber++;
        }
        //System.out.println("WeigthNumber: " + WeigthNumber);
    }

    public void multiplePoint(int NodeNumber, int LayerNumber, Snakes parent1, Snakes parent2, Evolution evo) {
        Random r = new Random();
        int pointmenge = 0;
        switch (LayerNumber) {
            case 0:
                pointmenge = r.nextInt(4 - 2) + 2;
                break;
            case 1:
                pointmenge = r.nextInt(3 - 1) + 1;
                break;
            case 2:
                pointmenge = r.nextInt(2 - 1) + 1;
                break;
        }
        int nodemenge = layerNodeValueArray.get(LayerNumber + 1);
        int nodemengePerPoint = nodemenge / pointmenge;
        int rest = nodemenge % pointmenge;
        //System.out.println("nodemenge: " + nodemenge);
        //System.out.println("pointmenge: " + pointmenge);
        //System.out.println("nodemengePerPoint: " + nodemengePerPoint);
        //System.out.println("rest: " + rest);

        int WeigthNumber = 0;
        for (int l = 0; l < pointmenge; l++) {
            int mengeDerErsten = r.nextInt(nodemengePerPoint);

            for (int j = 0; j < mengeDerErsten; j++) {
                //Weight 1
                //System.out.println("Weight 1");
                WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 1));
                WeigthNumber++;
            }
            for (int j = 0; j < nodemengePerPoint - mengeDerErsten; j++) {
                //Weight 2
                //System.out.println("Weight 2");
                WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 2));
                WeigthNumber++;

            }
            //System.out.println("WeigthNumber: " + WeigthNumber);
        }


        if (rest > 0) {
            int m = r.nextInt(rest);
            //System.out.println("m: " + m);
            for (int j = 0; j < m; j++) {
                //Weight 1
                //System.out.println("Weight 1");
                WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 1));
                WeigthNumber++;
            }
            for (int j = m; j < rest; j++) {
                //Weight 2
                //System.out.println("Weight 2");
                WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, WeigthNumber, parent1, parent2, 2));
                WeigthNumber++;

            }
        }
    }
}
