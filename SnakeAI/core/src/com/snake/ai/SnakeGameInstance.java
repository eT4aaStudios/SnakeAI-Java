package com.snake.ai;

import com.badlogic.gdx.utils.Array;
import com.snake.ai.SnakeGame.Dir;

public class SnakeGameInstance {
    public Dir startDir;
    public Array<Point> bestSnakeTreats;
    public Snake bestSnake;
    public Array<Dir> directionArray = new Array<>();
    public Array<Dir> directionTmpArray = new Array<>();
    public Settings settings;
    public int population;
    public Array<Integer> averageFitnessArray = new Array<>();
    public Array<Integer> hiscoreArray = new Array<>();
    public int hiScore;


    public SnakeGameInstance() {
        averageFitnessArray.add(0);
        hiscoreArray.add(0);
    }
}