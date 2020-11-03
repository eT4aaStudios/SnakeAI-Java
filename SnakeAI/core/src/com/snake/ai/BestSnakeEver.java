package com.snake.ai;

import com.badlogic.gdx.utils.Array;
import com.snake.ai.Snake.Dir;

public class BestSnakeEver {
    public Dir startDir;
    public Array<Point> bestSnakeTreats;
    public Snakes bestSnakeEver;
    public Array<Dir> directionArray;
    public Array<Dir> directionTmpArray;

    public BestSnakeEver() {
        directionArray = new Array<>();
        directionTmpArray = new Array<>();
    }
}