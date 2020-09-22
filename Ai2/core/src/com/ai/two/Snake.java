package com.ai.two;

import com.badlogic.gdx.utils.Array;

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

import static com.ai.two.main.LayerMenge;
import static com.ai.two.main.POPULATIONSIZE;
import static com.ai.two.main.bestSnakesArray;
import static com.ai.two.main.bestSnakesArraySize;
import static com.ai.two.main.currentSnake;
import static com.ai.two.main.enableOutputLayerLogging;
import static com.ai.two.main.freeze;
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
    static int nRows = 44;
    static int nCols = 64;
    static Dir dir;
    static int energy;
    static int timealaive;

    int[][] grid;
    static List<Point> snake;
    List<Point> treats;
    Font smallFont;


    final int treastmenge = 1;
    static int Sleep_Time = 130;
    static final int startlength = 15;
    public static int population;
    public static int snakeNr;
    public static int batch;
    public static int steps;
    int Sleep_Time2;

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

        dir = Dir.left;
        energy = 1000000;

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
            System.out.println(  "_____________________\n");
            System.out.println("Best Snake Fitness: "+bestSnakesArray.get(bestSnakesArray.size - 1).fitness);
            Sleep_Time2 = 2000;
            snakeNr = 0;
        }
        currentSnake = new Snakes();

        snakeNr++;

        snake = new ArrayList<>();
        for (int x = 0; x < startlength; x++)
            snake.add(new Point(nCols / 2 + x, nRows / 2));

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
            if(Sleep_Time2 > 0) {
                try {
                    Thread.sleep(Sleep_Time2);
                } catch (InterruptedException e) {
                    return;
                }
                Sleep_Time2 = 0;
            }else {
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
                        energy = 100000000;
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
            for (int i = 0; i < currentSnake.layerArray.get(4).NodeArray.size; i++) {
                switch (i) {
                    case 0:
                        System.out.println("(U): " + currentSnake.layerArray.get(4).NodeArray.get(i).value);
                        break;
                    case 1:
                        System.out.println("(D): " + currentSnake.layerArray.get(4).NodeArray.get(i).value);
                        break;
                    case 2:
                        System.out.println("(L): " + currentSnake.layerArray.get(4).NodeArray.get(i).value);
                        break;
                    case 3:
                        System.out.println("(R): " + currentSnake.layerArray.get(4).NodeArray.get(i).value);
                        break;
                }
            }
            System.out.println("\n");
        }

        if (currentSnake.layerArray.get(4).NodeArray.get(0).value >= 0.9d)
            if (dir != Dir.down) {
                dir = Dir.up;
                steps++;
            }
        if (currentSnake.layerArray.get(4).NodeArray.get(1).value >= 0.9d)
            if (dir != Dir.up) {
                dir = Dir.down;
                steps++;
            }
        if (currentSnake.layerArray.get(4).NodeArray.get(2).value >= 0.9d)
            if (dir != Dir.right) {
                dir = Dir.left;
                steps++;
            }
        if (currentSnake.layerArray.get(4).NodeArray.get(3).value >= 0.9d)
            if (dir != Dir.left) {
                dir = Dir.right;
                steps++;
            }

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
        System.out.println("\n_____________________");
        System.out.println(" Game Over new Snake   ");
        System.out.println("_____________________\n");
        currentSnake.score = score;
        gameOver = true;
        stop();
        Evolution startFitness = new Evolution();
        currentSnake.fitness = startFitness.FitnessFuntction(steps, score);

        if (bestSnakesArray.size < bestSnakesArraySize)
            bestSnakesArray.add(currentSnake);
        else if (currentSnake.fitness > bestSnakesArray.first().fitness) {
            bestSnakesArray.removeIndex(0);
            bestSnakesArray.add(currentSnake);
        }
        bestSnakesArray.sort(new FitnessComparator());

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
        g.setColor(Color.green);
        for (Point p : snake)
            g.fillRect(p.x * 10, p.y * 10, 10, 10);

        g.setColor(energy < 500 ? Color.red : Color.magenta);
        Point head = snake.get(0);
        g.fillRect(head.x * 10, head.y * 10, 10, 10);
    }

    void drawTreats(Graphics2D g) {
        g.setColor(Color.red);
        for (Point p : treats)
            g.fillRect(p.x * 10, p.y * 10, 10, 10);
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
        String s = format("hiscore %d    score %d   ", hiScore, score);
        g.drawString(s, 30, h - 30);

        String s2 = format("snakeNr %d    population %d", snakeNr, population);
        g.drawString(s2, 30, h - 410);

        String s3 = format("currentFitness %d   bestFitness %d", (int) currentSnake.fitness, (int) bestSnakesArray.first().fitness);
        g.drawString(s3, 30, h - 380);

        g.drawString(format("energy %d", energy), getWidth() - 150, h - 30);
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
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setTitle("Snake");
                f.setResizable(false);
                f.add(new Snake(), BorderLayout.CENTER);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                f.setLocation(1600, 500);
            }
        });
    }
}

class FitnessComparator implements Comparator<Snakes> {
    @Override
    public int compare(Snakes snakes, Snakes t1) {
        if (snakes.fitness > t1.fitness) {
            return 1; // First bigger
        } else if (snakes.fitness < t1.fitness) {
            return -1; // Second bigger
        } else
            return 0; // They are the same
    }
}