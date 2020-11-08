package com.snake.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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
import static com.snake.ai.main.buttonStart;
import static com.snake.ai.main.buttonshownodes;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.h;
import static com.snake.ai.main.hiscoreArray;
import static com.snake.ai.main.loadBestSnakeEver;
import static com.snake.ai.main.loadFromSavedSnake;
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

    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    @Override
    public void show() {
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
                    saveCurrentSnake();
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


        for (int i = 1; i < prefs.getInteger("GameMenge") + 1; i++) {
            Group g = new Group();
            loadSavedSnake = new TextButton("Load Game Nr.: " + i +
                    "\nAverage Fitness: " + prefs.getFloat("gameNr " + i + "average Fitness") +
                    "\nBest Fitness Ever: " + prefs.getFloat("gameNr " + i + "bestSnakeEver.fitness") +
                    "\nHighScore: " + prefs.getInteger("gameNr " + i + "hiScore") +
                    "\nPoulation: " + prefs.getInteger("gameNr " + i + "population"), skin);
            loadSavedSnake.setSize(w / 2 / 2f, h / 5);
            loadSavedSnake.setPosition(w / 2 / -6f, 0);
            final int finalI = i;
            loadSavedSnake.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (snakeNr != 0) {
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        System.out.println("Loaded saved Game Nr.:" + finalI);
                        loadBestArraySnake(finalI);
                    } else {
                        if (gameOver) {
                            Snake snake = new Snake();
                            snake.startNewGame();
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                            System.out.println("Loaded saved Game Nr.:" + finalI);
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

    public void saveCurrentSnake() {
        freeze = true;
        prefs.putInteger("GameMenge", prefs.getInteger("GameMenge") + 1);

        int gameNr = prefs.getInteger("GameMenge");
        saveEinstellungen(gameNr);
        saveBestSnakeArray(gameNr);
        saveBestSnakeEver(gameNr);
        saveGraph(gameNr);

        prefs.flush();
        loadSavedSnakeScrollPane();
        freeze = false;
    }

    public void saveGraph(int gameNr) {
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
        //Snake
        for (int k = 0; k < bestSnakeEver.bestSnakeEver.layerArray.size - 1; k++) {
            for (int l = 0; l < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.size; l++) {
                for (int m = 0; m < bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.get(l).WeigthArray.size; m++) {
                    prefs.putFloat("gameNr " + gameNr +
                            " bestSnakeEver.bestSnakeEver " +
                            " LayerNr " + k +
                            " NodeNr " + l +
                            " WeightNr " + m, bestSnakeEver.bestSnakeEver.layerArray.get(k).NodeArray.get(l).WeigthArray.get(m).floatValue());
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
                                " WeightNr " + m, bestSnakesArray.get(j).layerArray.get(k).NodeArray.get(l).WeigthArray.get(m).floatValue());
                    }
                }
            }
        }
        prefs.flush();
    }

    public void saveEinstellungen(int gameNr) {
        int maxFitness = 0;
        for (int m = 0; m < allSnakesArrays.get(0).allSnakesArray.size; m++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(m).fitness;
        }
        prefs.putFloat("gameNr " + gameNr + "average Fitness", maxFitness / POPULATIONSIZE);
        prefs.putInteger("gameNr " + gameNr + "hiScore", hiScore);
        prefs.putLong("gameNr " + gameNr + "bestSnakeEver.fitness", bestSnakeEver.bestSnakeEver.fitness);
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
        freeze = true;
        main.gameNr = gameNr;

        loadEinstellungen(gameNr);
        loadBestSnakeEver(gameNr);
        loadBestSnakeArray(gameNr);
        loadGraph(gameNr);

        snakeNr = 0;
        Snake.gameOver = true;
        freeze = false;
    }

    public void loadGraph(int gameNr) {
        averageFitnessArray.clear();
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "averageFitnessArray"); i++) {
            averageFitnessArray.add(prefs.getInteger("gameNr " + gameNr + " averageFitnessArray Nr. " + i));
        }

        hiscoreArray.clear();
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "hiscoreArray"); i++) {
            hiscoreArray.add(prefs.getInteger("gameNr " + gameNr + " hiscoreArray Nr. " + i));
        }

        prefs.flush();
    }

    public void loadEinstellungen(int gameNr) {
        population = prefs.getInteger("gameNr " + gameNr + "population");
        hiScore = prefs.getInteger("gameNr " + gameNr + "hiScore");

        main.bias = prefs.getFloat("gameNr " + gameNr + "bias");
        main.biasOutput = prefs.getFloat("gameNr " + gameNr + "biasOutput");

        main.mutationPropability = prefs.getFloat("gameNr " + gameNr + "mutationPropability");
        main.mutationMin = prefs.getFloat("gameNr " + gameNr + "mutationMin");
        main.mutationMax = prefs.getFloat("gameNr " + gameNr + "mutationMax");
        bestSnakesArraySize = prefs.getInteger("gameNr " + gameNr + "bestSnakesArraySize");

        //Neuronales Netzwerk Aussehen
        main.inputLayerNodes = prefs.getInteger("gameNr " + gameNr + "inputLayerNodes");
        main.Layer2Nodes = prefs.getInteger("gameNr " + gameNr + "Layer2Nodes");
        main.Layer3Nodes = prefs.getInteger("gameNr " + gameNr + "Layer3Nodes");
        main.Layer4Nodes = prefs.getInteger("gameNr " + gameNr + "Layer4Nodes");
        main.outputLayerNodes = prefs.getInteger("gameNr " + gameNr + "outputLayerNodes");
        main.LayerMenge = prefs.getInteger("gameNr " + gameNr + "LayerMenge");

        //Debugging Eigenschaften
        main.enableNodeLogging = prefs.getBoolean("gameNr " + gameNr + "enableNodeLogging");
        main.enableSehrNahLogging = prefs.getBoolean("gameNr " + gameNr + "enableSehrNahLogging");
        main.enableOutputLayerLogging = prefs.getBoolean("gameNr " + gameNr + "enableOutputLayerLogging");
        main.enableInputLayerLogging = prefs.getBoolean("gameNr " + gameNr + "enableInputLayerLogging");

        //Evolutions Eigenschaften
        main.POPULATIONSIZE = prefs.getInteger("gameNr " + gameNr + "POPULATIONSIZE");
        main.FIRSTPOPULATIONSIZE = prefs.getInteger("gameNr " + gameNr + "FIRSTPOPULATIONSIZE");
    }

    private void loadBestSnakeEver(int gameNr) {
        loadBestSnakeEver = true;
        currentSnake = new Snakes();
        bestSnakeEver.bestSnakeEver = currentSnake;
        bestSnakeEver.bestSnakeEver.fitness = prefs.getLong("gameNr " + gameNr + "bestSnakeEver.fitness");

        int dirX = prefs.getInteger("gameNr " + gameNr + "dirX");
        int dirY = prefs.getInteger("gameNr " + gameNr + "dirY");
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
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "directionArray"); i++) {
            dirX = prefs.getInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirX");
            dirY = prefs.getInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirY");
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

        if(bestSnakeEver.bestSnakeTreats == null)
            bestSnakeEver.bestSnakeTreats = new Array<>();
        bestSnakeEver.bestSnakeTreats.clear();
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "bestSnakeTreatsSize"); i++) {
            int x = prefs.getInteger("gameNr " + gameNr + "bestSnakeTreatsX " + i);
            int y = prefs.getInteger("gameNr " + gameNr + "bestSnakeTreatsY " + i);
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

    private void delete(int id) {
        prefs.putInteger("GameMenge", prefs.getInteger("GameMenge") - 1);

        for (int i = id; i < prefs.getInteger("GameMenge") + 1; i++) {
            reWriteBestSnakeEver(i);
            reWriteBestSnakeArray(i);
            reWriteEinstellungen(i);
            reWriteGraph(i);
        }

        removeBestSnakeEver(prefs.getInteger("GameMenge") + 1);
        removeBestSnakeArray(prefs.getInteger("GameMenge") + 1);
        removeEinstellungen(prefs.getInteger("GameMenge") + 1);
        removeGraph(prefs.getInteger("GameMenge") + 1);

        prefs.flush();
    }

    private void reWriteGraph(int gameNr) {
        int gameNr2 = gameNr + 1;

        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "averageFitnessArray"); i++) {
            prefs.putInteger("gameNr " + gameNr + " averageFitnessArray Nr. " + i, prefs.getInteger("gameNr " + gameNr2 + " averageFitnessArray Nr. " + i));
        }
        prefs.putInteger("gameNr " + gameNr + "averageFitnessArray", prefs.getInteger("gameNr " + gameNr2 + "averageFitnessArray"));

        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "hiscoreArray"); i++) {
            prefs.putInteger("gameNr " + gameNr + " hiscoreArray Nr. " + i, prefs.getInteger("gameNr " + gameNr2 + " hiscoreArray Nr. " + i));
        }
        prefs.putInteger("gameNr " + gameNr + "hiscoreArray", prefs.getInteger("gameNr " + gameNr2 + "hiscoreArray"));

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
                            prefs.getFloat("gameNr " + gameNr2 +
                                    " bestSnakeEver.bestSnakeEver " +
                                    " LayerNr " + k +
                                    " NodeNr " + l +
                                    " WeightNr " + m));
                }
            }
        }

        //StartDir
        prefs.putInteger("gameNr " + gameNr + "dirX", prefs.getInteger("gameNr " + gameNr2 + "dirX"));
        prefs.putInteger("gameNr " + gameNr + "dirX", prefs.getInteger("gameNr " + gameNr2 + "dirY"));

        //directionArray
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "directionArray"); i++) {
            prefs.putInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirX", prefs.getInteger("gameNr " + gameNr2 + " richtung Nr. " + i + "dirX"));
            prefs.putInteger("gameNr " + gameNr + " richtung Nr. " + i + "dirY", prefs.getInteger("gameNr " + gameNr2 + " richtung Nr. " + i + "dirY"));
        }
        prefs.putInteger("gameNr " + gameNr + "directionArray", prefs.getInteger("gameNr " + gameNr2 + "directionArray"));

        //Treats
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr2 + "bestSnakeTreatsSize"); i++) {
            prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsX " + i, prefs.getInteger("gameNr " + gameNr2 + "bestSnakeTreatsX " + i));
            prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsY " + i, prefs.getInteger("gameNr " + gameNr2 + "bestSnakeTreatsY " + i));
        }
        prefs.putInteger("gameNr " + gameNr + "bestSnakeTreatsSize", prefs.getInteger("gameNr " + gameNr2 + "bestSnakeTreatsSize"));

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
                                prefs.getFloat("gameNr " + gameNr2 +
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

        prefs.putFloat("gameNr " + gameNr + "average Fitness", prefs.getFloat("gameNr " + gameNr2 + "average Fitness"));
        prefs.putInteger("gameNr " + gameNr + "hiScore", prefs.getInteger("gameNr " + gameNr2 + "hiScore"));
        prefs.putInteger("gameNr " + gameNr + "bestSnakeEver.fitness", prefs.getInteger("gameNr " + gameNr2 + "bestSnakeEver.fitness"));
        prefs.putInteger("gameNr " + gameNr + "population", prefs.getInteger("gameNr " + gameNr2 + "population"));

        //Neuronales Netzwerk Eigenschaften
        prefs.putFloat("gameNr " + gameNr + "bias", prefs.getFloat("gameNr " + gameNr2 + "bias"));
        prefs.putFloat("gameNr " + gameNr + "biasOutput", prefs.getFloat("gameNr " + gameNr2 + "biasOutput"));

        prefs.putFloat("gameNr " + gameNr + "mutationPropability", prefs.getFloat("gameNr " + gameNr2 + "mutationPropability"));
        prefs.putFloat("gameNr " + gameNr + "mutationMin", prefs.getFloat("gameNr " + gameNr2 + "mutationMin"));
        prefs.putFloat("gameNr " + gameNr + "mutationMax", prefs.getFloat("gameNr " + gameNr2 + "mutationMax"));
        prefs.putInteger("gameNr " + gameNr + "bestSnakesArraySize", prefs.getInteger("gameNr " + gameNr2 + "bestSnakesArraySize"));

        //Neuronales Netzwerk Aussehen
        prefs.putInteger("gameNr " + gameNr + "inputLayerNodes", prefs.getInteger("gameNr " + gameNr2 + "inputLayerNodes"));
        prefs.putInteger("gameNr " + gameNr + "Layer2Nodes", prefs.getInteger("gameNr " + gameNr2 + "Layer2Nodes"));
        prefs.putInteger("gameNr " + gameNr + "Layer3Nodes", prefs.getInteger("gameNr " + gameNr2 + "Layer3Nodes"));
        prefs.putInteger("gameNr " + gameNr + "Layer4Nodes", prefs.getInteger("gameNr " + gameNr2 + "Layer4Nodes"));
        prefs.putInteger("gameNr " + gameNr + "outputLayerNodes", prefs.getInteger("gameNr " + gameNr2 + "outputLayerNodes"));
        prefs.putInteger("gameNr " + gameNr + "LayerMenge", prefs.getInteger("gameNr " + gameNr2 + "LayerMenge"));

        //Debugging Eigenschaften
        prefs.putBoolean("gameNr " + gameNr + "enableNodeLogging", prefs.getBoolean("gameNr " + gameNr2 + "enableNodeLogging"));
        prefs.putBoolean("gameNr " + gameNr + "enableSehrNahLogging", prefs.getBoolean("gameNr " + gameNr2 + "enableSehrNahLogging"));
        prefs.putBoolean("gameNr " + gameNr + "enableOutputLayerLogging", prefs.getBoolean("gameNr " + gameNr2 + "enableOutputLayerLogging"));
        prefs.putBoolean("gameNr " + gameNr + "enableInputLayerLogging", prefs.getBoolean("gameNr " + gameNr2 + "enableInputLayerLogging"));

        //Evolutions Eigenschaften
        prefs.putInteger("gameNr " + gameNr + "POPULATIONSIZE", prefs.getInteger("gameNr " + gameNr2 + "POPULATIONSIZE"));
        prefs.putInteger("gameNr " + gameNr + "FIRSTPOPULATIONSIZE", prefs.getInteger("gameNr " + gameNr2 + "FIRSTPOPULATIONSIZE"));
        prefs.flush();
    }

    private void removeGraph(int gameNr) {
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "averageFitnessArray"); i++) {
            prefs.remove("gameNr " + gameNr + " averageFitnessArray Nr. " + i);
        }
        prefs.remove("gameNr " + gameNr + "averageFitnessArray");

        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "hiscoreArray"); i++) {
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
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "directionArray"); i++) {
            prefs.remove("gameNr " + gameNr + " richtung Nr. " + i + "dirX");
            prefs.remove("gameNr " + gameNr + " richtung Nr. " + i + "dirY");
        }
        prefs.remove("gameNr " + gameNr + "directionArray");

        //Treats
        for (int i = 0; i < prefs.getInteger("gameNr " + gameNr + "bestSnakeTreatsSize"); i++) {
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

    public void removeEinstellungen(int gameNr) {
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
        shapeRenderer.line(w / 2,h,w / 2,0);
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
