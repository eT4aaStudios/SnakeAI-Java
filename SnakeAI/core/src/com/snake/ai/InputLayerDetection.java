package com.snake.ai;

import static com.snake.ai.Snake.snake;
import static com.snake.ai.Snake.startlength;
import static com.snake.ai.Snake.treats;
import static com.snake.ai.main.SnakeHeadX;
import static com.snake.ai.main.SnakeHeadY;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.enableInputLayerLogging;
import static com.snake.ai.main.enableSehrNahLogging;
import static com.snake.ai.main.foodpositionX;
import static com.snake.ai.main.foodpositionY;
import static com.snake.ai.main.reihen;
import static com.snake.ai.main.spalten;
import static com.snake.ai.main.visionFieldSize;

public class InputLayerDetection {

    public InputLayerDetection() {
        if (enableInputLayerLogging) {
            System.out.println("\nInputLayer Values: ");
            for (int i = 0; i < currentSnake.layerArray.get(0).NodeArray.size; i++) {
                currentSnake.layerArray.get(0).NodeArray.get(i).value = 0;
            }
            System.out.println("\n");
        }

        schwanzDetectionGerade();       //(4) 4  Vision
        schwanzDetectionSchraeg();      //(4) 8  Vision
        foodDetectionGerade();          //(4) 12 Vision
        foodDetectionSchraeg();         //(4) 16 Vision
        wandDetectionGerade();          //(4) 20 Vision
        wandDetectionSchreag();         //(4) 24 Vision
        length();         //(1) 25 Vision

        if (currentSnake.layerArray.get(0).NodeArray.size > 25) {
            directionGoingHead();           //(4) 29 Head Direction
            directionGoingTail();           //(4) 33 Tail Direction
        }

        if (visionFieldSize > 1)
            visionField();

        if (enableInputLayerLogging) {
            System.out.println("\nInputLayer Values: ");
            for (int i = 0; i < currentSnake.layerArray.get(0).NodeArray.size; i++) {
                System.out.println(currentSnake.layerArray.get(0).NodeArray.get(i).value);
            }
            System.out.println("\n");
        }
    }

    public void wandDetectionGerade() {
        currentSnake.layerArray.get(0).NodeArray.get(0).value = -0.015873f * SnakeHeadX + 1.01587301f;
        if (currentSnake.layerArray.get(0).NodeArray.get(0).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der linken Wand: " + currentSnake.layerArray.get(0).NodeArray.get(12).value);

        currentSnake.layerArray.get(0).NodeArray.get(1).value = 0.01587301f * SnakeHeadX - 0.015873f;
        if (currentSnake.layerArray.get(0).NodeArray.get(1).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der rechten Wand: " + currentSnake.layerArray.get(0).NodeArray.get(13).value);

        currentSnake.layerArray.get(0).NodeArray.get(2).value = -0.0232558f * SnakeHeadY + 1.02325581f;
        if (currentSnake.layerArray.get(0).NodeArray.get(2).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der Oberen Wand: " + currentSnake.layerArray.get(0).NodeArray.get(14).value);

        currentSnake.layerArray.get(0).NodeArray.get(3).value = 0.02325581f * SnakeHeadY - 0.0232558f;
        if (currentSnake.layerArray.get(0).NodeArray.get(3).value > 0.8d && enableSehrNahLogging)
            System.out.println("Sehr nah an der Unteren Wand: " + currentSnake.layerArray.get(0).NodeArray.get(15).value);
    }

    public void wandDetectionSchreag() {
        //schräg
        int x = SnakeHeadX;
        int y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            //System.out.println("Schraeg links Oben");
            x--;
            y--;
            if (x == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(4).value = -0.015873f * SnakeHeadX + 1.01587301f;
                i = reihen;
            }
            if (y == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(4).value = -0.0232558f * SnakeHeadY + 1.02325581f;
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
                currentSnake.layerArray.get(0).NodeArray.get(5).value = -0.015873f * SnakeHeadX + 1.01587301f;
                i = reihen;
            }
            if (y == spalten) {
                currentSnake.layerArray.get(0).NodeArray.get(5).value = 0.02325581f * SnakeHeadY - 0.0232558f;
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
                currentSnake.layerArray.get(0).NodeArray.get(6).value = 0.01587301f * SnakeHeadX - 0.015873f;
                i = reihen;
            }
            if (y == spalten) {
                currentSnake.layerArray.get(0).NodeArray.get(6).value = 0.02325581f * SnakeHeadY - 0.0232558f;
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
                currentSnake.layerArray.get(0).NodeArray.get(7).value = 0.01587301f * SnakeHeadX - 0.015873f;
                i = reihen;
            }
            if (y == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(7).value = -0.0232558f * SnakeHeadY + 1.02325581f;
                i = reihen;
            }
        }
    }

    public void foodDetectionGerade() {
        // Food Nodes
        for (int x = SnakeHeadX; x < reihen; x++) {
            if (x == foodpositionX && SnakeHeadY == foodpositionY) {
                currentSnake.layerArray.get(0).NodeArray.get(8).value = 1;
                //System.out.println("Food ist auf der rechten Seite");
            }
        }
        for (int x = SnakeHeadX; x > 0; x--) {
            if (x == foodpositionX && SnakeHeadY == foodpositionY) {
                currentSnake.layerArray.get(0).NodeArray.get(9).value = 1;
                //System.out.println("Food ist auf der linken Seite");
            }
        }
        for (int y = SnakeHeadY; y < spalten; y++) {
            if (y == foodpositionY && SnakeHeadX == foodpositionX) {
                currentSnake.layerArray.get(0).NodeArray.get(10).value = 1;
                //System.out.println("Food ist auf der Unteren Seite");
            }
        }
        for (int y = SnakeHeadY; y > 0; y--) {
            if (y == foodpositionY && SnakeHeadX == foodpositionX) {
                currentSnake.layerArray.get(0).NodeArray.get(11).value = 1;
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
                currentSnake.layerArray.get(0).NodeArray.get(12).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x--;
            y++;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg links Unten");
                currentSnake.layerArray.get(0).NodeArray.get(13).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y++;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg rechts Unten");
                currentSnake.layerArray.get(0).NodeArray.get(14).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int i = 0; i < reihen; i++) {
            x++;
            y--;
            if (x == foodpositionX && y == foodpositionY) {
                //System.out.println("Schraeg rechts Oben");
                currentSnake.layerArray.get(0).NodeArray.get(15).value = 1;
            }
        }
    }

    public void schwanzDetectionGerade() {
        float nearest1 = 99999;
        float nearest2 = 99999;
        float nearest3 = -99999;
        float nearest4 = -99999;
        for (int i = 0; i < snake.size(); i++) {
            if (snake.get(i).x < SnakeHeadX && SnakeHeadY == snake.get(i).y) {
                //System.out.println("Schwanz im Weg (Links)");
                if (nearest1 > SnakeHeadX - snake.get(i).x) {
                    currentSnake.layerArray.get(0).NodeArray.get(16).value = -0.01666666f * (SnakeHeadX - snake.get(i).x) + 1.01666666f;
                    nearest1 = currentSnake.layerArray.get(0).NodeArray.get(16).value;
                }
            }
            if (snake.get(i).x > SnakeHeadX && SnakeHeadY == snake.get(i).y) {
                //System.out.println("Schwanz im Weg (Rechts)");
                if (nearest2 > snake.get(i).x - SnakeHeadX) {
                    currentSnake.layerArray.get(0).NodeArray.get(17).value = -0.01666666f * (snake.get(i).x - SnakeHeadX) + 1.01666666f;
                    nearest2 = currentSnake.layerArray.get(0).NodeArray.get(17).value;
                }
            }
            if (snake.get(i).y < SnakeHeadY && SnakeHeadX == snake.get(i).x) {
                //System.out.println("Schwanz im Weg (Oben)");
                if (nearest3 < snake.get(i).y - SnakeHeadY) {
                    currentSnake.layerArray.get(0).NodeArray.get(18).value = 0.025f * (snake.get(i).y - SnakeHeadY) + 1.025f;
                    nearest3 = currentSnake.layerArray.get(0).NodeArray.get(18).value;
                }
            }
            if (snake.get(i).y > SnakeHeadY && SnakeHeadX == snake.get(i).x) {
                //System.out.println("Schwanz im Weg (Unten)");
                if (nearest4 < SnakeHeadY - snake.get(i).y) {
                    currentSnake.layerArray.get(0).NodeArray.get(19).value = 0.025f * (SnakeHeadY - snake.get(i).y) + 1.025f;
                    nearest4 = currentSnake.layerArray.get(0).NodeArray.get(19).value;
                }
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
                currentSnake.layerArray.get(0).NodeArray.get(20).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Unten links)");
                currentSnake.layerArray.get(0).NodeArray.get(21).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (unten Rechts)");
                currentSnake.layerArray.get(0).NodeArray.get(22).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x == x && snake.get(j).y == y) {
                //System.out.println("Schwanz im Weg (Oben Rechts)");
                currentSnake.layerArray.get(0).NodeArray.get(23).value = 1;
            }
        }
    }

    public void length() {
        float maxLength = (reihen - 2) * (spalten - 2) - startlength;
        currentSnake.layerArray.get(0).NodeArray.get(24).value = snake.size() / maxLength;
    }

    public void directionGoingHead() {
        switch (Snake.dir) {
            case up:
                currentSnake.layerArray.get(0).NodeArray.get(25).value = 1;
                break;
            case down:
                currentSnake.layerArray.get(0).NodeArray.get(26).value = 1;
                break;
            case right:
                currentSnake.layerArray.get(0).NodeArray.get(27).value = 1;
                break;
            case left:
                currentSnake.layerArray.get(0).NodeArray.get(28).value = 1;
                break;

        }
    }

    public void directionGoingTail() {
        Point p1 = snake.get(snake.size() - 2);
        Point p2 = snake.get(snake.size() - 1);
        int x = p1.x - p2.x;
        int y = p1.y - p2.y;

        if (x == 0 && y == -1) {
            //Tail going up
            currentSnake.layerArray.get(0).NodeArray.get(29).value = 1;
        }
        if (x == 0 && y == 1) {
            //Tail going down
            currentSnake.layerArray.get(0).NodeArray.get(30).value = 1;
        }
        if (x == 1 && y == 0) {
            //Tail going right
            currentSnake.layerArray.get(0).NodeArray.get(31).value = 1;
        }
        if (x == -1 && y == 0) {
            //Tail going left
            currentSnake.layerArray.get(0).NodeArray.get(32).value = 1;
        }
    }

    public void visionField() {
        int i = 0;
        int leftTopCornerX = Snake.snake.get(0).x - (visionFieldSize - 1) / 2;
        int leftTopCornerY = Snake.snake.get(0).y - (visionFieldSize - 1) / 2 + 1;

        for (int x = leftTopCornerX; x < leftTopCornerX + visionFieldSize; x++) {
            for (int y = leftTopCornerY; y < leftTopCornerY + visionFieldSize; y++) {
                if (x > 0 && x < reihen - 1)
                    if (y > 1 && y < spalten) {
                        for (int j = 0; j < snake.size(); j++) {
                            if (snake.get(j).x == x && snake.get(j).y == y) {
                                currentSnake.layerArray.get(0).NodeArray.get(i).value = 1f;
                            }
                        }
                        for (int j = 0; j < treats.size; j++) {
                            if (treats.get(j).x == x && treats.get(j).y == y) {
                                currentSnake.layerArray.get(0).NodeArray.get(i).value = 0.5f;
                            }
                        }
                        i++;
                    }
            }
        }
    }
}
