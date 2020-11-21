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

public class Node {
    public Array<Float> WeigthArray;
    public float value;
    Evolution evo;

    public Node(int NodeNumber, int LayerNumber, boolean populationGreaterZero, Snakes parent1, Snakes parent2) {
        if (LayerNumber + 1 < LayerMenge) {

            evo = new Evolution();
            WeigthArray = new Array<>();
            if (populationGreaterZero) {
                Random r = new Random();
                int i = r.nextInt(1);
                /*if (i == 1) {
                    singlePoint(NodeNumber, LayerNumber, parent1, parent2);
                } else {
                    multiplePoint(NodeNumber, LayerNumber, parent1, parent2);
                }*/
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

    public void noPoint(int NodeNumber, int LayerNumber, Snakes parent1, Snakes parent2) {
        for (int i = 0; i < layerNodeValueArray.get(LayerNumber + 1); i++) {
            //Weight 1
            WeigthArray.add(evo.mutation3(LayerNumber, NodeNumber, i, parent1, parent2, 1));
        }
    }

    public void singlePoint(int NodeNumber, int LayerNumber, Snakes parent1, Snakes parent2) {
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
    }

    public void multiplePoint(int NodeNumber, int LayerNumber, Snakes parent1, Snakes parent2) {
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
