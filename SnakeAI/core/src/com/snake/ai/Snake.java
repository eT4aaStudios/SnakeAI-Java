package com.snake.ai;

import static com.snake.ai.SavedSnakes.getInteger;
import static com.snake.ai.SavedSnakes.prefs;
import static com.snake.ai.main.FIRSTPOPULATIONSIZE;
import static com.snake.ai.main.POPULATIONSIZE;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.averageFitnessArray;
import static com.snake.ai.main.bestSnakeEver;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.bestSnakesArraySize;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.enableNewPopulationLogging;
import static com.snake.ai.main.enableOutputLayerLogging;
import static com.snake.ai.main.evo;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gameNr;
import static com.snake.ai.main.hiscoreArray;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.mutationPropability;
import static com.snake.ai.main.populationsSinceLastSave;
import static com.snake.ai.main.r;
import static com.snake.ai.main.reihen;
import static com.snake.ai.main.replay;
import static com.snake.ai.main.requestReplayStop;
import static com.snake.ai.main.spalten;

import com.badlogic.gdx.utils.Array;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Snake implements Runnable {
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

    Thread gameThread;
    boolean pauseThread = true;
    static int score;
    static int hiScore;
    static final int startlength = 3;
    static Dir dir, startDir;
    static int energy;
    static int timealaive;
    public FitnessComparator fitnessComparator;

    int[][] grid;
    static List<Point> snake;
    static Array<Point> treats;

    static int Sleep_Time = 85;

    public static int population;
    public static int snakeNr;
    public static int steps;
    long startTime;
    static long timePerPop;
    main main2;

    public Snake(main main2) {
        initGrid();
        fitnessComparator = new FitnessComparator();
        this.main2 = main2;
        (gameThread = new Thread(this)).start();
        gameThread.setName("SnakeAiCalculating");
    }

    void startNewGame() {
        gameOver = false;
        stop();
        treats = new Array<>();
        addTreat();

        bestSnakeEver.directionTmpArray.clear();

        energy = 400;

        if (score > hiScore) {
            hiScore = score;
        }
        score = 0;
        timealaive = 0;
        steps = 0;
        if (!replay && ((snakeNr == POPULATIONSIZE && population > 0) || (snakeNr == FIRSTPOPULATIONSIZE && population == 0))) {
            newPopulation();
        }

        if (replay) {
            currentSnake = bestSnakeEver.bestSnakeEver;
        } else {
            currentSnake = null;
            currentSnake = new Snakes();
            snakeNr++;
        }

        snake = null;
        snake = new ArrayList<>();

        direction();

        pauseThread = true;
    }

    public void direction() {
        if (replay) {
            dir = bestSnakeEver.startDir;
        } else {
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
                for (int x = 0; x < startlength; x++) {
                    Point point = new Point(reihen / 2 + x, spalten / 2);
                    snake.add(point);
                }
                break;
            case down:
                NodeVis.highest = 1;
                for (int x = startlength; x > 0; x--) {
                    Point point = new Point(reihen / 2 + x, spalten / 2);
                    snake.add(point);
                }
                break;
            case left:
                NodeVis.highest = 2;
                for (int x = 0; x < startlength; x++) {
                    Point point = new Point(reihen / 2, spalten / 2 + x);
                    snake.add(point);
                }
                break;
            case right:
                NodeVis.highest = 3;
                for (int x = startlength; x > 0; x--) {
                    Point point = new Point(reihen / 2, spalten / 2 + x);
                    snake.add(point);
                }
                break;
        }
        startDir = dir;
        main.SnakeHeadX = snake.get(0).x;
        main.SnakeHeadY = snake.get(0).y;
    }

    public void newPopulation() {
        timePerPop = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("snakeAiData.txt"), StandardCharsets.UTF_8))) {

            writer.write("Population" + population + "\n");
            writer.write("Highscore" + hiScore + "\n");
            writer.write("Time" + prefs.getString("time") + "\n");
            writer.write("TimePerPop" + timePerPop);
        } catch (Exception ignored) {
        }

        loadFromSavedSnake = false;
        population++;
        if (enableNewPopulationLogging) {
            System.out.println("\n_______________________");
            System.out.println("NEW POPULATION (Nr.: " + population + ")");
            System.out.println("_______________________\n");
        }



        //Umsortierung
        if (allSnakesArrays.size > 1) {
            allSnakesArrays.get(0).allSnakesArray.clear();
            for (int i = 0;i < allSnakesArrays.get(1).allSnakesArray.size;i++) {
                allSnakesArrays.get(0).allSnakesArray.add(allSnakesArrays.get(1).allSnakesArray.get(i));
            }
            allSnakesArrays.get(1).allSnakesArray.clear();
        } else {
            allSnakes allSnakes = new allSnakes();
            allSnakesArrays.add(allSnakes);
        }


        allSnakesArrays.get(0).allSnakesArray.sort(fitnessComparator);

        //Average Fitness
        int maxFitness = 0;
        for (int i = 0; i < allSnakesArrays.get(0).allSnakesArray.size; i++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(i).fitness;
        }
        if (population > 1) {
            if (enableNewPopulationLogging)
                System.out.println("average Fitness: " + maxFitness / POPULATIONSIZE);
            averageFitnessArray.add(maxFitness / POPULATIONSIZE);
        } else {
            if (enableNewPopulationLogging)

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
        mutationPropability = Math.pow(0.996,hiscoreArray.get(population) - 400);

        //Best Snakes System.out
        bestSnakesArray.clear();
        for (int i = 0; i < bestSnakesArraySize; i++) {
            bestSnakesArray.add(allSnakesArrays.get(0).allSnakesArray.get(i));
            if (enableNewPopulationLogging)
                System.out.println("Nr.:" + i + " SnakeGame Fitness: " + bestSnakesArray.get(i).fitness + " Score: " + bestSnakesArray.get(i).score);
        }
        snakeNr = 0;
        if (gameNr != 0) {
            if (populationsSinceLastSave % 100 == 0)
                System.gc();
            if (populationsSinceLastSave == 499) {
                SavedSnakes savedSnakes = new SavedSnakes(main2);
                savedSnakes.saveCurrentSnake(true);
                if (gameNr > -1) {
                    savedSnakes.delete(gameNr);
                    gameNr = getInteger("GameMenge");
                } else {
                    gameNr = getInteger("GameMenge") + 1;
                }
                populationsSinceLastSave = 0;
            } else
                populationsSinceLastSave++;
        }
    }

    void stop() {
        pauseThread = true;
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
        while (true) {
            if (Sleep_Time > 0)
                try {
                    Thread.sleep(Sleep_Time);
                } catch (InterruptedException ignored) {

                }
            if (!freeze && currentSnake != null) {
                if (energyUsed() || hitsWall() || hitsSnake()) {
                    gameOver();
                } else {
                    if (eatsTreat()) {
                        score++;
                        energy = 400;
                        growSnake();
                    }
                    moveSnake();

                    main.SnakeHeadX = snake.get(0).x;
                    main.SnakeHeadY = snake.get(0).y;
                }

                main2.berechneLayer();
                doAction();
                //currentSnake.fitness = evo.FitnessFuntction(steps, score);
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
        currentSnake.fitness = evo.FitnessFuntction2(steps, score);

        allSnakesArrays.get(allSnakesArrays.size - 1).allSnakesArray.add(currentSnake);

        if (!replay && (currentSnake.fitness >= bestSnakeEver.bestSnakeEver.fitness)) {

            bestSnakeEver.bestSnakeEver.layerArray = new Array<>(currentSnake.layerArray);
            bestSnakeEver.bestSnakeEver.fitness = currentSnake.fitness;
            bestSnakeEver.bestSnakeEver.score = currentSnake.score;
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
            here:
            while (true) {
                x = r.nextInt(reihen - 2) + 1;
                y = r.nextInt(spalten - 2) + 1;
                if (grid[y][x] != 0)
                    continue;
                main.foodpositionX = x;
                main.foodpositionY = y;
                Point p = new Point(x, y);
                if (treats.contains(p, false))
                    continue;
                if (snake != null)
                    for (int i = 0; i < snake.size(); i++) {
                        if (snake.get(i).x == x && snake.get(i).y == y)
                            continue here;
                    }
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
        return Double.compare(t1.fitness, snakes.fitness);
    }
}