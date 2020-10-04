package com.snake.ai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static com.snake.ai.main.POPULATIONSIZE;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.bestArrays;
import static com.snake.ai.main.bestSnakesArraySize;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.enableOutputLayerLogging;
import static com.snake.ai.main.freeze;
import static java.lang.String.format;

public class Snake extends JPanel implements Runnable {
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
    int score, hiScore;
    static final int startlength = 3;
    static Dir dir;
    static int energy;
    static int timealaive;

    int[][] grid;
    static List<Point> snake;
    List<Point> treats;
    Font smallFont;

    static int nCols = 12;
    static int nRows = 12;
    static int Sleep_Time = 85;
    final int treastmenge = 1;

    public static int population;
    public static int snakeNr;
    public static int steps;
    int Sleep_Time2;
    public static JFrame f;

    public Snake() {
        setPreferredSize(new Dimension(640, 440));
        setBackground(Color.darkGray);
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);

        smallFont = getFont().deriveFont(Font.BOLD, 18);
        initGrid();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameOver) {
                    startNewGame();
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (dir != Dir.down)
                            dir = Dir.up;
                        break;

                    case KeyEvent.VK_LEFT:
                        if (dir != Dir.right)
                            dir = Dir.left;
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (dir != Dir.left)
                            dir = Dir.right;
                        break;

                    case KeyEvent.VK_DOWN:
                        if (dir != Dir.up)
                            dir = Dir.down;
                        break;
                }
                repaint();
            }
        });
    }

    void startNewGame() {
        gameOver = false;

        stop();
        initGrid();
        treats = new LinkedList<>();

        energy = 2000;

        if (score > hiScore) {
            hiScore = score;
        }
        score = 0;
        timealaive = 0;
        steps = 0;
        if (snakeNr == POPULATIONSIZE) {
            population++;
            System.out.println("\n_____________________");
            System.out.println("    NEW POPULATION!   ");
            System.out.println("_____________________\n");
            int maxFitness = 0;
            for (int i = 0; i < allSnakesArrays.get(population - 1).allSnakesArray.size; i++) {
                maxFitness += allSnakesArrays.get(population - 1).allSnakesArray.get(i).fitness;
            }
            System.out.println("average Fitness: " + maxFitness / POPULATIONSIZE);
            bestSnakes best = new bestSnakes();
            bestArrays.add(best);
            allSnakes allSnakes = new allSnakes();
            allSnakesArrays.add(allSnakes);
            for (int i = 0; i < bestArrays.get(population - 1).bestSnakesArray.size; i++)
                System.out.println("Nr.:" + i + " Snake Fitness: " + bestArrays.get(population - 1).bestSnakesArray.get(i).fitness);
            if (isFocused())
                Sleep_Time2 = 2000;
            snakeNr = 0;
        }
        currentSnake = new Snakes();

        snakeNr++;

        snake = new ArrayList<>();

        Random r = new Random();
        switch (r.nextInt(4)) {
            case 0:
                dir = Dir.left;
                for (int x = 0; x < startlength; x++)
                    snake.add(new Point(nCols / 2 + x, nRows / 2));
                break;
            case 1:
                dir = Dir.right;
                for (int x = startlength; x > 0; x--)
                    snake.add(new Point(nCols / 2 + x, nRows / 2));
                break;
            case 2:
                dir = Dir.up;
                for (int x = 0; x < startlength; x++)
                    snake.add(new Point(nCols / 2, nRows / 2 + x));
                break;
            case 3:
                dir = Dir.down;
                for (int x = startlength; x > 0; x--)
                    snake.add(new Point(nCols / 2, nRows / 2 + x));
                break;
        }


        do
            addTreat();
        while (treats.isEmpty());

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
        grid = new int[nRows][nCols];
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                if (c == 0 || c == nCols - 1 || r == 0 || r == nRows - 1)
                    grid[r][c] = WALL;
            }
        }
    }

    @Override
    public void run() {
        while (Thread.currentThread() == gameThread) {
            if (Sleep_Time2 > 0) {
                try {
                    Thread.sleep(Sleep_Time2);
                } catch (InterruptedException e) {
                    return;
                }
                Sleep_Time2 = 0;
            } else {
                try {
                    Thread.sleep(Sleep_Time);
                } catch (InterruptedException e) {
                    return;
                }
            }
            if (!freeze) {

                if (energyUsed() || hitsWall() || hitsSnake()) {
                    gameOver();
                } else {
                    if (eatsTreat()) {
                        score++;
                        energy = 2000;
                        growSnake();
                    }
                    moveSnake();
                    addTreat();

                    main.SnakeHeadX = snake.get(0).x;
                    main.SnakeHeadY = snake.get(0).y;
                }

                main main2 = new main();
                main2.berechneLayer();
                doAction();
                Evolution evo = new Evolution();
                currentSnake.fitness = evo.FitnessFuntction(steps, score);

                repaint();
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
        if (highest >= 1) {
            switch (id) {
                case 0:
                    if (dir != Dir.down && dir != Dir.up) {
                        dir = Dir.up;
                    }
                    break;
                case 1:
                    if (dir != Dir.up && dir != Dir.down) {
                        dir = Dir.down;
                    }
                    break;
                case 2:
                    if (dir != Dir.right && dir != Dir.left) {
                        dir = Dir.left;
                    }
                    break;
                case 3:
                    if (dir != Dir.left && dir != Dir.right) {
                        dir = Dir.right;
                    }
                    break;
            }
        }
        steps++;
    }

    boolean energyUsed() {
        energy -= 10;
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
        for (Point p : treats)
            if (p.x == nextCol && p.y == nextRow) {
                return treats.remove(p);
            }
        return false;
    }

    void gameOver() {
        allSnakesArrays.get(population).allSnakesArray.add(currentSnake);
        currentSnake.score = score;
        gameOver = true;
        stop();
        Evolution startFitness = new Evolution();
        currentSnake.fitness = startFitness.FitnessFuntction(steps, score);

        if (bestArrays.get(population).bestSnakesArray.size < bestSnakesArraySize)
            bestArrays.get(population).bestSnakesArray.add(currentSnake);
        else if (currentSnake.fitness > bestArrays.get(population).bestSnakesArray.get(bestArrays.get(population).bestSnakesArray.size - 1).fitness) {
            bestArrays.get(population).bestSnakesArray.removeIndex(0);
            bestArrays.get(population).bestSnakesArray.add(currentSnake);
        }
        bestArrays.get(population).bestSnakesArray.sort(new FitnessComparator());
        // Muss unten sein
        startNewGame();
        repaint();
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
        if (treats.size() < treastmenge) {

            if (rand.nextInt(10) == 0) { // 1 in 10

                if (rand.nextInt(4) != 0) {  // 3 in 4
                    int x, y;
                    while (true) {

                        x = rand.nextInt(nCols);
                        y = rand.nextInt(nRows);
                        if (grid[y][x] != 0)
                            continue;
                        main.foodpositionX = x;
                        main.foodpositionY = y;

                        Point p = new Point(x, y);
                        if (snake.contains(p) || treats.contains(p))
                            continue;

                        treats.add(p);
                        break;
                    }
                } else if (treats.size() > 1)
                    treats.remove(0);
            }
        }
    }

    void drawGrid(Graphics2D g) {
        g.setColor(Color.lightGray);
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                if (grid[r][c] == WALL)
                    g.fillRect(c * 10, r * 10, 10, 10);
            }
        }
    }

    void drawSnake(Graphics2D g) {
        g.setColor(new Color(0.56f,0.56f,0.56f,0.57f));
        if (snake.size() > 0) {
            for(int i = 0;i < snake.size();i++) {
                Point p = snake.get(i);
                g.fillRect(p.x * 10, p.y * 10, 10, 10);
            }
        }

        g.setColor(energy < 500 ? Color.red : new Color(0,0.7f,0.8f,0.9f));
        if (snake.size() > 0) {
            Point head = snake.get(0);
            g.fillRect(head.x * 10, head.y * 10, 10, 10);
        }
    }

    void drawTreats(Graphics2D g) {
        g.setColor(Color.red);
        if (treats.size() > 0) {
            for (Point p : treats)
                g.fillRect(p.x * 10, p.y * 10, 10, 10);
        }
    }

    void drawStartScreen(Graphics2D g) {
        g.setColor(Color.cyan);
        g.setFont(getFont());
        g.drawString("Snake", 240, 190);
        g.setColor(Color.red);
        g.setFont(smallFont);
        g.drawString("(click to start)", 250, 240);
    }

    void drawScore(Graphics2D g) {
        int h = getHeight();
        g.setFont(smallFont);
        g.setColor(Color.white);
        String s = format("Highscore %d    score %d   ", hiScore, score);
        g.drawString(s, 30, h - 30);

        String s2 = format("Snake Nr. %d    population %d", snakeNr, population);
        g.drawString(s2, 30, h - 410);

        String s3;
        if (bestArrays.get(population).bestSnakesArray.size != 0)
            s3 = format("BestFitness %d   CurrentFitness %d", bestArrays.get(population).bestSnakesArray.get(bestArrays.get(population).bestSnakesArray.size - 1).fitness, currentSnake.fitness);
        else
            s3 = format("CurrentFitness %d", currentSnake.fitness);
        g.drawString(s3, 30, h - 380);

        g.drawString(format("Energy %d", energy), getWidth() - 150, h - 30);
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g);

        if (gameOver) {
            drawStartScreen(g);
        } else {
            drawSnake(g);
            drawTreats(g);
            drawScore(g);
        }
    }

    public static void main2() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setTitle("Snake");
                f.setResizable(true);
                f.add(new Snake(), BorderLayout.CENTER);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                f.setLocation(1700, 500);
            }
        });
    }

    public boolean isFocused() {
        return f.isFocused();
    }
}

class FitnessComparator implements Comparator<Snakes> {
    @Override
    public int compare(Snakes snakes, Snakes t1) {
        return Integer.compare(snakes.fitness, t1.fitness);
    }
}