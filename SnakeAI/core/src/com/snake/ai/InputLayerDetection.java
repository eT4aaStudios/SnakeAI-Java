package com.snake.ai;

import static com.snake.ai.SnakeGame.dir;
import static com.snake.ai.SnakeGame.energy;
import static com.snake.ai.SnakeGame.maxEnergy;
import static com.snake.ai.SnakeGame.snake;
import static com.snake.ai.main.currentSnake;

public class InputLayerDetection {

    private int x, y;
    private final double reihen, spalten;
    private int SnakeHeadX, SnakeHeadY;
    private int foodpositionX, foodpositionY;
    private final double diagonalLength;

    public InputLayerDetection() {
        reihen = main.reihen - 1;
        spalten = main.spalten - 1;
        diagonalLength = reihen <= spalten ? reihen * 2 : spalten * 2;
    }

    public void start() {
        SnakeHeadX = main.SnakeHeadX - 1;
        SnakeHeadY = main.SnakeHeadY - 1;
        foodpositionX = main.foodpositionX - 1;
        foodpositionY = main.foodpositionY - 1;
        wandDetectionGerade();          //(4)
        wandDetectionSchreag();         //(4)
        schwanzDetectionGerade();       //(4)
        schwanzDetectionSchraeg();      //(4)
        foodDetectionGerade();          //(4)
        foodDetectionSchraeg();         //(4)


        if (currentSnake.layerArray.get(0).NodeArray.size > 24) {
            directionGoingHead();           //(4) 29 Head Direction
            directionGoingTail();           //(4) 33 Tail Direction
            //energy();
        }
    }

    public void wandDetectionGerade() {
        currentSnake.layerArray.get(0).NodeArray.get(0).value = (reihen - SnakeHeadX) / reihen;
        //    System.out.println("Sehr nah an der linken Wand: " + currentSnake.layerArray.get(0).NodeArray.get(12).value);

        currentSnake.layerArray.get(0).NodeArray.get(1).value = (SnakeHeadX) / reihen;
        //    System.out.println("Sehr nah an der rechten Wand: " + currentSnake.layerArray.get(0).NodeArray.get(13).value);

        currentSnake.layerArray.get(0).NodeArray.get(2).value = (spalten - SnakeHeadY) / spalten;
        //    System.out.println("Sehr nah an der Oberen Wand: " + currentSnake.layerArray.get(0).NodeArray.get(14).value);

        currentSnake.layerArray.get(0).NodeArray.get(3).value = (SnakeHeadY) / spalten;
        //    System.out.println("Sehr nah an der Unteren Wand: " + currentSnake.layerArray.get(0).NodeArray.get(15).value);
    }

    public void wandDetectionSchreag() {
        //schräg
        x = SnakeHeadX;
        y = SnakeHeadY;
        boolean positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg links Oben");
            if (positionIsUp)
                x--;
            else
                y--;
            positionIsUp = !positionIsUp;
            if (x == 0 || y == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(4).value = (diagonalLength - i) / diagonalLength;
                break;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg links Unten");
            if (positionIsUp)
                x--;
            else
                y++;
            positionIsUp = !positionIsUp;
            if (x == 0 || y == spalten) {
                currentSnake.layerArray.get(0).NodeArray.get(5).value = (diagonalLength - i) / diagonalLength;
                break;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg rechts Unten");
            if (positionIsUp)
                x++;
            else
                y++;
            positionIsUp = !positionIsUp;
            if (x == reihen || y == spalten) {
                currentSnake.layerArray.get(0).NodeArray.get(6).value = (diagonalLength - i) / diagonalLength;

                break;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        positionIsUp = dir == SnakeGame.Dir.up || dir == SnakeGame.Dir.down;
        for (double i = 1; i < 99999; i++) {
            //System.out.println("Schraeg rechts Oben");
            if (positionIsUp)
                x++;
            else
                y--;
            positionIsUp = !positionIsUp;
            if (x == reihen || y == 0) {
                currentSnake.layerArray.get(0).NodeArray.get(7).value = (diagonalLength - i) / diagonalLength;
                break;
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
        x = SnakeHeadX;
        y = SnakeHeadY;
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
        double nearest1 = 99999;
        double nearest2 = 99999;
        double nearest3 = -99999;
        double nearest4 = -99999;
        for (int i = 0; i < snake.size(); i++) {
            int snakeX = snake.get(i).x - 1;
            int snakeY = snake.get(i).y - 1;
            if (snakeX < SnakeHeadX && SnakeHeadY == snakeY) {
                //System.out.println("Schwanz im Weg (Links)");
                if (nearest1 > (SnakeHeadX - snakeX) / reihen) {
                    currentSnake.layerArray.get(0).NodeArray.get(16).value = (SnakeHeadX - snakeX) / reihen;
                    nearest1 = currentSnake.layerArray.get(0).NodeArray.get(16).value;
                }
            }
            if (snakeX > SnakeHeadX && SnakeHeadY == snakeY) {
                //System.out.println("Schwanz im Weg (Rechts)");
                if (nearest2 > (snakeX - SnakeHeadX) / reihen) {
                    currentSnake.layerArray.get(0).NodeArray.get(17).value = (snakeX - SnakeHeadX) / reihen;
                    nearest2 = currentSnake.layerArray.get(0).NodeArray.get(17).value;
                }
            }
            if (snakeY < SnakeHeadY && SnakeHeadX == snakeX) {
                //System.out.println("Schwanz im Weg (Oben)");
                if (nearest3 < (snakeY - SnakeHeadY) / spalten) {
                    currentSnake.layerArray.get(0).NodeArray.get(18).value = (snakeY - SnakeHeadY) / spalten;
                    nearest3 = currentSnake.layerArray.get(0).NodeArray.get(18).value;
                }
            }
            if (snakeY > SnakeHeadY && SnakeHeadX == snakeX) {
                //System.out.println("Schwanz im Weg (Unten)");
                if (nearest4 < (SnakeHeadY - snakeY) / spalten) {
                    currentSnake.layerArray.get(0).NodeArray.get(19).value = (SnakeHeadY - snakeY) / spalten;
                    nearest4 = currentSnake.layerArray.get(0).NodeArray.get(19).value;
                }
            }
        }
    }

    public void schwanzDetectionSchraeg() {
        //schräg
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x--;
            y--;
            if (snake.get(j).x - 1 == x && snake.get(j).y - 1 == y) {
                //System.out.println("Schwanz im Weg (Oben links)");
                currentSnake.layerArray.get(0).NodeArray.get(20).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x - 1 == x && snake.get(j).y - 1 == y) {
                //System.out.println("Schwanz im Weg (Unten links)");
                currentSnake.layerArray.get(0).NodeArray.get(21).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x - 1 == x && snake.get(j).y - 1 == y) {
                //System.out.println("Schwanz im Weg (unten Rechts)");
                currentSnake.layerArray.get(0).NodeArray.get(22).value = 1;
            }
        }
        x = SnakeHeadX;
        y = SnakeHeadY;
        for (int j = 0; j < snake.size(); j++) {
            x++;
            y--;
            if (snake.get(j).x - 1 == x && snake.get(j).y - 1 == y) {
                //System.out.println("Schwanz im Weg (Oben Rechts)");
                currentSnake.layerArray.get(0).NodeArray.get(23).value = 1;
            }
        }
    }

    public void directionGoingHead() {
        //switch (SnakeGame.dir) {
        //    case up:
        //        currentSnake.layerArray.get(0).NodeArray.get(24).value = 1;
        //        break;
        //    case down:
        //        currentSnake.layerArray.get(0).NodeArray.get(25).value = 1;
        //        break;
        //    case right:
        //        currentSnake.layerArray.get(0).NodeArray.get(26).value = 1;
        //        break;
        //    case left:
        //        currentSnake.layerArray.get(0).NodeArray.get(27).value = 1;
        //        break;
        //
        //}
    }

    public void directionGoingTail() {
        Point p1 = snake.get(snake.size() - 2);
        Point p2 = snake.get(snake.size() - 1);
        x = p1.x - p2.x;
        y = p1.y - p2.y;

        if (x == 0 && y == -1) {
            //Tail going up
            currentSnake.layerArray.get(0).NodeArray.get(28).value = 1;
        }
        if (x == 0 && y == 1) {
            //Tail going down
            currentSnake.layerArray.get(0).NodeArray.get(28).value = 1;
        }
        if (x == 1 && y == 0) {
            //Tail going right
            currentSnake.layerArray.get(0).NodeArray.get(30).value = 1;
        }
        if (x == -1 && y == 0) {
            //Tail going left
            currentSnake.layerArray.get(0).NodeArray.get(31).value = 1;
        }
    }

    public void energy() {
        currentSnake.layerArray.get(0).NodeArray.get(32).value = (maxEnergy - energy) / (float) maxEnergy;
    }
}
