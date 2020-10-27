package com.snake.ai;

import java.awt.Point;

import static com.snake.ai.Snake.snake;
import static com.snake.ai.main.SnakeHeadX;
import static com.snake.ai.main.SnakeHeadY;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.enableInputLayerLogging;
import static com.snake.ai.main.enableSehrNahLogging;
import static com.snake.ai.main.foodpositionX;
import static com.snake.ai.main.foodpositionY;
import static com.snake.ai.main.reihen;
import static com.snake.ai.main.spalten;

public class InputLayerDetection {

    public InputLayerDetection() {
        schwanzDetectionGerade();       //(4) 4  Vision
        schwanzDetectionSchraeg();      //(4) 8  Vision
        foodDetectionGerade();          //(4) 12 Vision
        foodDetectionSchraeg();         //(4) 16 Vision
        wandDetectionGerade();          //(4) 20 Vision
        wandDetectionSchreag();         //(4) 24 Vision

        //Todo buggy
        //directionGoingHead();           //(4) 28 Head Direction
        //directionGoingTail();           //(4) 32 Tail Direction

        if (enableInputLayerLogging) {
            System.out.println("\nInputLayer Values: ");
            for (int i = 0; i < currentSnake.layerArray.get(0).NodeArray.size; i++) {
                System.out.println(currentSnake.layerArray.get(0).NodeArray.get(i).value);
            }
            System.out.println("\n");
        }
    }

    public void wandDetectionGerade() {
        currentSnake.layerArray.get(0).NodeArray.get(0).value = -0.015873d * SnakeHeadX + 1.01587301d;
        if (currentSnake.layerArray.get(0).NodeArray.get(0).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der linken Wand: " + currentSnake.layerArray.get(0).NodeArray.get(12).value);

        currentSnake.layerArray.get(0).NodeArray.get(1).value = 0.01587301d * SnakeHeadX - 0.015873d;
        if (currentSnake.layerArray.get(0).NodeArray.get(1).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der rechten Wand: " + currentSnake.layerArray.get(0).NodeArray.get(13).value);

        currentSnake.layerArray.get(0).NodeArray.get(2).value = -0.0232558d * SnakeHeadY + 1.02325581d;
        if (currentSnake.layerArray.get(0).NodeArray.get(2).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der Oberen Wand: " + currentSnake.layerArray.get(0).NodeArray.get(14).value);

        currentSnake.layerArray.get(0).NodeArray.get(3).value = 0.02325581d * SnakeHeadY - 0.0232558d;
        if (currentSnake.layerArray.get(0).NodeArray.get(3).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der Unteren Wand: " + currentSnake.layerArray.get(0).NodeArray.get(15).value);
    }

    public void wandDetectionSchreag() {
        //schräg
        double x = SnakeHeadX;
        double y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            //System.out.println("Schraeg links Oben");
            x--;
            y--;
            if (x == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(4).value = -0.015873d * SnakeHeadX + 1.01587301d;
                i = reihen;
            }
            if (y == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(4).value = -0.0232558d * SnakeHeadY + 1.02325581d;
                i = reihen;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            //System.out.println("Schraeg links Unten");
            x--;
            y++;
            if (x == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(5).value = -0.015873d * SnakeHeadX + 1.01587301d;
                i = reihen;
            }
            if (y == spalten) {
                currentSnake.layerArray.get(0).NodeArray.get(5).value = 0.02325581d * SnakeHeadY - 0.0232558d;
                i = reihen;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            //System.out.println("Schraeg rechts Unten");
            x++;
            y++;
            if (x == reihen) {
                currentSnake.layerArray.get(0).NodeArray.get(6).value = 0.01587301d * SnakeHeadX - 0.015873d;
                i = reihen;
            }
            if (y == spalten) {
                currentSnake.layerArray.get(0).NodeArray.get(6).value = 0.02325581d * SnakeHeadY - 0.0232558d;
                i = reihen;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            //System.out.println("Schraeg rechts Oben");
            x++;
            y--;
            if (x == reihen) {
                currentSnake.layerArray.get(0).NodeArray.get(7).value = 0.01587301d * SnakeHeadX - 0.015873d;
                i = reihen;
            }
            if (y == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(7).value = -0.0232558d * SnakeHeadY + 1.02325581d;
                i = reihen;
            }
        }
    }

    public void foodDetectionGerade() {
        // Food Nodes
        for (double x = SnakeHeadX; x < reihen; x++) {
            if (x == foodpositionX && SnakeHeadY == foodpositionY) {
                currentSnake.layerArray.get(0).NodeArray.get(8).value = 1d;
                //System.out.println("Food ist auf der rechten Seite");
            }
        }
        for (double x = SnakeHeadX; x > 0; x--) {
            if (x == foodpositionX && SnakeHeadY == foodpositionY) {
                currentSnake.layerArray.get(0).NodeArray.get(9).value = 1d;
                //System.out.println("Food ist auf der linken Seite");
            }
        }
        for (double y = SnakeHeadY; y < spalten; y++) {
            if (y == foodpositionY && SnakeHeadX == foodpositionX) {
                currentSnake.layerArray.get(0).NodeArray.get(10).value = 1d;
                //System.out.println("Food ist auf der Unteren Seite");
            }
        }
        for (double y = SnakeHeadY; y > 0; y--) {
            if (y == foodpositionY && SnakeHeadX == foodpositionX) {
                currentSnake.layerArray.get(0).NodeArray.get(11).value = 1d;
                //System.out.println("Food ist auf der Oberen Seite");
            }
        }
    }

    public void foodDetectionSchraeg() {
        //schräg
        double x = SnakeHeadX;
        double y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x--;
            y--;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg links Oben");
                currentSnake.layerArray.get(0).NodeArray.get(12).value = 1d;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x--;
            y++;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg links Unten");
                currentSnake.layerArray.get(0).NodeArray.get(13).value = 1d;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y++;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg rechts Unten");
                currentSnake.layerArray.get(0).NodeArray.get(14).value = 1d;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y--;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg rechts Oben");
                currentSnake.layerArray.get(0).NodeArray.get(15).value = 1d;
            }
        }
    }

    public void schwanzDetectionGerade() {
        double nearest1 = 99999d;
        double nearest2 = 99999d;
        double nearest3 = -99999d;
        double nearest4 = -99999d;
        for (int i = 0; i < snake.size(); i++) {
            double x = snake.get(i).getX();
            double y = snake.get(i).getY();
            if (x < SnakeHeadX && SnakeHeadY == y) {
                //System.out.println("Schwanz im Weg (Links)");
                if (nearest1 > SnakeHeadX - x) {
                    currentSnake.layerArray.get(0).NodeArray.get(16).value = -0.01666666d * (SnakeHeadX - x) + 1.01666666d;
                    nearest1 = currentSnake.layerArray.get(0).NodeArray.get(16).value;
                }
            }
            if (x > SnakeHeadX && SnakeHeadY == y) {
                //System.out.println("Schwanz im Weg (Rechts)");
                if (nearest2 > x - SnakeHeadX) {
                    currentSnake.layerArray.get(0).NodeArray.get(17).value = -0.01666666d * (x - SnakeHeadX) + 1.01666666d;
                    nearest2 = currentSnake.layerArray.get(0).NodeArray.get(17).value;
                }
            }
            if (y < SnakeHeadY && SnakeHeadX == x) {
                //System.out.println("Schwanz im Weg (Oben)");
                if (nearest3 < y - SnakeHeadY) {
                    currentSnake.layerArray.get(0).NodeArray.get(18).value = 0.025d * (y - SnakeHeadY) + 1.025d;
                    nearest3 = currentSnake.layerArray.get(0).NodeArray.get(18).value;
                }
            }
            if (y > SnakeHeadY && SnakeHeadX == x) {
                //System.out.println("Schwanz im Weg (Unten)");
                if (nearest4 < SnakeHeadY - y) {
                    currentSnake.layerArray.get(0).NodeArray.get(19).value = 0.025d * (SnakeHeadY - y) + 1.025d;
                    nearest4 = currentSnake.layerArray.get(0).NodeArray.get(19).value;
                }
            }
        }
    }

    public void schwanzDetectionSchraeg() {
        //schräg
        double x = SnakeHeadX;
        double y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x--;
            y--;
            if (snake.get(j).getX() == x && snake.get(j).getY() == y) {
                //System.out.println("Schwanz im Weg (Oben links)");
                currentSnake.layerArray.get(0).NodeArray.get(20).value = 1d;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).getX() == x && snake.get(j).getY() == y) {
                //System.out.println("Schwanz im Weg (Unten links)");
                currentSnake.layerArray.get(0).NodeArray.get(21).value = 1d;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).getX() == x && snake.get(j).getY() == y) {
                //System.out.println("Schwanz im Weg (unten Rechts)");
                currentSnake.layerArray.get(0).NodeArray.get(22).value = 1d;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).getX() == x && snake.get(j).getY() == y) {
                //System.out.println("Schwanz im Weg (Oben Rechts)");
                currentSnake.layerArray.get(0).NodeArray.get(23).value = 1d;
            }
        }
    }

    public void directionGoingHead() {
        switch (Snake.dir) {
            case up:
                currentSnake.layerArray.get(0).NodeArray.get(24).value = 1d;
                break;
            case down:
                currentSnake.layerArray.get(0).NodeArray.get(25).value = 1d;
                break;
            case right:
                currentSnake.layerArray.get(0).NodeArray.get(26).value = 1d;
                break;
            case left:
                currentSnake.layerArray.get(0).NodeArray.get(27).value = 1d;
                break;

        }
    }

    public void directionGoingTail() {
        Point p1 = snake.get(snake.size() - 1 - 1);
        Point p2 = snake.get(snake.size() - 1);
        double x = p1.getX() - p2.getX();
        double y = p1.getY() - p2.getY();

        if (x == 0 && y == -1) {
            //Tail going up
            currentSnake.layerArray.get(0).NodeArray.get(28).value = 1d;
        }
        if (x == 0 && y == 1) {
            //Tail going down
            currentSnake.layerArray.get(0).NodeArray.get(29).value = 1d;
        }
        if (x == 1 && y == 0) {
            //Tail going right
            currentSnake.layerArray.get(0).NodeArray.get(30).value = 1d;
        }
        if (x == -1 && y == 0) {
            //Tail going left
            currentSnake.layerArray.get(0).NodeArray.get(31).value = 1d;
        }
    }
}
