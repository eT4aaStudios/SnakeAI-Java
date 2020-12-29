package com.snake.ai;

public class Point {
    int x,y;
    float value;
    int sort;

    public Point(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x,int y,float value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Point(int x,int y,float value,int sort) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.sort = sort;
    }
}
