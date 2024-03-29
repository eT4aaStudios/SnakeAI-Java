package com.snake.ai;

public class Point {
    int x, y;
    double value;



    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y,double value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public String toString() {
        return x + "x " + y+"y";
    }

    public boolean equals (Object obj) {
        if (this==obj) return true;
        if (this == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        // Class name is Employ & have lastname
        Point otherPoint = (Point) obj ;
        return this.x == otherPoint.x && this.y == otherPoint.y;
    }
}
