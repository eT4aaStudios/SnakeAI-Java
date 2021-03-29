package com.snake.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

import static com.badlogic.gdx.Gdx.gl;
import static com.snake.ai.Snake.gameOver;
import static com.snake.ai.Snake.hiScore;
import static com.snake.ai.Snake.population;
import static com.snake.ai.Snake.snakeNr;
import static com.snake.ai.main.POPULATIONSIZE;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.averageFitnessArray;
import static com.snake.ai.main.batch;
import static com.snake.ai.main.bestSnakeEver;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.bestSnakesArraySize;
import static com.snake.ai.main.bias;
import static com.snake.ai.main.biasOutput;
import static com.snake.ai.main.buttonStart;
import static com.snake.ai.main.buttonshownodes;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gameNr;
import static com.snake.ai.main.h;
import static com.snake.ai.main.hiscoreArray;
import static com.snake.ai.main.loadBestSnakeEver;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.mutationMax;
import static com.snake.ai.main.mutationMin;
import static com.snake.ai.main.mutationPropability;
import static com.snake.ai.main.savedSnakesScreenbutton;
import static com.snake.ai.main.shapeRenderer;
import static com.snake.ai.main.w;

public class SavedSnakes implements Screen {
    public static Preferences prefs;

    Stage savedStage;

    private static Table WeckerscrollTable;
    private static Table WeckerselectionContainer;
    public static ScrollPane WeckerscrollPane;
    private static TextButton.TextButtonStyle buttonSelected;
    private static Button loadSavedSnake, delete;
    private float position;
    public static Skin skin2;
    public static TextureAtlas atlas;
    public static BitmapFont pixel10;

    Button saveCurrentSnake;

    Skin skin;
    FileHandle file;
    private static Properties properties;

    public SavedSnakes() {
        prefs = Gdx.app.getPreferences("SnakeAi");
        file = Gdx.files.internal("BestSnake.txt");
        properties = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(file.read());
            properties.loadFromXML(in);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            StreamUtils.closeQuietly(in);
        }
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        savedStage = new Stage();
        Gdx.input.setInputProcessor(savedStage);
        prefs = Gdx.app.getPreferences("SnakeAi");

        saveCurrentSnake = new TextButton("Save Current Snake", skin);
        saveCurrentSnake.setSize(savedSnakesScreenbutton.getWidth(), savedSnakesScreenbutton.getHeight());
        saveCurrentSnake.setPosition(buttonshownodes.getX(), savedSnakesScreenbutton.getY());
        saveCurrentSnake.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (snakeNr != 0)
                    saveCurrentSnake(false);
                else
                    System.out.println("You need to start the Game to Save a new Game");
            }
        });
        savedStage.addActor(saveCurrentSnake);
        savedStage.addActor(buttonStart);
        savedStage.addActor(main.savedSnakesScreenbutton);

        loadSavedSnakeScrollPane();
    }

    public void loadSavedSnakeScrollPane() {
        try {
            float position = WeckerscrollPane.getScrollY();
            WeckerscrollPane.clear();
            WeckerscrollPane.setScrollY(position);
            WeckerscrollPane.updateVisualScroll();
        } catch (Exception ignored) {

        }

        atlas = new TextureAtlas("scrollpane/textures.atlas");
        pixel10 = new BitmapFont(Gdx.files.internal("scrollpane/pixel.fnt"), atlas.findRegion("pixel"), false);
        skin2 = new Skin(atlas);
        skin2.add("default-font", pixel10);
        skin2.load(Gdx.files.internal("scrollpane//ui.json"));

        buttonSelected = new TextButton.TextButtonStyle();
        buttonSelected.up = new TextureRegionDrawable(skin2.getRegion("default-round-down"));

        WeckerscrollTable = new Table();
        WeckerscrollTable.setFillParent(true);
        savedStage.addActor(WeckerscrollTable);

        WeckerselectionContainer = new Table();


        for (int i = 0; i < getInteger("GameMenge") + 1; i++) {
            Group g = new Group();
            if (i == 0)
                loadSavedSnake = new TextButton("Load Game Nr.: " + i +
                        "\nAverage Fitness: " + getFloat("gameNr " + i + "average Fitness") +
                        "\nBest Fitness Ever: " + getFloat("gameNr " + i + "bestSnakeEver.fitness") +
                        "\nHighScore: " + getInteger("gameNr " + i + "hiScore") +
                        "\nPoulation: " + getInteger("gameNr " + i + "population"), skin);
            else
                loadSavedSnake = new TextButton("Load Game Nr.: " + i +
                        "\nAverage Fitness: " + getFloat("gameNr " + i + "average Fitness") +
                        "\nBest Fitness Ever: " + getFloat("gameNr " + i + "bestSnakeEver.fitness") +
                        "\nHighScore: " + getInteger("gameNr " + i + "hiScore") +
                        "\nPoulation: " + getInteger("gameNr " + i + "population"), skin);

            loadSavedSnake.setSize(w / 2 / 2f, h / 5);
            loadSavedSnake.setPosition(w / 2 / -6f, 0);
            final int finalI = i;
            loadSavedSnake.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (snakeNr != 0) {
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        loadBestArraySnake(finalI);
                    } else {
                        if (gameOver) {
                            Snake snake = new Snake();
                            snake.startNewGame();
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                            loadBestArraySnake(finalI);
                        }
                    }
                }
            });
            g.addActor(loadSavedSnake);

            delete = new TextButton("Delete", skin);
            delete.setSize(w / 2 / 6.5f, h / 5);
            delete.setPosition(w / 2 / 2.7f, 0);
            delete.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Deleted Game Nr.: " + finalI);
                    delete(finalI);
                    loadSavedSnakeScrollPane();
                }
            });
            if (finalI > 0)
                g.addActor(delete);

            WeckerselectionContainer.add(g).padBottom(4).size(w / 2 / 3.2f, h / 5f);
            WeckerselectionContainer.row();
        }
        WeckerselectionContainer.pack();
        WeckerselectionContainer.setTransform(false);
        WeckerselectionContainer.setOrigin(WeckerselectionContainer.getWidth() / 1f, WeckerselectionContainer.getHeight() / 40);

        WeckerscrollPane = new ScrollPane(WeckerselectionContainer, skin2);
        WeckerscrollPane.setScrollingDisabled(true, false);
        WeckerscrollPane.layout();
        WeckerscrollTable.add(WeckerscrollPane).size(w / 2, h / 1.3f).fill();
        WeckerscrollTable.setPosition(w / 4 - w / 2, h / 10 - WeckerscrollTable.getHeight() / 1.3f);
        WeckerscrollPane.setScrollY(position);
        WeckerscrollPane.updateVisualScroll();

        Gdx.input.setInputProcessor(savedStage);
    }

    public void saveCurrentSnake(boolean automaticSave) {
        if (gameNr == 0)
            gameNr = getInteger("GameMenge");

        freeze = true;
        prefs.putInteger("GameMenge", getInteger("GameMenge") + 1);

        int gameNr = getInteger("GameMenge");
        saveEinstellungen(gameNr);
        saveBestSnakeArray(gameNr);
        saveBestSnakeEver(gameNr);
        saveGraph(gameNr);

        prefs.flush();
        if (!automaticSave)
            loadSavedSnakeScrollPane();
        freeze = false;
    }

    private void saveGraph(int gameNr) {
        prefs.putInteger("gameNr " + gameNr + "averageFitnessArray", averageFitnessArray.size);
        for (int i = 0; i < averageFitnessArray.size; i++) {
            prefs.putInteger("gameNr " + gameNr + " averageFitnessArray Nr. " + i, averageFitnessArray.get(i));
        }

        prefs.putInteger("gameNr " + gameNr + "hiscoreArray", hiscoreArray.size);
        for (int i = 0; i < hiscoreArray.size; i++) {
            prefs.putInteger("gameNr " + gameNr + " hiscoreArray Nr. " + i, hiscoreArray.get(i));
        }

        prefs.flush();
    }

    private void saveBestSnakeEver(int gameNr) {
        try {
            //Snake
            for (int k = 0; k < bestSnakeEver.bestSnakeEver.layerArray.size - 1; k++) {
                for (int l = 0; l < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.size; l++) {
                    for (int m = 0; m < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.get(l).WeigthArray.size; m++) {
                        prefs.putFloat("gameNr " + gameNr +
                                " bestSnakeEver.bestSnakeEver " +
                                " LayerNr " + k +
                                " NodeNr " + l +
                                " WeightNr " + m, bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.get(l).WeigthArray.get(m));
                    }
                }
            }

            //StartDir
            int dirX = bestSnakeEver.startDir.x;
            int dirY = bestSnakeEver.startDir.y;
            prefs.putInteger("gameNr " + gameNr + "dirX", dirX);
            prefs.putInteger("gameNr " + gameNr + "dirX", dirY);

            //directionArray
            prefs.putInteger("gameNr " + gameNr + "directionArray", bestSnakeEver.directionArray.size);
            for (int i = 0; i < bestSnakeEver.directionArray.size; i++) {
                dirX = bestSnakeEver.directionArray.get(i).x;
                dirY = bestSnakeEver.directionArray.get(i).y;
                prefs.putInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirX", dirX);
                prefs.putInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirY", dirY);
            }

            //Treats
            prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsSize", bestSnakeEver.bestSnakeTreats.size);
            for (int i = 0; i < bestSnakeEver.bestSnakeTreats.size; i++) {
                prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsX " + i, bestSnakeEver.bestSnakeTreats.get(i).x);
                prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsY " + i, bestSnakeEver.bestSnakeTreats.get(i).y);
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        prefs.flush();
    }

    private void saveBestSnakeArray(int gameNr) {
        for (int j = 0; j < bestSnakesArray.size; j++) {
            for (int k = 0; k < bestSnakesArray.get(j).layerArray.size - 1; k++) {
                for (int l = 0; l < bestSnakesArray.get(j).layerArray.get(k).NodeArray.size; l++) {
                    for (int m = 0; m < bestSnakesArray.get(j).layerArray.get(k).NodeArray.get(l).WeigthArray.size; m++) {
                        prefs.putFloat("gameNr " + gameNr +
                                " SnakeNr " + j +
                                " LayerNr " + k +
                                " NodeNr " + l +
                                " WeightNr " + m, bestSnakesArray.get(j).layerArray.get(k).NodeArray.get(l).WeigthArray.get(m));
                    }
                }
            }
        }
        prefs.flush();
    }

    private void saveEinstellungen(int gameNr) {
        int maxFitness = 0;
        for (int m = 0; m < allSnakesArrays.get(0).allSnakesArray.size; m++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(m).fitness;
        }
        prefs.putFloat("gameNr " + gameNr + "average Fitness", maxFitness / POPULATIONSIZE);
        prefs.putInteger("gameNr " + gameNr + "hiScore", hiScore);
        prefs.putInteger("gameNr " + gameNr + "bestSnakeEver.fitness", (int) bestSnakeEver.bestSnakeEver.fitness);
        prefs.putInteger("gameNr " + gameNr + "population", population);

        //Neuronales Netzwerk Eigenschaften
        prefs.putFloat("gameNr " + gameNr + "bias", (float) main.bias);
        prefs.putFloat("gameNr " + gameNr + "biasOutput", (float) main.biasOutput);

        prefs.putFloat("gameNr " + gameNr + "mutationPropability", (float) main.mutationPropability);
        prefs.putFloat("gameNr " + gameNr + "mutationMin", (float) main.mutationMin);
        prefs.putFloat("gameNr " + gameNr + "mutationMax", (float) main.mutationMax);
        prefs.putInteger("gameNr " + gameNr + "bestSnakesArraySize", main.bestSnakesArraySize);

        //Neuronales Netzwerk Aussehen
        prefs.putInteger("gameNr " + gameNr + "inputLayerNodes", main.inputLayerNodes);
        prefs.putInteger("gameNr " + gameNr + "Layer2Nodes", main.Layer2Nodes);
        prefs.putInteger("gameNr " + gameNr + "Layer3Nodes", main.Layer3Nodes);
        prefs.putInteger("gameNr " + gameNr + "Layer4Nodes", main.Layer4Nodes);
        prefs.putInteger("gameNr " + gameNr + "outputLayerNodes", main.outputLayerNodes);
        prefs.putInteger("gameNr " + gameNr + "LayerMenge", main.LayerMenge);

        //Debugging Eigenschaften
        prefs.putBoolean("gameNr " + gameNr + "enableNodeLogging", main.enableNodeLogging);
        prefs.putBoolean("gameNr " + gameNr + "enableSehrNahLogging", main.enableSehrNahLogging);
        prefs.putBoolean("gameNr " + gameNr + "enableOutputLayerLogging", main.enableOutputLayerLogging);
        prefs.putBoolean("gameNr " + gameNr + "enableInputLayerLogging", main.enableInputLayerLogging);

        //Evolutions Eigenschaften
        prefs.putInteger("gameNr " + gameNr + "POPULATIONSIZE", main.POPULATIONSIZE);
        prefs.putInteger("gameNr " + gameNr + "FIRSTPOPULATIONSIZE", main.FIRSTPOPULATIONSIZE);
        prefs.flush();
    }

    public void loadBestArraySnake(int gameNr) {
        System.out.println("Loaded saved Game Nr.:" + gameNr);
        freeze = true;
        main.gameNr = gameNr;

        if (gameNr > 0) {
            loadEinstellungen(gameNr);
            loadBestSnakeEver(gameNr);
            loadBestSnakeArray(gameNr);
            loadGraph(gameNr);
        } else if (gameNr == 0) {
            loadEinstellungen0();
            loadBestSnakeEver0();
            loadBestSnakeArray0();
            loadGraph0();
        }

        snakeNr = 0;
        Snake.gameOver = true;
        freeze = false;
    }

    private void loadGraph(int gameNr) {
        averageFitnessArray.clear();
        for (int i = 0; i < getInteger("gameNr " + gameNr + "averageFitnessArray"); i++) {
            averageFitnessArray.add(getInteger("gameNr " + gameNr + " averageFitnessArray Nr. " + i));
        }

        hiscoreArray.clear();
        for (int i = 0; i < getInteger("gameNr " + gameNr + "hiscoreArray"); i++) {
            hiscoreArray.add(getInteger("gameNr " + gameNr + " hiscoreArray Nr. " + i));
        }

        prefs.flush();
    }

    private void loadEinstellungen(int gameNr) {
        population = getInteger("gameNr " + gameNr + "population");
        hiScore = getInteger("gameNr " + gameNr + "hiScore");

        main.bias = getFloat("gameNr " + gameNr + "bias");
        main.biasOutput = getFloat("gameNr " + gameNr + "biasOutput");

        main.mutationPropability = getFloat("gameNr " + gameNr + "mutationPropability");
        main.mutationMin = getFloat("gameNr " + gameNr + "mutationMin");
        main.mutationMax = getFloat("gameNr " + gameNr + "mutationMax");
        bestSnakesArraySize = getInteger("gameNr " + gameNr + "bestSnakesArraySize");

        //Neuronales Netzwerk Aussehen
        main.inputLayerNodes = getInteger("gameNr " + gameNr + "inputLayerNodes");
        main.Layer2Nodes = getInteger("gameNr " + gameNr + "Layer2Nodes");
        main.Layer3Nodes = getInteger("gameNr " + gameNr + "Layer3Nodes");
        main.Layer4Nodes = getInteger("gameNr " + gameNr + "Layer4Nodes");
        main.outputLayerNodes = getInteger("gameNr " + gameNr + "outputLayerNodes");
        main.LayerMenge = getInteger("gameNr " + gameNr + "LayerMenge");

        //Debugging Eigenschaften
        main.enableNodeLogging = getBoolean("gameNr " + gameNr + "enableNodeLogging");
        main.enableSehrNahLogging = getBoolean("gameNr " + gameNr + "enableSehrNahLogging");
        main.enableOutputLayerLogging = getBoolean("gameNr " + gameNr + "enableOutputLayerLogging");
        main.enableInputLayerLogging = getBoolean("gameNr " + gameNr + "enableInputLayerLogging");

        //Evolutions Eigenschaften
        main.POPULATIONSIZE = getInteger("gameNr " + gameNr + "POPULATIONSIZE");
        main.FIRSTPOPULATIONSIZE = getInteger("gameNr " + gameNr + "FIRSTPOPULATIONSIZE");
    }

    private void loadBestSnakeEver(int gameNr) {
        loadBestSnakeEver = true;
        currentSnake = new Snakes();
        bestSnakeEver.bestSnakeEver = currentSnake;
        bestSnakeEver.bestSnakeEver.fitness = getInteger("gameNr " + gameNr + "bestSnakeEver.fitness");

        int dirX = getInteger("gameNr " + gameNr + "dirX");
        int dirY = getInteger("gameNr " + gameNr + "dirY");
        if ((dirX == 0) && (dirY == -1)) {
            bestSnakeEver.startDir = Snake.Dir.down;
        } else if ((dirX == 0) && (dirY == 1)) {
            bestSnakeEver.startDir = Snake.Dir.up;
        } else if ((dirX == -1) && (dirY == 0)) {
            bestSnakeEver.startDir = Snake.Dir.right;
        } else if ((dirX == 1) && (dirY == 0)) {
            bestSnakeEver.startDir = Snake.Dir.left;
        }

        bestSnakeEver.directionArray.clear();
        for (int i = 0; i < getInteger("gameNr " + gameNr + "directionArray"); i++) {
            dirX = getInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirX");
            dirY = getInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirY");
            if ((dirX == 0) && (dirY == -1)) {
                bestSnakeEver.directionArray.add(Snake.Dir.up);
            } else if ((dirX == 0) && (dirY == 1)) {
                bestSnakeEver.directionArray.add(Snake.Dir.down);
            } else if ((dirX == -1) && (dirY == 0)) {
                bestSnakeEver.directionArray.add(Snake.Dir.left);
            } else if ((dirX == 1) && (dirY == 0)) {
                bestSnakeEver.directionArray.add(Snake.Dir.right);
            }
        }

        if (bestSnakeEver.bestSnakeTreats == null)
            bestSnakeEver.bestSnakeTreats = new Array<>();
        bestSnakeEver.bestSnakeTreats.clear();
        for (int i = 0; i < getInteger("gameNr " + gameNr + "bestSnakeTreatsSize"); i++) {
            int x = getInteger("gameNr " + gameNr + "bestSnakeTreatsX " + i);
            int y = getInteger("gameNr " + gameNr + "bestSnakeTreatsY " + i);
            bestSnakeEver.bestSnakeTreats.add(new Point(x, y));
        }
        loadBestSnakeEver = false;
    }

    private void loadBestSnakeArray(int gameNr) {
        bestSnakesArray.clear();
        loadFromSavedSnake = true;
        allSnakes allSnakes = new allSnakes();
        allSnakesArrays.clear();
        allSnakesArrays.add(allSnakes);
        for (int i = 0; i < bestSnakesArraySize; i++) {
            main.SnakeNr = i;
            currentSnake = new Snakes();
            bestSnakesArray.add(currentSnake);
            allSnakes.allSnakesArray.add(currentSnake);
        }
        loadFromSavedSnake = false;
    }

    public static int getInteger(String key) {
        int defValue = 0;
        if (key.toLowerCase().contains("gameNr 0".toLowerCase()))
            return Integer.parseInt(properties.getProperty(key, Integer.toString(defValue)));
        else
            return prefs.getInteger(key);
    }

    public static float getFloat(String key) {
        int defValue = 0;
        if (key.toLowerCase().contains("gameNr 0".toLowerCase()))
            return Float.parseFloat(properties.getProperty(key, Float.toString(defValue)));
        else
            return prefs.getFloat(key);
    }

    public static boolean getBoolean(String key) {
        if (key.toLowerCase().contains("gameNr 0".toLowerCase()))
            return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(false)));
        else
            return prefs.getBoolean(key);
    }

    private void loadGraph0() {
        averageFitnessArray.clear();
        for (int i = 0; i < getInteger("gameNr " + 0 + "averageFitnessArray"); i++) {
            averageFitnessArray.add(getInteger("gameNr " + 0 + " averageFitnessArray Nr. " + i));
        }

        hiscoreArray.clear();
        for (int i = 0; i < getInteger("gameNr " + 0 + "hiscoreArray"); i++) {
            hiscoreArray.add(getInteger("gameNr " + 0 + " hiscoreArray Nr. " + i));
        }

        prefs.flush();
    }

    private void loadEinstellungen0() {
        population = getInteger("gameNr " + 0 + "population");
        hiScore = getInteger("gameNr " + 0 + "hiScore");

        bias = getFloat("gameNr " + 0 + "bias");
        biasOutput = getFloat("gameNr " + 0 + "biasOutput");

        mutationPropability = getFloat("gameNr " + 0 + "mutationPropability");
        mutationMin = getFloat("gameNr " + 0 + "mutationMin");
        mutationMax = getFloat("gameNr " + 0 + "mutationMax");
        bestSnakesArraySize = getInteger("gameNr " + 0 + "bestSnakesArraySize");

        //Neuronales Netzwerk Aussehen
        main.inputLayerNodes = getInteger("gameNr " + 0 + "inputLayerNodes");
        main.Layer2Nodes = getInteger("gameNr " + 0 + "Layer2Nodes");
        main.Layer3Nodes = getInteger("gameNr " + 0 + "Layer3Nodes");
        main.Layer4Nodes = getInteger("gameNr " + 0 + "Layer4Nodes");
        main.outputLayerNodes = getInteger("gameNr " + 0 + "outputLayerNodes");
        main.LayerMenge = getInteger("gameNr " + 0 + "LayerMenge");

        //Debugging Eigenschaften
        main.enableNodeLogging = getBoolean("gameNr " + 0 + "enableNodeLogging");
        main.enableSehrNahLogging = getBoolean("gameNr " + 0 + "enableSehrNahLogging");
        main.enableOutputLayerLogging = getBoolean("gameNr " + 0 + "enableOutputLayerLogging");
        main.enableInputLayerLogging = getBoolean("gameNr " + 0 + "enableInputLayerLogging");

        //Evolutions Eigenschaften
        main.POPULATIONSIZE = getInteger("gameNr " + 0 + "POPULATIONSIZE");
        main.FIRSTPOPULATIONSIZE = getInteger("gameNr " + 0 + "FIRSTPOPULATIONSIZE");
    }

    private void loadBestSnakeEver0() {
        loadBestSnakeEver = true;
        currentSnake = new Snakes();
        bestSnakeEver.bestSnakeEver = currentSnake;
        bestSnakeEver.bestSnakeEver.fitness = getInteger("gameNr " + 0 + "bestSnakeEver.fitness");

        int dirX = getInteger("gameNr " + 0 + "dirX");
        int dirY = getInteger("gameNr " + 0 + "dirY");
        if ((dirX == 0) && (dirY == -1)) {
            bestSnakeEver.startDir = Snake.Dir.down;
        } else if ((dirX == 0) && (dirY == 1)) {
            bestSnakeEver.startDir = Snake.Dir.up;
        } else if ((dirX == -1) && (dirY == 0)) {
            bestSnakeEver.startDir = Snake.Dir.right;
        } else if ((dirX == 1) && (dirY == 0)) {
            bestSnakeEver.startDir = Snake.Dir.left;
        }

        bestSnakeEver.directionArray.clear();
        for (int i = 0; i < getInteger("gameNr " + 0 + "directionArray"); i++) {
            dirX = getInteger("gameNr " + 0 + " richtung Nr. " + i + "dirX");
            dirY = getInteger("gameNr " + 0 + " richtung Nr. " + i + "dirY");
            if ((dirX == 0) && (dirY == -1)) {
                bestSnakeEver.directionArray.add(Snake.Dir.up);
            } else if ((dirX == 0) && (dirY == 1)) {
                bestSnakeEver.directionArray.add(Snake.Dir.down);
            } else if ((dirX == -1) && (dirY == 0)) {
                bestSnakeEver.directionArray.add(Snake.Dir.left);
            } else if ((dirX == 1) && (dirY == 0)) {
                bestSnakeEver.directionArray.add(Snake.Dir.right);
            }
        }

        if (bestSnakeEver.bestSnakeTreats == null)
            bestSnakeEver.bestSnakeTreats = new Array<>();
        bestSnakeEver.bestSnakeTreats.clear();
        for (int i = 0; i < getInteger("gameNr " + 0 + "bestSnakeTreatsSize"); i++) {
            int x = getInteger("gameNr " + 0 + "bestSnakeTreatsX " + i);
            int y = getInteger("gameNr " + 0 + "bestSnakeTreatsY " + i);
            bestSnakeEver.bestSnakeTreats.add(new Point(x, y));
        }
        loadBestSnakeEver = false;
    }

    private void loadBestSnakeArray0() {
        bestSnakesArray.clear();
        loadFromSavedSnake = true;
        allSnakes allSnakes = new allSnakes();
        allSnakesArrays.clear();
        allSnakesArrays.add(allSnakes);
        for (int i = 0; i < bestSnakesArraySize; i++) {
            main.SnakeNr = i;
            currentSnake = new Snakes();
            bestSnakesArray.add(currentSnake);
            allSnakes.allSnakesArray.add(currentSnake);
        }
        loadFromSavedSnake = false;
    }

    public void delete(int id) {
        prefs.putInteger("GameMenge", getInteger("GameMenge") - 1);

        for (int i = id; i < getInteger("GameMenge") + 1; i++) {
            reWriteBestSnakeEver(i);
            reWriteBestSnakeArray(i);
            reWriteEinstellungen(i);
            reWriteGraph(i);
        }

        removeBestSnakeEver(getInteger("GameMenge") + 1);
        removeBestSnakeArray(getInteger("GameMenge") + 1);
        removeEinstellungen(getInteger("GameMenge") + 1);
        removeGraph(getInteger("GameMenge") + 1);

        prefs.flush();
    }

    private void reWriteGraph(int gameNr) {
        int gameNr2 = gameNr + 1;

        for (int i = 0; i < getInteger("gameNr " + gameNr2 + "averageFitnessArray"); i++) {
            prefs.putInteger("gameNr " + gameNr + " averageFitnessArray Nr. " + i, getInteger("gameNr " + gameNr2 + " averageFitnessArray Nr. " + i));
        }
        prefs.putInteger("gameNr " + gameNr + "averageFitnessArray", getInteger("gameNr " + gameNr2 + "averageFitnessArray"));

        for (int i = 0; i < getInteger("gameNr " + gameNr2 + "hiscoreArray"); i++) {
            prefs.putInteger("gameNr " + gameNr + " hiscoreArray Nr. " + i, getInteger("gameNr " + gameNr2 + " hiscoreArray Nr. " + i));
        }
        prefs.putInteger("gameNr " + gameNr + "hiscoreArray", getInteger("gameNr " + gameNr2 + "hiscoreArray"));

        prefs.flush();
    }

    private void reWriteBestSnakeEver(int gameNr) {
        int gameNr2 = gameNr + 1;
        //Snake
        for (int k = 0; k < bestSnakeEver.bestSnakeEver.layerArray.size - 1; k++) {
            for (int l = 0; l < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.size; l++) {
                for (int m = 0; m < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.get(l).WeigthArray.size; m++) {
                    prefs.putFloat("gameNr " + gameNr +
                                    " bestSnakeEver.bestSnakeEver " +
                                    " LayerNr " + k +
                                    " NodeNr " + l +
                                    " WeightNr " + m,
                            getFloat("gameNr " + gameNr2 +
                                    " bestSnakeEver.bestSnakeEver " +
                                    " LayerNr " + k +
                                    " NodeNr " + l +
                                    " WeightNr " + m));
                }
            }
        }

        //StartDir
        prefs.putInteger("gameNr " + gameNr + "dirX", getInteger("gameNr " + gameNr2 + "dirX"));
        prefs.putInteger("gameNr " + gameNr + "dirX", getInteger("gameNr " + gameNr2 + "dirY"));

        //directionArray
        for (int i = 0; i < getInteger("gameNr " + gameNr + "directionArray"); i++) {
            prefs.putInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirX", getInteger("gameNr " + gameNr2 + " richtung Nr. " + i + "dirX"));
            prefs.putInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirY", getInteger("gameNr " + gameNr2 + " richtung Nr. " + i + "dirY"));
        }
        prefs.putInteger("gameNr " + gameNr + "directionArray", getInteger("gameNr " + gameNr2 + "directionArray"));

        //Treats
        for (int i = 0; i < getInteger("gameNr " + gameNr2 + "bestSnakeTreatsSize"); i++) {
            prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsX " + i, getInteger("gameNr " + gameNr2 + "bestSnakeTreatsX " + i));
            prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsY " + i, getInteger("gameNr " + gameNr2 + "bestSnakeTreatsY " + i));
        }
        prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsSize", getInteger("gameNr " + gameNr2 + "bestSnakeTreatsSize"));

        prefs.flush();
    }

    private void reWriteBestSnakeArray(int gameNr) {
        int gameNr2 = gameNr + 1;
        for (int j = 0; j < bestSnakesArray.size; j++) {
            for (int k = 0; k < bestSnakesArray.get(j).layerArray.size - 1; k++) {
                for (int l = 0; l < bestSnakesArray.get(j).layerArray.get(k).NodeArray.size; l++) {
                    for (int m = 0; m < bestSnakesArray.get(j).layerArray.get(k).NodeArray.get(l).WeigthArray.size; m++) {
                        prefs.putFloat("gameNr " + gameNr +
                                        " SnakeNr " + j +
                                        " LayerNr " + k +
                                        " NodeNr " + l +
                                        " WeightNr " + m,
                                getFloat("gameNr " + gameNr2 +
                                        " SnakeNr " + j +
                                        " LayerNr " + k +
                                        " NodeNr " + l +
                                        " WeightNr " + m));
                    }
                }
            }
        }
        prefs.flush();
    }

    private void reWriteEinstellungen(int gameNr) {
        int gameNr2 = gameNr + 1;

        prefs.putFloat("gameNr " + gameNr + "average Fitness", getFloat("gameNr " + gameNr2 + "average Fitness"));
        prefs.putInteger("gameNr " + gameNr + "hiScore", getInteger("gameNr " + gameNr2 + "hiScore"));
        prefs.putInteger("gameNr " + gameNr + "bestSnakeEver.fitness", getInteger("gameNr " + gameNr2 + "bestSnakeEver.fitness"));
        prefs.putInteger("gameNr " + gameNr + "population", getInteger("gameNr " + gameNr2 + "population"));

        //Neuronales Netzwerk Eigenschaften
        prefs.putFloat("gameNr " + gameNr + "bias", getFloat("gameNr " + gameNr2 + "bias"));
        prefs.putFloat("gameNr " + gameNr + "biasOutput", getFloat("gameNr " + gameNr2 + "biasOutput"));

        prefs.putFloat("gameNr " + gameNr + "mutationPropability", getFloat("gameNr " + gameNr2 + "mutationPropability"));
        prefs.putFloat("gameNr " + gameNr + "mutationMin", getFloat("gameNr " + gameNr2 + "mutationMin"));
        prefs.putFloat("gameNr " + gameNr + "mutationMax", getFloat("gameNr " + gameNr2 + "mutationMax"));
        prefs.putInteger("gameNr " + gameNr + "bestSnakesArraySize", getInteger("gameNr " + gameNr2 + "bestSnakesArraySize"));

        //Neuronales Netzwerk Aussehen
        prefs.putInteger("gameNr " + gameNr + "inputLayerNodes", getInteger("gameNr " + gameNr2 + "inputLayerNodes"));
        prefs.putInteger("gameNr " + gameNr + "Layer2Nodes", getInteger("gameNr " + gameNr2 + "Layer2Nodes"));
        prefs.putInteger("gameNr " + gameNr + "Layer3Nodes", getInteger("gameNr " + gameNr2 + "Layer3Nodes"));
        prefs.putInteger("gameNr " + gameNr + "Layer4Nodes", getInteger("gameNr " + gameNr2 + "Layer4Nodes"));
        prefs.putInteger("gameNr " + gameNr + "outputLayerNodes", getInteger("gameNr " + gameNr2 + "outputLayerNodes"));
        prefs.putInteger("gameNr " + gameNr + "LayerMenge", getInteger("gameNr " + gameNr2 + "LayerMenge"));

        //Debugging Eigenschaften
        prefs.putBoolean("gameNr " + gameNr + "enableNodeLogging", getBoolean("gameNr " + gameNr2 + "enableNodeLogging"));
        prefs.putBoolean("gameNr " + gameNr + "enableSehrNahLogging", getBoolean("gameNr " + gameNr2 + "enableSehrNahLogging"));
        prefs.putBoolean("gameNr " + gameNr + "enableOutputLayerLogging", getBoolean("gameNr " + gameNr2 + "enableOutputLayerLogging"));
        prefs.putBoolean("gameNr " + gameNr + "enableInputLayerLogging", getBoolean("gameNr " + gameNr2 + "enableInputLayerLogging"));

        //Evolutions Eigenschaften
        prefs.putInteger("gameNr " + gameNr + "POPULATIONSIZE", getInteger("gameNr " + gameNr2 + "POPULATIONSIZE"));
        prefs.putInteger("gameNr " + gameNr + "FIRSTPOPULATIONSIZE", getInteger("gameNr " + gameNr2 + "FIRSTPOPULATIONSIZE"));
        prefs.flush();
    }

    private void removeGraph(int gameNr) {
        for (int i = 0; i < getInteger("gameNr " + gameNr + "averageFitnessArray"); i++) {
            prefs.remove("gameNr " + gameNr + " averageFitnessArray Nr. " + i);
        }
        prefs.remove("gameNr " + gameNr + "averageFitnessArray");

        for (int i = 0; i < getInteger("gameNr " + gameNr + "hiscoreArray"); i++) {
            prefs.remove("gameNr " + gameNr + " hiscoreArray Nr. " + i);
        }
        prefs.remove("gameNr " + gameNr + "hiscoreArray");
        prefs.flush();
    }


    private void removeBestSnakeEver(int gameNr) {
        //Snake
        for (int k = 0; k < bestSnakeEver.bestSnakeEver.layerArray.size - 1; k++) {
            for (int l = 0; l < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.size; l++) {
                for (int m = 0; m < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.get(l).WeigthArray.size; m++) {
                    prefs.remove("gameNr " + gameNr +
                            " bestSnakeEver.bestSnakeEver " +
                            " LayerNr " + k +
                            " NodeNr " + l +
                            " WeightNr " + m);
                }
            }
        }

        //StartDir
        prefs.remove("gameNr " + gameNr + "dirX");
        prefs.remove("gameNr " + gameNr + "dirX");

        //directionArray
        for (int i = 0; i < getInteger("gameNr " + gameNr + "directionArray"); i++) {
            prefs.remove("gameNr " + gameNr + " richtung Nr. " + i + "dirX");
            prefs.remove("gameNr " + gameNr + " richtung Nr. " + i + "dirY");
        }
        prefs.remove("gameNr " + gameNr + "directionArray");

        //Treats
        for (int i = 0; i < getInteger("gameNr " + gameNr + "bestSnakeTreatsSize"); i++) {
            prefs.remove("gameNr " + gameNr + "bestSnakeTreatsX " + i);
            prefs.remove("gameNr " + gameNr + "bestSnakeTreatsY " + i);
        }
        prefs.remove("gameNr " + gameNr + "bestSnakeTreatsSize");


        prefs.flush();
    }

    private void removeBestSnakeArray(int gameNr) {
        for (int j = 0; j < bestSnakesArray.size; j++) {
            for (int k = 0; k < bestSnakesArray.get(j).layerArray.size - 1; k++) {
                for (int l = 0; l < bestSnakesArray.get(j).layerArray.get(k).NodeArray.size; l++) {
                    for (int m = 0; m < bestSnakesArray.get(j).layerArray.get(k).NodeArray.get(l).WeigthArray.size; m++) {
                        prefs.remove("gameNr " + gameNr +
                                " SnakeNr " + j +
                                " LayerNr " + k +
                                " NodeNr " + l +
                                " WeightNr " + m);
                    }
                }
            }
        }
        prefs.flush();
    }

    private void removeEinstellungen(int gameNr) {
        prefs.remove("gameNr " + gameNr + "average Fitness");
        prefs.remove("gameNr " + gameNr + "hiScore");
        prefs.remove("gameNr " + gameNr + "bestSnakeEver.fitness");
        prefs.remove("gameNr " + gameNr + "population");

        //Neuronales Netzwerk Eigenschaften
        prefs.remove("gameNr " + gameNr + "bias");
        prefs.remove("gameNr " + gameNr + "biasOutput");

        prefs.remove("gameNr " + gameNr + "mutationPropability");
        prefs.remove("gameNr " + gameNr + "mutationMin");
        prefs.remove("gameNr " + gameNr + "mutationMax");
        prefs.remove("gameNr " + gameNr + "bestSnakesArraySize");

        //Neuronales Netzwerk Aussehen
        prefs.remove("gameNr " + gameNr + "inputLayerNodes");
        prefs.remove("gameNr " + gameNr + "Layer2Nodes");
        prefs.remove("gameNr " + gameNr + "Layer3Nodes");
        prefs.remove("gameNr " + gameNr + "Layer4Nodes");
        prefs.remove("gameNr " + gameNr + "outputLayerNodes");
        prefs.remove("gameNr " + gameNr + "LayerMenge");

        //Debugging Eigenschaften
        prefs.remove("gameNr " + gameNr + "enableNodeLogging");
        prefs.remove("gameNr " + gameNr + "enableSehrNahLogging");
        prefs.remove("gameNr " + gameNr + "enableOutputLayerLogging");
        prefs.remove("gameNr " + gameNr + "enableInputLayerLogging");

        //Evolutions Eigenschaften
        prefs.remove("gameNr " + gameNr + "POPULATIONSIZE");
        prefs.remove("gameNr " + gameNr + "FIRSTPOPULATIONSIZE");
        prefs.flush();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.7f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        savedStage.act(Gdx.graphics.getDeltaTime());

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(w / 2, h, w / 2, 0);
        shapeRenderer.end();

        savedStage.draw();
        batch.begin();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
