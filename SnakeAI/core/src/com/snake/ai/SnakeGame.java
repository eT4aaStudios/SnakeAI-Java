package com.snake.ai;

import static com.snake.ai.main.averageSteps;
import static com.snake.ai.main.bestSnakes;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.evo;
import static com.snake.ai.main.foodPositionX;
import static com.snake.ai.main.foodPositionY;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gameNr;
import static com.snake.ai.main.isThisAndroid;
import static com.snake.ai.main.log;
import static com.snake.ai.main.populationsSinceLastSave;
import static com.snake.ai.main.r;
import static com.snake.ai.main.replay;
import static com.snake.ai.main.requestReplayStop;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.snakeArray;
import static com.snake.ai.main.snakeGameInstance;
import static com.snake.ai.main.snakeHeadX;

import com.badlogic.gdx.utils.Array;
import com.google.gwt.core.client.Scheduler;

import java.util.Comparator;

public class SnakeGame /*implements Runnable*/ {
    enum Dir {
        up(0, -1), right(1, 0), down(0, 1), left(-1, 0);

        Dir(int x, int y) {
            this.x = x;
            this.y = y;
        }

        final int x, y;
    }

    static final int WALL = -1;
    static boolean gameOver = true;
    int[][] grid;
    static Array<Point> snake;
    static Array<Point> treats;

    public static Thread gameThread;
    static short score;
    static Dir dir, startDir;
    static int energy;
    static int timeAlive;
    public FitnessComparator fitnessComparator;
    public static int snakeNr;
    public static int steps;
    long startTime = System.currentTimeMillis();
    static long timePerPop;
    main main2;
    AndroidConnection androidConnection;

    static int sleepTime = 85;

    public SnakeGame(main main2) {
        initGrid();
        fitnessComparator = new FitnessComparator();
        this.main2 = main2;
        treats = new Array<>();


    }

    void startNewGame() {
        gameOver = false;
        treats.clear();
        addTreat();

        snakeGameInstance.directionTmpArray.clear();

        energy = settings.maxEnergy;


        score = 0;
        timeAlive = 0;
        steps = 0;
        if (!replay && snakeNr >= settings.POPULATIONSIZE) {
            newPopulation();
        }

        if (replay) {
            currentSnake = snakeGameInstance.bestSnake;
        } else {
            currentSnake = new Snake();
            snakeNr++;
        }

        snake = new Array<>();

        direction();

        //NeuralNetworkVisualization.animationCounter = 0;
        //NeuralNetworkVisualization.offset = 0;
        //NeuralNetworkVisualization.time = 0;
    }

    public void direction() {
        if (replay) {
            dir = snakeGameInstance.startDir;
        } else {
            int i = r.nextInt(4);
            switch (i) {
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
                NeuralNetworkVisualization.highest = 0;
                for (int x = 0; x < settings.startLength; x++) {
                    Point point = new Point(settings.reihen / 2, settings.spalten / 2 + x);
                    snake.add(point);
                }
                break;
            case down:
                NeuralNetworkVisualization.highest = 1;
                for (int x = 0; x < settings.startLength; x++) {
                    Point point = new Point(settings.reihen / 2, settings.spalten / 2 - x);
                    snake.add(point);
                }
                break;
            case left:
                NeuralNetworkVisualization.highest = 2;
                for (int x = 0; x < settings.startLength; x++) {
                    Point point = new Point(settings.reihen / 2 + x, settings.spalten / 2);
                    snake.add(point);
                }
                break;
            case right:
                NeuralNetworkVisualization.highest = 3;
                for (int x = 0; x < settings.startLength; x++) {
                    Point point = new Point(settings.reihen / 2 - x, settings.spalten / 2);
                    snake.add(point);
                }
                break;
        }
        startDir = dir;
        snakeHeadX = snake.get(0).x;
        main.snakeHeadY = snake.get(0).y;
    }

    public void newPopulation() {
        timePerPop = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        //settings.mutationProbability = 100f / (averageSteps / (float) settings.POPULATIONSIZE);
        averageSteps = 0;

        snakeGameInstance.population++;
        if (isThisAndroid())
            androidConnection.updateNotification(snakeGameInstance.population);
        //Add Data from current Population to the Graph
        int maxFitness = 0;
        for (int i = 0; i < snakeArray.size; i++) {
            maxFitness += snakeArray.get(i).fitness;
        }
        snakeGameInstance.averageFitnessArray.add(maxFitness / settings.POPULATIONSIZE);

        int maxHiscore = 0;
        for (int i = 0; i < snakeArray.size; i++) {
            if (maxHiscore <= snakeArray.get(i).score) {
                maxHiscore = snakeArray.get(i).score;
            }
        }
        snakeGameInstance.hiscoreArray.add(maxHiscore);

        //Add new Best Snakes and delete old bad Snakes from 500 Best Snakes
        Array<Snake> tmpArray = new Array<>(bestSnakes);
        tmpArray.addAll(snakeArray);
        tmpArray.sort(fitnessComparator);
        if (snakeGameInstance.population > 1)
            tmpArray.removeRange(bestSnakes.size, tmpArray.size - 1);
        else
            try {
                tmpArray.removeRange(settings.bestSnakesArraySize, tmpArray.size - 1);
            } catch (Exception ignored) {

            }
        bestSnakes = tmpArray;
        snakeArray.clear();


        //Autosave
        if (gameNr != 0) {
            if (populationsSinceLastSave >= settings.autoSaveAt - 1) {
                //saveAsJson(snakeGameInstance, bestSnakes);
                populationsSinceLastSave = 0;
            } else
                populationsSinceLastSave++;
        }
        snakeNr = 0;
    }

    void initGrid() {
        grid = new int[settings.spalten + 2][settings.reihen + 2];
        for (int r = 0; r < settings.spalten + 2; r++) {
            for (int c = 0; c < settings.reihen + 2; c++) {
                if (c == 0 || c == settings.reihen + 2 - 1 || r == 0 || r == settings.spalten + 2 - 1)
                    grid[r][c] = WALL;
            }
        }
    }

    public void run() {
        Scheduler.RepeatingCommand cmd = new Scheduler.RepeatingCommand() {
            final float sleepTimeFromScheduler = sleepTime;

            public boolean execute() {
                //if (!Thread.currentThread().isInterrupted()) {

                logic();
                return sleepTime == sleepTimeFromScheduler;
            }
        };

        Scheduler.get().scheduleFixedPeriod(cmd, sleepTime);
    }

    public void logic() {
        if (!freeze) {
            if (energyUsed() || hitsWall() || hitsSnake()) {
                gameOver();
            } else {
                if (eatsTreat()) {
                    score++;
                    energy = settings.maxEnergy;
                    growSnake();
                }
                moveSnake();
                snakeHeadX = snake.get(0).x;
                main.snakeHeadY = snake.get(0).y;
                if (score == (settings.reihen * settings.spalten - settings.startLength)) {
                    freeze = true;
                    log("Done! Max Score reached!");
                    gameOver();
                    //saveAsJson(snakeGameInstance, bestSnakes);
                    return;
                }
                main2.berechneLayer();
                doAction();
            }
        }
    }

    private void doAction() {
        double highest = 0;
        int id = 0;
        for (int i = 0; i < currentSnake.layerArray.get(currentSnake.layerArray.size - 1).nodeArray.size; i++) {
            if (currentSnake.layerArray.get(currentSnake.layerArray.size - 1).nodeArray.get(i).value > highest) {
                highest = currentSnake.layerArray.get(currentSnake.layerArray.size - 1).nodeArray.get(i).value;
                id = i;
            }
        }

        switch (id) {
            case 0:
                if (dir != Dir.down) {
                    dir = Dir.up;
                    NeuralNetworkVisualization.highest = 0;
                } else
                    NeuralNetworkVisualization.highest = 1;
                break;
            case 1:
                if (dir != Dir.up) {
                    dir = Dir.down;
                    NeuralNetworkVisualization.highest = 1;
                } else
                    NeuralNetworkVisualization.highest = 0;
                break;
            case 2:
                if (dir != Dir.right) {
                    dir = Dir.left;
                    NeuralNetworkVisualization.highest = 2;
                } else
                    NeuralNetworkVisualization.highest = 3;
                break;
            case 3:
                if (dir != Dir.left) {
                    dir = Dir.right;
                    NeuralNetworkVisualization.highest = 3;
                } else
                    NeuralNetworkVisualization.highest = 2;
                break;
        }
        if (!replay)
            snakeGameInstance.directionTmpArray.add(dir);
        else if (!gameOver && !requestReplayStop)
            dir = snakeGameInstance.directionArray.get(steps);
        steps++;
    }

    boolean energyUsed() {
        energy -= 1;
        timeAlive++;
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
        for (int i = 0; i < snake.size; i++) {
            Point p = snake.get(i);
            if (p.x == nextCol && p.y == nextRow)
                return true;
        }
        return false;
    }

    boolean eatsTreat() {
        Point head = snake.get(0);
        int nextCol = head.x + dir.x;
        int nextRow = head.y + dir.y;
        if (treats.get(treats.size - 1).x == nextCol && treats.get(treats.size - 1).y == nextRow) {
            if (score + 1 == (settings.reihen * settings.spalten - settings.startLength))
                return true;
            addTreat();
            return true;
        }
        return false;
    }

    public void gameOver() {
        currentSnake.score = score;
        gameOver = true;
        averageSteps += steps;
        currentSnake.fitness = evo.fitnessFunction(settings.fitnessFunction, steps, score);
        if (score > snakeGameInstance.hiScore) {
            snakeGameInstance.hiScore = score;
        }

        snakeArray.add(currentSnake);

        if (!replay && (currentSnake.fitness >= snakeGameInstance.bestSnake.fitness)) {
            snakeGameInstance.bestSnake.layerArray = new Array<>(currentSnake.layerArray);
            snakeGameInstance.bestSnake.fitness = currentSnake.fitness;
            snakeGameInstance.bestSnake.score = currentSnake.score;
            snakeGameInstance.bestSnakeTreats = new Array<>(treats);
            snakeGameInstance.startDir = startDir;
            snakeGameInstance.directionArray = new Array<>(snakeGameInstance.directionTmpArray);
        }

        if (requestReplayStop) {
            requestReplayStop = false;
            replay = false;
        }
        NeuralNetworkVisualization.snakeVisionFieldArray.clear();

        // Muss unten sein
        if (score != (settings.reihen * settings.spalten - settings.startLength))
            startNewGame();
    }

    void moveSnake() {
        for (int i = snake.size - 1; i > 0; i--) {
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
        Point tail = snake.get(snake.size - 1);
        int x = tail.x + dir.x;
        int y = tail.y + dir.y;
        snake.add(new Point(x, y));
    }

    void addTreat() {
        if (replay) {
            treats.add(snakeGameInstance.bestSnakeTreats.get(treats.size));
        } else {
            int x, y;
            here:
            while (true) {
                x = r.nextInt(settings.reihen) + 1;
                y = r.nextInt(settings.spalten) + 1;
                Point p = new Point(x, y);

                if ((snake != null && snake.contains(p, false)) || (x == foodPositionX && y == foodPositionY)) {
                    continue;
                }

                foodPositionX = x;
                foodPositionY = y;
                treats.add(p);
                break;
            }
        }
    }
}

class FitnessComparator implements Comparator<Snake> {
    @SuppressWarnings("NewApi")
    @Override
    public int compare(Snake snake, Snake t1) {
        return Double.compare(t1.fitness, snake.fitness);
    }
}