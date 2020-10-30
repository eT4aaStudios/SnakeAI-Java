package com.snake.ai;

import com.badlogic.gdx.utils.Array;
import com.snake.ai.Snake.Dir;

import java.awt.Point;
import java.util.List;

public class BestSnakeEver {
    public Dir startDir;
    public List<Point> bestSnakeTreats;
    public Snakes bestSnakeEver;
    public Array<Dir> directionArray;
    public Array<Dir> directionTmpArray;

    public BestSnakeEver() {
        directionArray = new Array<>();
        directionTmpArray = new Array<>();
    }
}