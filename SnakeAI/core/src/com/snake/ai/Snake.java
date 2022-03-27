package com.snake.ai;

import static com.snake.ai.main.bestSnakes;
import static com.snake.ai.main.fitnessDoubleToE;
import static com.snake.ai.main.layerNodeValueArray;
import static com.snake.ai.main.loadingSavedGame;
import static com.snake.ai.main.r;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.snakeGameInstance;

import com.badlogic.gdx.utils.Array;


public class Snake {

    protected Array<Layer> layerArray;
    protected short score;
    protected double fitness;

    public String toString() {
        return "Score:" + score + "|Fitness:" + fitnessDoubleToE(fitness);
    }

    public Snake() {
        if (!loadingSavedGame) {
            Snake parent1Snake = null, parent2Snake = null;
            if (snakeGameInstance.population > 0) {
                parent1Snake = selectParent1();
                parent2Snake = selectParent2(parent1Snake);
            }
            layerArray = new Array<>();

            int layerSinglePoint = r.nextInt(settings.layerMenge - 1);
            int nodeSinglePoint = r.nextInt(layerNodeValueArray.get(layerSinglePoint));
            int weightSinglePoint = r.nextInt(layerNodeValueArray.get(layerSinglePoint + 1));

            if (!settings.crossover)
                parent2Snake = parent1Snake;

            for (int i = 0; i < settings.layerMenge; i++) {
                Layer layer = new Layer(i, parent1Snake, parent2Snake, layerSinglePoint, nodeSinglePoint, weightSinglePoint);
                layerArray.add(layer);
            }
        }
    }

    //Roulette
    public Snake selectParent1() {
        //Parent 1
        double maxFitness = 0;
        for (int i = 0; i < bestSnakes.size; i++) {
            maxFitness += bestSnakes.get(i).fitness;
        }

        double choosenId = maxFitness * r.nextDouble();

        double zahlZumChecken = 0;
        for (int i = 0; i < bestSnakes.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + bestSnakes.get(i).fitness) {
                return bestSnakes.get(i);
            } else {
                zahlZumChecken += bestSnakes.get(i).fitness;
            }
        }
        return null;
    }

    //Roulette
    public Snake selectParent2(Snake parent1) {
        //Parent 2
        double maxFitness = 0;
        Array<Snake> tmpArray = new Array<>(bestSnakes);
        tmpArray.removeValue(parent1, false);

        for (int i = 0; i < tmpArray.size; i++) {
            maxFitness += tmpArray.get(i).fitness;
        }

        double choosenId = maxFitness * r.nextDouble();

        double zahlZumChecken = 0;
        for (int i = 0; i < tmpArray.size; i++) {
            if (choosenId >= zahlZumChecken && choosenId <= zahlZumChecken + tmpArray.get(i).fitness) {
                return tmpArray.get(i);
            } else {
                zahlZumChecken += tmpArray.get(i).fitness;
            }
        }
        return null;
    }
}
