package com.snake.ai;

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
        if (enableInputLayerLogging) {
            System.out.println("\nInputLayer Values: ");
            for (int i = 0; i < currentSnake.layerArray.get(0).NodeArray.size; i++) {
                currentSnake.layerArray.get(0).NodeArray.get(i).value = 0;
            }
            System.out.println("\n");
        }

        foodDetectionGerade();
        foodDetectionSchraeg();
        wandDetectionGerade();
        schwanzDetectionGerade();
        //schwanzDetectionSchraeg();

        if (enableInputLayerLogging) {
            System.out.println("\nInputLayer Values: ");
            for (int i = 0; i < currentSnake.layerArray.get(0).NodeArray.size; i++) {
                System.out.println(currentSnake.layerArray.get(0).NodeArray.get(i).value);
            }
            System.out.println("\n");
        }
    }

    public void wandDetectionGerade() {
        currentSnake.layerArray.get(0).NodeArray.get(12).value = -0.015873 * SnakeHeadX + 1.01587301;
        if (currentSnake.layerArray.get(0).NodeArray.get(12).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der linken Wand: " + currentSnake.layerArray.get(0).NodeArray.get(12).value);

        currentSnake.layerArray.get(0).NodeArray.get(13).value = 0.01587301 * SnakeHeadX - 0.015873;
        if (currentSnake.layerArray.get(0).NodeArray.get(13).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der rechten Wand: " + currentSnake.layerArray.get(0).NodeArray.get(13).value);

        currentSnake.layerArray.get(0).NodeArray.get(14).value = -0.0232558 * SnakeHeadY + 1.02325581;
        if (currentSnake.layerArray.get(0).NodeArray.get(14).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der Oberen Wand: " + currentSnake.layerArray.get(0).NodeArray.get(14).value);

        currentSnake.layerArray.get(0).NodeArray.get(15).value = 0.02325581 * SnakeHeadY - 0.0232558;
        if (currentSnake.layerArray.get(0).NodeArray.get(15).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der Unteren Wand: " + currentSnake.layerArray.get(0).NodeArray.get(15).value);
    }

    public void wandDetectionSchreag() {
        //layerArray.get(0).NodeArray.get(4).value = 1d / SnakeHeadX;
        //if (layerArray.get(0).NodeArray.get(4).value > 0.8d && enableSehrNahLogging)
        //    System.out.println("Sehr nah an der linken Wand: " + layerArray.get(0).NodeArray.get(4).value);
//
        //layerArray.get(0).NodeArray.get(5).value = SnakeHeadX / 1d / reihen;
        //if (layerArray.get(0).NodeArray.get(5).value > 0.8d && enableSehrNahLogging)
        //    System.out.println("Sehr nah an der rechten Wand: " + layerArray.get(0).NodeArray.get(5).value);
//
        //layerArray.get(0).NodeArray.get(6).value = 1d / SnakeHeadY;
        //if (layerArray.get(0).NodeArray.get(6).value > 0.8d && enableSehrNahLogging)
        //    System.out.println("Sehr nah an der Oberen Wand: " + layerArray.get(0).NodeArray.get(6).value);
//
        //layerArray.get(0).NodeArray.get(7).value = SnakeHeadY / 1d / spalten;
        //if (layerArray.get(0).NodeArray.get(7).value > 0.8d && enableSehrNahLogging)
        //    System.out.println("Sehr nah an der Unteren Wand: " + layerArray.get(0).NodeArray.get(7).value);
    }

    public void foodDetectionGerade() {
        // Food Nodes
        for (int x = SnakeHeadX; x < reihen; x++) {
            if (x == foodpositionX && SnakeHeadY == foodpositionY) {
                currentSnake.layerArray.get(0).NodeArray.get(0).value = 1;
                //System.out.println("Food ist auf der rechten Seite");
            }
        }
        for (int x = SnakeHeadX; x > 0; x--) {
            if (x == foodpositionX && SnakeHeadY == foodpositionY) {
                currentSnake.layerArray.get(0).NodeArray.get(1).value = 1;
                //System.out.println("Food ist auf der linken Seite");
            }
        }
        for (int y = SnakeHeadY; y < spalten; y++) {
            if (y == foodpositionY && SnakeHeadX == foodpositionX) {
                currentSnake.layerArray.get(0).NodeArray.get(2).value = 1;
                //System.out.println("Food ist auf der Unteren Seite");
            }
        }
        for (int y = SnakeHeadY; y > 0; y--) {
            if (y == foodpositionY && SnakeHeadX == foodpositionX) {
                currentSnake.layerArray.get(0).NodeArray.get(3).value = 1;
                //System.out.println("Food ist auf der Oberen Seite");
            }
        }
    }

    public void foodDetectionSchraeg() {
        //schräg
        int x = SnakeHeadX;
        int y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x--;
            y--;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg links Oben");
                currentSnake.layerArray.get(0).NodeArray.get(4).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x--;
            y++;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg links Unten");
                currentSnake.layerArray.get(0).NodeArray.get(5).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y++;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg rechts Unten");
                currentSnake.layerArray.get(0).NodeArray.get(6).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y--;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg rechts Oben");
                currentSnake.layerArray.get(0).NodeArray.get(7).value = 1;
            }
        }
    }

    public void schwanzDetectionGerade() {
        for (int i = 0; i < snake.size(); i++) {
            if (snake.get(i).x < SnakeHeadX && SnakeHeadY == snake.get(i).y) {
                //System.out.println("Schwanz im Weg (Links)");
                currentSnake.layerArray.get(0).NodeArray.get(8).value = -0.01639934 * (SnakeHeadX - snake.get(i).x) + 1.01639344;
            }
            if (snake.get(i).x > SnakeHeadX && SnakeHeadY == snake.get(i).y) {
                //System.out.println("Schwanz im Weg (Rechts)");
                currentSnake.layerArray.get(0).NodeArray.get(9).value = 0.01587301 * (snake.get(i).x - SnakeHeadX) - 0.015873;
            }
            if (snake.get(i).y < SnakeHeadY && SnakeHeadX == snake.get(i).x) {
                //System.out.println("Schwanz im Weg (Oben)");
                currentSnake.layerArray.get(0).NodeArray.get(10).value = -0.0243902 * (snake.get(i).y - SnakeHeadY) + 1.02439024;
            }
            if (snake.get(i).y > SnakeHeadY && SnakeHeadX == snake.get(i).x) {
                //System.out.println("Schwanz im Weg (Unten)");
                currentSnake.layerArray.get(0).NodeArray.get(11).value = -0.0243902 * (SnakeHeadY - snake.get(i).y) + 1.02439024;
            }
        }
    }

    public void schwanzDetectionSchraeg() {
        //schräg
        int x = SnakeHeadX;
        int y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x--;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Oben links)");
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Unten links)");
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (unten Rechts)");
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Oben Rechts)");
            }
        }
    }
}
