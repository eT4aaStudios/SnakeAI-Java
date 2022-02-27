package com.snake.ai;

import com.badlogic.gdx.utils.Array;
import com.snake.ai.SnakeGame.Dir;

public class BestSnakeEver {
    public Dir startDir;
    public Array<Point> bestSnakeTreats;
    public Snake bestSnakeEver;
    public Array<Dir> directionArray;
    public Array<Dir> directionTmpArray;

    public BestSnakeEver() {
        directionArray = new Array<>();
        directionTmpArray = new Array<>();
    }
}