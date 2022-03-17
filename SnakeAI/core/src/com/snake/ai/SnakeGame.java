package com.snake.ai;

import static com.snake.ai.Settings.POPULATIONSIZE;
import static com.snake.ai.Settings.bestSnakesArraySize;
import static com.snake.ai.Settings.maxEnergy;
import static com.snake.ai.Settings.reihen;
import static com.snake.ai.Settings.spalten;
import static com.snake.ai.Settings.startLength;
import static com.snake.ai.main.averageSteps;
import static com.snake.ai.main.bestSnakes;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.evo;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gameNr;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.populationsSinceLastSave;
import static com.snake.ai.main.r;
import static com.snake.ai.main.replay;
import static com.snake.ai.main.requestReplayStop;
import static com.snake.ai.main.snakeArray;
import static com.snake.ai.main.snakeGameInstance;

import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class SnakeGame implements Runnable {
    enum Dir {
        up(0, -1), right(1, 0), down(0, 1), left(-1, 0);

        Dir(int x, int y) {
            this.x = x;
            this.y = y;
        }

        final int x, y;
    }

    static final int WALL = -1;
    static volatile boolean gameOver = true;
    int[][] grid;
    static Array<Point> snake;
    static Array<Point> treats;

    Thread gameThread;
    boolean pauseThread = true;
    static short score;
    static Dir dir, startDir;
    static int energy;
    static int timeAlive;
    public FitnessComparator fitnessComparator;
    public static int snakeNr;
    public static int steps;
    long startTime;
    static long timePerPop;
    main main2;

    static int sleepTime = 85;

    public SnakeGame(main main2) {
        initGrid();
        fitnessComparator = new FitnessComparator();
        this.main2 = main2;
        gameThread = new Thread(this);
        gameThread.setPriority(Thread.MAX_PRIORITY);
        gameThread.start();
        gameThread.setName("SnakeAiCalculating");
        treats = new Array<>();
    }

    void startNewGame() {
        gameOver = false;
        stop();
        treats.clear();
        addTreat();

        snakeGameInstance.directionTmpArray.clear();

        energy = maxEnergy;

        if (score > snakeGameInstance.hiScore) {
            snakeGameInstance.hiScore = score;
        }
        score = 0;
        timeAlive = 0;
        steps = 0;
        if (!replay && snakeNr == POPULATIONSIZE) {
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

        pauseThread = true;
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
                NodeVis.highest = 0;
                for (int x = 0; x < startLength; x++) {
                    Point point = new Point(reihen / 2, spalten / 2 + x);
                    snake.add(point);
                }
                break;
            case down:
                NodeVis.highest = 1;
                for (int x = 0; x < startLength; x++) {
                    Point point = new Point(reihen / 2, spalten / 2 - x);
                    snake.add(point);
                }
                break;
            case left:
                NodeVis.highest = 2;
                for (int x = 0; x < startLength; x++) {
                    Point point = new Point(reihen / 2 + x, spalten / 2);
                    snake.add(point);
                }
                break;
            case right:
                NodeVis.highest = 3;
                for (int x = 0; x < startLength; x++) {
                    Point point = new Point(reihen / 2 - x, spalten / 2);
                    snake.add(point);
                }
                break;
        }
        startDir = dir;
        main.snakeHeadX = snake.get(0).x;
        main.snakeHeadY = snake.get(0).y;
    }

    public void newPopulation() {
        timePerPop = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        //mutationProbability = 100f / (averageSteps / (float) POPULATIONSIZE);
        averageSteps = 0;

        loadFromSavedSnake = false;
        snakeGameInstance.population++;
        //Add Data from current Population to the Graph
        int maxFitness = 0;
        for (int i = 0; i < snakeArray.size; i++) {
            maxFitness += snakeArray.get(i).fitness;
        }
        snakeGameInstance.averageFitnessArray.add(maxFitness / POPULATIONSIZE);

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
            tmpArray.removeRange(bestSnakesArraySize, tmpArray.size - 1);
        bestSnakes = tmpArray;
        for (int i = 0; i < bestSnakes.size; i++) {
            bestSnakes.get(i).parent1Snake = null;
            bestSnakes.get(i).parent2Snake = null;
        }
        snakeArray.clear();


        //Autosave
        if (gameNr != 0) {
            if (populationsSinceLastSave == 499) {
                /*TODO SavedSnakes savedSnakes = new SavedSnakes(main2);
                savedSnakes.saveCurrentSnake(true);
                if (gameNr > -1) {
                    savedSnakes.delete(gameNr);
                    gameNr = getInteger("GameMenge");
                } else {
                    gameNr = getInteger("GameMenge") + 1;
                }
                populationsSinceLastSave = 0;*/
            } else
                populationsSinceLastSave++;
        }
        snakeNr = 0;
    }

    void stop() {
        pauseThread = true;
    }

    void initGrid() {
        grid = new int[spalten + 2][reihen + 2];
        for (int r = 0; r < spalten + 2; r++) {
            for (int c = 0; c < reihen + 2; c++) {
                if (c == 0 || c == reihen + 2 - 1 || r == 0 || r == spalten + 2 - 1)
                    grid[r][c] = WALL;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (sleepTime > 0)
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) {

                }
            if (!freeze && currentSnake != null) {
                if (energyUsed() || hitsWall() || hitsSnake()) {
                    gameOver();
                } else {
                    if (eatsTreat()) {
                        score++;
                        energy = maxEnergy;
                        growSnake();
                    }
                    moveSnake();
                    main.snakeHeadX = snake.get(0).x;
                    main.snakeHeadY = snake.get(0).y;
                    main2.berechneLayer();
                    doAction();
                }



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
        averageSteps += steps;
        currentSnake.fitness = evo.fitnessFunction(steps, score);

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

        // Muss unten sein
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
                x = r.nextInt(reihen - 2) + 1;
                y = r.nextInt(spalten - 2) + 1;
                if (grid[y][x] != 0)
                    continue;
                main.foodPositionX = x;
                main.foodPositionY = y;
                Point p = new Point(x, y);
                if (treats.contains(p, false) || (snake != null && snake.contains(p,false)))
                    continue;
                if (snake != null)
                    for (int i = 0; i < snake.size; i++) {
                        if (snake.get(i).x == x && snake.get(i).y == y)
                            continue here;
                    }
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