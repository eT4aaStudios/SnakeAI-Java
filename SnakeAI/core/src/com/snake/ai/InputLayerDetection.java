package com.snake.ai;

import static com.snake.ai.Settings.maxEnergy;
import static com.snake.ai.Settings.reihen;
import static com.snake.ai.Settings.spalten;
import static com.snake.ai.SnakeGame.dir;
import static com.snake.ai.SnakeGame.energy;
import static com.snake.ai.SnakeGame.snake;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.foodPositionX;
import static com.snake.ai.main.foodPositionY;
import static com.snake.ai.main.snakeHeadX;
import static com.snake.ai.main.snakeHeadY;

import com.badlogic.gdx.utils.Array;

public class InputLayerDetection {

    private int x, y;

    public void start() {
        wandDetectionGerade();
        //wandDetectionSchreag();
        schwanzDetectionGerade();
        //schwanzDetectionSchraeg();
        foodDetectionGerade();
        foodDetectionSchraeg();


        if (currentSnake.layerArray.get(0).nodeArray.size > 24) {
            directionGoingHead();           //(4) 29 Head Direction
            directionGoingTail();           //(4) 33 Tail Direction
            //energy();
        }
    }

    public void wandDetectionGerade() {
        //Linear
        /*
        currentSnake.layerArray.get(0).nodeArray.get(0).value = (reihen - snakeHeadX) / (float) (reihen - 1f);
        //    System.out.println("Sehr nah an der linken Wand: " + currentSnake.layerArray.get(0).nodeArray.get(12).value);

        currentSnake.layerArray.get(0).nodeArray.get(1).value = (snakeHeadX - 1) / (float) (reihen - 1f);
        //    System.out.println("Sehr nah an der rechten Wand: " + currentSnake.layerArray.get(0).nodeArray.get(13).value);

        currentSnake.layerArray.get(0).nodeArray.get(2).value = (spalten - snakeHeadY) / (float) (spalten - 1f);
        //    System.out.println("Sehr nah an der Oberen Wand: " + currentSnake.layerArray.get(0).nodeArray.get(14).value);

        currentSnake.layerArray.get(0).nodeArray.get(3).value = (snakeHeadY - 1) / (float) (spalten - 1f);
        //    System.out.println("Sehr nah an der Unteren Wand: " + currentSnake.layerArray.get(0).nodeArray.get(15).value);*/

        //Proportional
        currentSnake.layerArray.get(0).nodeArray.get(0).value = 1 / (double) (snakeHeadX - 1 + 1);
        currentSnake.layerArray.get(0).nodeArray.get(1).value = 1 / (double) (reihen - snakeHeadX + 1);
        currentSnake.layerArray.get(0).nodeArray.get(2).value = 1 / (double) (snakeHeadY - 1 + 1);
        currentSnake.layerArray.get(0).nodeArray.get(3).value = 1 / (double) (spalten - snakeHeadY + 1);

    }

    public void wandDetectionSchreag() {/*
        //schräg
        x = snakeHeadX;
        y = snakeHeadY;
        boolean positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg links Oben");
            if (positionIsUp)
                x--;
            else
                y--;
            positionIsUp = !positionIsUp;
            if (x == 0 || y == 0) {
                currentSnake.layerArray.get(0).nodeArray.get(4).value = (diagonalLength - i) / diagonalLength;
                break;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg links Unten");
            if (positionIsUp)
                x--;
            else
                y++;
            positionIsUp = !positionIsUp;
            if (x == 0 || y == spalten) {
                currentSnake.layerArray.get(0).nodeArray.get(5).value = (diagonalLength - i) / diagonalLength;
                break;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg rechts Unten");
            if (positionIsUp)
                x++;
            else
                y++;
            positionIsUp = !positionIsUp;
            if (x == reihen || y == spalten) {
                currentSnake.layerArray.get(0).nodeArray.get(6).value = (diagonalLength - i) / diagonalLength;

                break;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg rechts Oben");
            if (positionIsUp)
                x++;
            else
                y--;
            positionIsUp = !positionIsUp;
            if (x == reihen || y == 0) {
                currentSnake.layerArray.get(0).nodeArray.get(7).value = (diagonalLength - i) / diagonalLength;
                break;
            }
        }*/
    }

    public void foodDetectionGerade() {
        // Food Nodes
        for (int x = snakeHeadX; x < reihen; x++) {
            if (x == foodPositionX && snakeHeadY == foodPositionY) {
                currentSnake.layerArray.get(0).nodeArray.get(8).value = 1;
                //System.out.println("Food ist auf der rechten Seite");
            }
        }
        for (int x = snakeHeadX; x > 0; x--) {
            if (x == foodPositionX && snakeHeadY == foodPositionY) {
                currentSnake.layerArray.get(0).nodeArray.get(9).value = 1;
                //System.out.println("Food ist auf der linken Seite");
            }
        }
        for (int y = snakeHeadY; y < spalten; y++) {
            if (y == foodPositionY && snakeHeadX == foodPositionX) {
                currentSnake.layerArray.get(0).nodeArray.get(10).value = 1;
                //System.out.println("Food ist auf der Unteren Seite");
            }
        }
        for (int y = snakeHeadY; y > 0; y--) {
            if (y == foodPositionY && snakeHeadX == foodPositionX) {
                currentSnake.layerArray.get(0).nodeArray.get(11).value = 1;
                //System.out.println("Food ist auf der Oberen Seite");
            }
        }
    }

    public void foodDetectionSchraeg() {
        //schräg
        x = snakeHeadX;
        y = snakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x--;
            y--;
            if (x == foodPositionX && y == foodPositionY) {
                //System.out.println("Schraeg links Oben");
                currentSnake.layerArray.get(0).nodeArray.get(12).value = 1;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x--;
            y++;
            if (x == foodPositionX && y == foodPositionY) {
                //System.out.println("Schraeg links Unten");
                currentSnake.layerArray.get(0).nodeArray.get(13).value = 1;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y++;
            if (x == foodPositionX && y == foodPositionY) {
                //System.out.println("Schraeg rechts Unten");
                currentSnake.layerArray.get(0).nodeArray.get(14).value = 1;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y--;
            if (x == foodPositionX && y == foodPositionY) {
                //System.out.println("Schraeg rechts Oben");
                currentSnake.layerArray.get(0).nodeArray.get(15).value = 1;
            }
        }
    }

    public void schwanzDetectionGerade() {
        //Linear
        /*//Links
        for (int i = snakeHeadX - 1; i > 0; i--) {
            if (dir != SnakeGame.Dir.right && snake.contains(new Point(i, snakeHeadY), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(16).value = ((reihen - 2) - (snakeHeadX - i - 1)) / (reihen - 2d);
                break;
            }
        }
        //Rechts
        for (int i = snakeHeadX + 1; i <= reihen; i++) {
            if (dir != SnakeGame.Dir.left && snake.contains(new Point(i, snakeHeadY), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(17).value = ((reihen - 2d) - (i - snakeHeadX - 1)) / (reihen - 2d);
                break;
            }
        }
        //Oben
        for (int i = snakeHeadY - 1; i > 0; i--) {
            if (dir != SnakeGame.Dir.down && snake.contains(new Point(snakeHeadX, i), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(18).value = ((spalten - 2d) - (snakeHeadY - i - 1)) / (spalten - 2d);
                break;
            }
        }
        //Unten
        for (int i = snakeHeadY + 1; i <= spalten; i++) {
            if (dir != SnakeGame.Dir.up && snake.contains(new Point(snakeHeadX, i), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(19).value = ((spalten - 2d) - (i - snakeHeadY - 1)) / (spalten - 2d);
                break;
            }
        }*/


        //Proportional
        //Links
        for (int i = snakeHeadX - 1; i > 0; i--) {
            if (dir != SnakeGame.Dir.right && snake.contains(new Point(i, snakeHeadY), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(16).value = 1 / ((reihen - 2) - (snakeHeadX - i - 1) + 1d);
                break;
            }
        }
        //Rechts
        for (int i = snakeHeadX + 1; i <= reihen; i++) {
            if (dir != SnakeGame.Dir.left && snake.contains(new Point(i, snakeHeadY), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(17).value = 1 / ((reihen - 2d) - (i - snakeHeadX - 1) + 1d);
                break;
            }
        }
        //Oben
        for (int i = snakeHeadY - 1; i > 0; i--) {
            if (dir != SnakeGame.Dir.down && snake.contains(new Point(snakeHeadX, i), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(18).value = 1 / ((spalten - 2d) - (snakeHeadY - i - 1) + 1d);
                break;
            }
        }
        //Unten
        for (int i = snakeHeadY + 1; i <= spalten; i++) {
            if (dir != SnakeGame.Dir.up && snake.contains(new Point(snakeHeadX, i), false)) {
                currentSnake.layerArray.get(0).nodeArray.get(19).value = 1 / ((spalten - 2d) - (i - snakeHeadY - 1) + 1d);
                break;
            }
        }
    }

    private boolean contains(Array<Point> snake, Point point) {
        for (int i = 0; i < snake.size; i++) {
            if (snake.get(i).equals(point)) {
                return true;
            }
        }
        return false;
    }

    public void schwanzDetectionSchraeg() {
        //schräg
        x = snakeHeadX;
        y = snakeHeadY;
        for (int j = 0; j < snake.size; j++) {
            x--;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Oben links)");
                currentSnake.layerArray.get(0).nodeArray.get(20).value = 1;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        for (int j = 0; j < snake.size; j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Unten links)");
                currentSnake.layerArray.get(0).nodeArray.get(21).value = 1;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        for (int j = 0; j < snake.size; j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (unten Rechts)");
                currentSnake.layerArray.get(0).nodeArray.get(22).value = 1;
            }
        }
        x = snakeHeadX;
        y = snakeHeadY;
        for (int j = 0; j < snake.size; j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Oben Rechts)");
                currentSnake.layerArray.get(0).nodeArray.get(23).value = 1;
            }
        }
    }

    public void directionGoingHead() {
        switch (SnakeGame.dir) {
            case up:
                currentSnake.layerArray.get(0).nodeArray.get(24).value = 1;
                break;
            case down:
                currentSnake.layerArray.get(0).nodeArray.get(25).value = 1;
                break;
            case right:
                currentSnake.layerArray.get(0).nodeArray.get(26).value = 1;
                break;
            case left:
                currentSnake.layerArray.get(0).nodeArray.get(27).value = 1;
                break;

        }
    }

    public void directionGoingTail() {
        Point p1 = snake.get(snake.size - 2);
        Point p2 = snake.get(snake.size - 1);
        x = p1.x - p2.x;
        y = p1.y - p2.y;

        if (x == 0 && y == -1) {
            //Tail going up
            currentSnake.layerArray.get(0).nodeArray.get(28).value = 1;
        }
        if (x == 0 && y == 1) {
            //Tail going down
            currentSnake.layerArray.get(0).nodeArray.get(29).value = 1;
        }
        if (x == 1 && y == 0) {
            //Tail going right
            currentSnake.layerArray.get(0).nodeArray.get(30).value = 1;
        }
        if (x == -1 && y == 0) {
            //Tail going left
            currentSnake.layerArray.get(0).nodeArray.get(31).value = 1;
        }
    }

    public void energy() {
        currentSnake.layerArray.get(0).nodeArray.get(32).value = (maxEnergy - energy) / (double) maxEnergy;
    }
}
