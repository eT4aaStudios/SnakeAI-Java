package com.snake.ai;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.snake.ai.main.FIRSTPOPULATIONSIZE;
import static com.snake.ai.main.POPULATIONSIZE;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.averageFitnessArray;
import static com.snake.ai.main.bestSnakeEver;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.bestSnakesArraySize;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.enableOutputLayerLogging;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.hiscoreArray;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.reihen;
import static com.snake.ai.main.replay;
import static com.snake.ai.main.requestReplayStop;
import static com.snake.ai.main.spalten;

public class Snake implements Runnable {
    enum Dir {
        up(0, -1), right(1, 0), down(0, 1), left(-1, 0);

        Dir(int x, int y) {
            this.x = x;
            this.y = y;
        }

        final int x, y;
    }

    static final Random rand = new Random();
    static final int WALL = -1;

    static volatile boolean gameOver = true;

    Thread gameThread;
    static int score;
    static int hiScore;
    static final int startlength = 3;
    static Dir dir, startDir;
    static int energy;
    static int timealaive;

    int[][] grid;
    static List<Point> snake;
    static Array<Point> treats;

    static int Sleep_Time = 85;

    public static int population;
    public static int snakeNr;
    public static int steps;
    long startTime;
    static long timePerPop;

    public Snake() {
        initGrid();
    }

    void startNewGame() {
        gameOver = false;

        //TODO Save
        stop();
        initGrid();
        treats = new Array<>();
        addTreat();

        bestSnakeEver.directionTmpArray.clear();

        energy = 150 + score * 2;

        if (score > hiScore) {
            hiScore = score;
        }
        score = 0;
        timealaive = 0;
        steps = 0;
        if (!replay && ((snakeNr == POPULATIONSIZE && population > 0) || (snakeNr == FIRSTPOPULATIONSIZE && population == 0))) {
            loadFromSavedSnake = false;
            population++;
            System.out.println("\n_______________________");
            System.out.println("NEW POPULATION (Nr.: " + population + ")");
            System.out.println("_______________________\n");

            timePerPop = System.currentTimeMillis() - startTime;

            startTime = System.currentTimeMillis();

            //Umsortierung
            allSnakes allSnakes = new allSnakes();
            if (allSnakesArrays.size > 1) {
                allSnakesArrays.set(0, allSnakesArrays.get(1));
                allSnakesArrays.set(1, allSnakes);
            } else
                allSnakesArrays.add(allSnakes);

            allSnakesArrays.get(0).allSnakesArray.sort(new FitnessComparator());

            //Average Fitness
            int maxFitness = 0;
            for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
                maxFitness += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
            }
            if (population > 1) {
                System.out.println("average Fitness: " + maxFitness / POPULATIONSIZE);
                averageFitnessArray.add(maxFitness / POPULATIONSIZE);
            } else {
                System.out.println("average Fitness: " + maxFitness / FIRSTPOPULATIONSIZE);
                averageFitnessArray.add(maxFitness / FIRSTPOPULATIONSIZE);
            }

            int maxHiscore = 0;
            for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
                if (maxHiscore <= allSnakesArrays.get(0).allSnakesArray.get(i).score) {
                    maxHiscore = allSnakesArrays.get(0).allSnakesArray.get(i).score;
                }
            }
            hiscoreArray.add(maxHiscore);


            //Best Snakes System.out
            bestSnakesArray.clear();
            for (int i = 0; i < bestSnakesArraySize; i++) {
                bestSnakesArray.add(allSnakesArrays.get(0).allSnakesArray.get(i));
                System.out.println("Nr.:" + i + " Snake Fitness: " + bestSnakesArray.get(i).fitness + " Score: " + bestSnakesArray.get(i).score);
            }
            snakeNr = 0;
        }

        if (!replay) {
            currentSnake = new Snakes();
            snakeNr++;
        } else
            currentSnake = bestSnakeEver.bestSnakeEver;


        snake = new ArrayList<>();

        if (replay) {
            dir = bestSnakeEver.startDir;
        } else {
            Random r = new Random();
            switch (r.nextInt(4)) {
                case 0:
                    dir = Dir.left;
                    break;
                case 1:
                    dir = Dir.right;
                    break;
                case 2:
                    dir = Dir.up;
                    break;
                case 3:
                    dir = Dir.down;
                    break;
            }
        }
        switch (dir) {
            case up:
                NodeVis.highest = 0;
                for (int x = 0; x < startlength; x++)
                    snake.add(new Point(reihen / 2 + x, spalten / 2));
                break;
            case down:
                NodeVis.highest = 1;
                for (int x = startlength; x > 0; x--)
                    snake.add(new Point(reihen / 2 + x, spalten / 2));
                break;
            case left:
                NodeVis.highest = 2;
                for (int x = 0; x < startlength; x++)
                    snake.add(new Point(reihen / 2, spalten / 2 + x));
                break;
            case right:
                NodeVis.highest = 3;
                for (int x = startlength; x > 0; x--)
                    snake.add(new Point(reihen / 2, spalten / 2 + x));
                break;
        }
        startDir = dir;
        main.SnakeHeadX = snake.get(0).x;
        main.SnakeHeadY = snake.get(0).y;

        (gameThread = new Thread(this)).start();
    }

    void stop() {
        if (gameThread != null) {
            Thread tmp = gameThread;
            gameThread = null;
            tmp.interrupt();
        }
    }

    void initGrid() {
        grid = new int[spalten][reihen];
        for (int r = 0; r < spalten; r++) {
            for (int c = 0; c < reihen; c++) {
                if (c == 0 || c == reihen - 1 || r == 0 || r == spalten - 1)
                    grid[r][c] = WALL;
            }
        }
    }

    @Override
    public void run() {
        while (Thread.currentThread() == gameThread) {
            if (Sleep_Time > 0)
                try {
                    Thread.sleep(Sleep_Time);
                } catch (InterruptedException e) {
                    return;
                }
            if (!freeze) {

                if (energyUsed() || hitsWall() || hitsSnake()) {
                    gameOver();
                } else {
                    if (eatsTreat()) {
                        score++;
                        energy = 150 + score * 2;
                        growSnake();
                    }
                    moveSnake();

                    main.SnakeHeadX = snake.get(0).x;
                    main.SnakeHeadY = snake.get(0).y;
                }

                main main2 = new main();
                main2.berechneLayer();
                doAction();
                Evolution evo = new Evolution();
                currentSnake.fitness = evo.FitnessFuntction(steps, score);
            }
        }
    }

    private void doAction() {
        if (enableOutputLayerLogging) {
            System.out.println("\nOutputLayer Values: ");
            for (int i = 0; i < currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.size; i++) {
                switch (i) {
                    case 0:
                        System.out.println("(U): " + currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value);
                        break;
                    case 1:
                        System.out.println("(D): " + currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value);
                        break;
                    case 2:
                        System.out.println("(L): " + currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value);
                        break;
                    case 3:
                        System.out.println("(R): " + currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value);
                        break;
                }
            }
            System.out.println("\n");
        }

        double highest = 0;
        int id = 0;
        for (int i = 0; i < currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.size; i++) {
            if (currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value > highest) {
                highest = currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value;
                id = i;
            }
        }

        switch (id) {
            case 0:
                if (dir != Dir.down) {
                    dir = Dir.up;
                    NodeVis.highest = 0;
                } else
                    NodeVis.highest = 1;
                break;
            case 1:
                if (dir != Dir.up) {
                    dir = Dir.down;
                    NodeVis.highest = 1;
                } else
                    NodeVis.highest = 0;
                break;
            case 2:
                if (dir != Dir.right) {
                    dir = Dir.left;
                    NodeVis.highest = 2;
                } else
                    NodeVis.highest = 3;
                break;
            case 3:
                if (dir != Dir.left) {
                    dir = Dir.right;
                    NodeVis.highest = 3;
                } else
                    NodeVis.highest = 2;
                break;
        }
        if (!replay)
            bestSnakeEver.directionTmpArray.add(dir);
        else if (!gameOver && !requestReplayStop)
            dir = bestSnakeEver.directionArray.get(steps);
        steps++;
    }

    boolean energyUsed() {
        energy -= 1;
        timealaive++;
        return energy <= 0;
    }

    boolean hitsWall() {
        Point head = snake.get(0);
        int nextCol = head.x + dir.x;
        int nextRow = head.y + dir.y;
        return grid[nextRow][nextCol] == WALL;
    }

    boolean hitsSnake() {
        Point head = snake.get(0);
        int nextCol = head.x + dir.x;
        int nextRow = head.y + dir.y;
        for (Point p : snake)
            if (p.x == nextCol && p.y == nextRow)
                return true;
        return false;
    }

    boolean eatsTreat() {
        Point head = snake.get(0);
        int nextCol = head.x + dir.x;
        int nextRow = head.y + dir.y;
        if (treats.get(treats.size - 1).x == nextCol && treats.get(treats.size - 1).y == nextRow) {
            addTreat();
            return true;
        }
        return false;
    }

    public void gameOver() {
        currentSnake.score = score;
        gameOver = true;
        stop();
        Evolution startFitness = new Evolution();
        currentSnake.fitness = startFitness.FitnessFuntction(steps, score);

        allSnakesArrays.get(allSnakesArrays.size - 1).allSnakesArray.add(currentSnake);

        if (!replay && (currentSnake.fitness >= bestSnakeEver.bestSnakeEver.fitness)) {
            bestSnakeEver.bestSnakeEver = null;
            bestSnakeEver.bestSnakeTreats = null;
            bestSnakeEver.startDir = null;
            bestSnakeEver.directionArray = null;

            bestSnakeEver.bestSnakeEver = currentSnake;
            bestSnakeEver.bestSnakeTreats = new Array<>(treats);
            bestSnakeEver.startDir = startDir;
            bestSnakeEver.directionArray = new Array<>(bestSnakeEver.directionTmpArray);
        }

        if (requestReplayStop) {
            requestReplayStop = false;
            replay = false;
        }

        // Muss unten sein
        startNewGame();
    }

    void moveSnake() {
        for (int i = snake.size() - 1; i > 0; i--) {
            Point p1 = snake.get(i - 1);
            Point p2 = snake.get(i);
            p2.x = p1.x;
            p2.y = p1.y;
        }
        Point head = snake.get(0);

        head.x += dir.x;
        head.y += dir.y;
    }

    void growSnake() {
        Point tail = snake.get(snake.size() - 1);
        int x = tail.x + dir.x;
        int y = tail.y + dir.y;
        snake.add(new Point(x, y));
    }

    void addTreat() {
        if (replay) {
            treats.add(bestSnakeEver.bestSnakeTreats.get(treats.size));
        } else {
            int x, y;
            while (true) {
                x = rand.nextInt(reihen - 2) + 1;
                y = rand.nextInt(spalten - 2) + 1;
                if (grid[y][x] != 0)
                    continue;
                main.foodpositionX = x;
                main.foodpositionY = y;
                Point p = new Point(x, y);
                if (treats.contains(p, false) || (snake != null && snake.contains(p)))
                    continue;
                treats.add(p);
                break;
            }
        }
    }
}

class FitnessComparator implements Comparator<Snakes> {
    @SuppressWarnings("NewApi")
    @Override
    public int compare(Snakes snakes, Snakes t1) {
        return Integer.compare(t1.fitness, snakes.fitness);
    }
}