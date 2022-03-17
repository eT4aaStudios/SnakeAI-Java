package com.snake.ai;

import static com.snake.ai.Settings.POPULATIONSIZE;
import static com.snake.ai.Settings.bestSnakesArraySize;
import static com.snake.ai.Settings.inputLayerNodes;
import static com.snake.ai.Settings.layer2Nodes;
import static com.snake.ai.Settings.layer3Nodes;
import static com.snake.ai.Settings.layer4Nodes;
import static com.snake.ai.Settings.layerMenge;
import static com.snake.ai.Settings.maxEnergy;
import static com.snake.ai.Settings.mutationMax;
import static com.snake.ai.Settings.mutationProbability;
import static com.snake.ai.Settings.outputLayerNodes;
import static com.snake.ai.Settings.reihen;
import static com.snake.ai.Settings.spalten;
import static com.snake.ai.Settings.startLength;
import static com.snake.ai.SnakeGame.energy;
import static com.snake.ai.SnakeGame.gameOver;
import static com.snake.ai.SnakeGame.score;
import static com.snake.ai.SnakeGame.sleepTime;
import static com.snake.ai.SnakeGame.snakeNr;
import static com.snake.ai.SnakeGame.timePerPop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class main extends Game {

    public static SpriteBatch batch;
    Stage stage;
    static Button buttonStart;
    Button buttonGraph;
    static TextButton buttonLangsamer;
    static TextButton buttonSchneller;
    static TextButton buttonMax;
    static TextButton buttonShowNodes;
    static TextButton savedSnakesScreenButton;
    TextButton buttonReplay;
    Skin skin;
    public static float w;
    public static float h;
    public static int foodPositionX, foodPositionY;
    public static int snakeHeadX, snakeHeadY;
    Array<Integer> felderArray;
    public static boolean freeze = true;
    public static Snake currentSnake;
    public static Array<Integer> layerNodeValueArray;
    public static boolean currentScreen;
    public static Array<Snake> snakeArray;
    static ShapeRenderer shapeRenderer;
    BitmapFont font;
    public static boolean graphmode1;
    public static int populationsSinceLastSave;
    public static Random r = ThreadLocalRandom.current();
    public static Evolution evo;
    InputLayerDetection newDetection;
    public static int averageSteps;
    public static boolean loadFromSavedSnake, loadBestSnakeEver;
    public static int gameNr = -1;
    public static int SnakeNr;
    public static boolean replay, requestReplayStop;
    public static Array<Snake> bestSnakes = new Array<>();
    public static Gson gson = new Gson();
    public static SnakeGameInstance snakeGameInstance = new SnakeGameInstance();
    SnakeGame snakeGame;
    NodeVis NodeVis;
    SavedSnakes SavedSnakes;

    @Override
    public void create() {
        snakeGame = new SnakeGame(this);
        layerNodeValueArray = new Array<>();
        layerNodeValueArray.add(inputLayerNodes);
        layerNodeValueArray.add(layer2Nodes);
        layerNodeValueArray.add(layer3Nodes);
        layerNodeValueArray.add(layer4Nodes);
        layerNodeValueArray.add(outputLayerNodes);
        for (int i = 0; i < layerNodeValueArray.size; i++) {
            if (layerNodeValueArray.get(i) == 0) {
                layerNodeValueArray.removeIndex(i);
            }
        }
        System.out.println("Trainable Weights: "+(inputLayerNodes * layer2Nodes + layer2Nodes * layer3Nodes + layer3Nodes * layer4Nodes + layer4Nodes * outputLayerNodes));
        System.out.println("Trainable Biases: "+(inputLayerNodes  + layer2Nodes + layer3Nodes  + layer4Nodes));
        System.out.println("Trainable Parameters Sum: "+(inputLayerNodes * layer2Nodes + layer2Nodes * layer3Nodes + layer3Nodes * layer4Nodes + layer4Nodes * outputLayerNodes +
                inputLayerNodes  + layer2Nodes + layer3Nodes  + layer4Nodes));
        System.out.println("Mutated Parameters expected: "+(inputLayerNodes * layer2Nodes + layer2Nodes * layer3Nodes + layer3Nodes * layer4Nodes + layer4Nodes * outputLayerNodes +
                inputLayerNodes  + layer2Nodes + layer3Nodes  + layer4Nodes) * (mutationProbability / 100));

        snakeArray = new Array<>();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        felderArray = new Array<>();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(w / 1100);
        evo = new Evolution();

        buttonStart = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("transparent2.png"))));
        buttonStart.setSize(w / 2, h);
        buttonStart.setPosition(w / 2, 0);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameOver) {
                    snakeGame.startNewGame();
                    freeze = false;
                } else {
                    if (sleepTime == 0)
                        sleepTime = 40;
                    freeze = !freeze;
                }
            }
        });
        buttonGraph = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("transparent2.png"))));
        buttonGraph.setSize(w / 3.9f, h / 2.23f);
        buttonGraph.setPosition(w / 100, h / 2f);
        buttonGraph.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                graphmode1 = !graphmode1;
            }
        });
        buttonLangsamer = new TextButton("Slower", skin);
        buttonLangsamer.setSize(w / 14, h / 8);
        buttonLangsamer.setPosition(w / 100, h / 3f);
        buttonLangsamer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sleepTime += 40;
            }
        });
        buttonSchneller = new TextButton("Faster", skin);
        buttonSchneller.setSize(buttonLangsamer.getWidth(), buttonLangsamer.getHeight());
        buttonSchneller.setPosition(buttonLangsamer.getX(), h / 5.3f);
        buttonSchneller.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (sleepTime > 30)
                    sleepTime -= 30;
            }
        });
        buttonMax = new TextButton("Max Velocity", skin);
        buttonMax.setSize(buttonLangsamer.getWidth(), buttonLangsamer.getHeight());
        buttonMax.setPosition(buttonLangsamer.getX(), h / 50f);
        buttonMax.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sleepTime = 0;
            }
        });
        buttonReplay = new TextButton(" Replay Best SnakeGame\n(Active: " + replay + ")", skin);
        buttonReplay.setSize(buttonLangsamer.getWidth(), buttonLangsamer.getHeight());
        buttonReplay.setPosition(w / 7f, buttonSchneller.getY());
        buttonReplay.getLabel().setFontScale(w / 1100);
        buttonReplay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (replay) {
                    requestReplayStop = true;
                    buttonReplay.setText(" Replay Best SnakeGame\n(Active: false)");
                } else {
                    replay = true;
                    buttonReplay.setText(" Replay Best SnakeGame\n(Active: true)");
                }
                gameOver = true;
                currentSnake = snakeGameInstance.bestSnake;

            }
        });
        buttonShowNodes = new TextButton("Show Nodes", skin);
        buttonShowNodes.setSize(buttonLangsamer.getWidth(), h / 8);
        buttonShowNodes.setPosition(buttonReplay.getX(), buttonLangsamer.getY());
        buttonShowNodes.getLabel().setFontScale(buttonReplay.getLabel().getFontScaleX());
        buttonShowNodes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.this.setScreen(NodeVis);
                if (currentScreen) {
                    stage.clear();
                    stage.addActor(buttonGraph);
                    stage.addActor(buttonStart);
                    stage.addActor(savedSnakesScreenButton);
                    stage.addActor(buttonShowNodes);
                    stage.addActor(buttonLangsamer);
                    stage.addActor(buttonSchneller);
                    stage.addActor(buttonMax);
                    stage.addActor(buttonReplay);
                } else {
                    stage.clear();
                    stage.addActor(buttonStart);
                    stage.addActor(savedSnakesScreenButton);
                    stage.addActor(buttonShowNodes);
                }
                currentScreen = !currentScreen;
            }
        });
        savedSnakesScreenButton = new TextButton("Show Saved Snake", skin);
        savedSnakesScreenButton.setSize(buttonLangsamer.getWidth(), h / 8);
        savedSnakesScreenButton.setPosition(buttonReplay.getX(), buttonMax.getY());
        savedSnakesScreenButton.getLabel().setFontScale(buttonReplay.getLabel().getFontScaleX());
        savedSnakesScreenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (main.this.getScreen() != SavedSnakes) {
                    main.this.setScreen(SavedSnakes);
                } else {
                    main.this.setScreen(NodeVis);
                    Gdx.input.setInputProcessor(stage);
                    currentScreen = false;
                    stage.clear();
                    stage.addActor(buttonGraph);
                    stage.addActor(buttonStart);
                    stage.addActor(savedSnakesScreenButton);
                    stage.addActor(buttonShowNodes);
                    stage.addActor(buttonLangsamer);
                    stage.addActor(buttonSchneller);
                    stage.addActor(buttonMax);
                    stage.addActor(buttonReplay);
                }
            }
        });

        stage.addActor(buttonGraph);
        stage.addActor(buttonStart);
        stage.addActor(savedSnakesScreenButton);
        stage.addActor(buttonShowNodes);
        stage.addActor(buttonLangsamer);
        stage.addActor(buttonSchneller);
        stage.addActor(buttonMax);
        stage.addActor(buttonReplay);

        currentSnake = new Snake();
        for (int i = 0; i < bestSnakesArraySize; i++) {
            bestSnakes.add(currentSnake);
        }
        snakeGameInstance.bestSnake = new Snake();

        NodeVis = new NodeVis();
        SavedSnakes = new SavedSnakes(this);
        newDetection = new InputLayerDetection();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.3f, 0.7f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(w / 2, h, w / 2, 0);
        shapeRenderer.end();

        drawGame();

        if (this.getScreen() != SavedSnakes) {
            if (!currentScreen) {
                try {
                    drawFonts();
                    drawGraph();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            stage.draw();
        }
        batch.begin();
        font.draw(batch, "Paused: " + freeze, w / 1.12f, h / 1.075f);
        batch.end();

        //if (treats != null)
        //    System.out.println(treats.size);
    }


    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public void ResetLayers() {
        for (int i = 0; i < currentSnake.layerArray.size; i++) {
            for (int j = 0; j < currentSnake.layerArray.get(i).nodeArray.size; j++) {
                currentSnake.layerArray.get(i).nodeArray.get(j).value = 0;
            }
        }
    }

    public void berechneLayer() {
        ResetLayers();
        newDetection.start();
        for (int i = 0; i < layerMenge - 1; i++) {
            berechneLayer(i);
        }
    }

    public void berechneLayer(int layerNumber) {
        for (int NodeLayer2 = 0; NodeLayer2 < currentSnake.layerArray.get(layerNumber + 1).nodeArray.size; NodeLayer2++) {
            double sum = 0;
            for (int NodeLayer1 = 0; NodeLayer1 < currentSnake.layerArray.get(layerNumber).nodeArray.size; NodeLayer1++) {
                //weight
                double weight = currentSnake.layerArray.get(layerNumber).nodeArray.get(NodeLayer1).weightArray.get(NodeLayer2);
                //value
                double value = currentSnake.layerArray.get(layerNumber).nodeArray.get(NodeLayer1).value;
                sum += weight * value;
            }

            double bias = currentSnake.layerArray.get(layerNumber + 1).nodeArray.get(NodeLayer2).bias;
            if (layerNumber == layerMenge - 2)
                currentSnake.layerArray.get(layerNumber + 1).nodeArray.get(NodeLayer2).value = outputActivationFunction(sum, bias);
            else
                currentSnake.layerArray.get(layerNumber + 1).nodeArray.get(NodeLayer2).value = activationFunction(sum, bias);
        }
    }

    public double activationFunction(double x, double bias) {
        x += bias;

        //Sigmoid
        //return 1d / (1d + Math.exp(-x));

        //Tanh
        //return Math.tanh(x);

        //Relu
        return Math.max(0, x);

        //Relu 2.0 (Best)
        //if (x > 0) {
        //    return 1;
        //} else {
        //    return 0;
        //}
    }

    public double outputActivationFunction(double x, double bias) {
        x += bias;

        //Sigmoid
        return 1 / (1 + Math.exp(-x));

        //Tanh (Best)
        //return Math.tanh(x);

        //Relu
        //return Math.max(0, x);

        //Relu 2.0
        //if (x > 0) {
        //    return 1;
        //} else {
        //    return 0;
        //}
    }

    public void drawGraph() {
        Array<Integer> tmpArray;
        if (!graphmode1) {
            tmpArray = new Array<>(snakeGameInstance.hiscoreArray);
        } else {
            tmpArray = new Array<>(snakeGameInstance.averageFitnessArray);
        }


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(0
                , h / 2f - w / 100
                , (w / 100) * 2 + w / 3.9f
                , (w / 100) * 2 + h / 2.23f);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(buttonLangsamer.getX()
                , h / 2f
                , w / 3.9f
                , h / 2.23f);
        shapeRenderer.end();


        if (tmpArray.size > 1) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.WHITE);
            //X Achse
            shapeRenderer.line(w / 20
                    , h / 1.82f
                    , w / 4f
                    , h / 1.82f);
            //Y Achse
            shapeRenderer.line(w / 20
                    , h / 1.82f
                    , w / 20
                    , h / 1.08f);

            int highest = 0;
            for (int i = 0; i < tmpArray.size; i++) {
                if (tmpArray.get(i) >= tmpArray.get(highest))
                    highest = i;
            }

            batch.begin();
            for (int i = 0; i < tmpArray.get(highest) + 1; i++) {
                if ((i % ((int) ((tmpArray.get(highest) / 5.0)) + 1)) == 0) {
                    font.setUseIntegerPositions(false);
                    font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

                    if (!graphmode1)
                        font.draw(batch, "" + i, w / 80, ((h / 1.08f - h / 1.75f) / tmpArray.get(highest)) * i + h / 1.75f);
                    else
                        font.draw(batch, "" + fitnessDoubleToE(i), w / 80, ((h / 1.08f - h / 1.75f) / tmpArray.get(highest)) * i + h / 1.75f);
                }
            }

            for (int i = 0; i < tmpArray.size; i++) {
                if (tmpArray.size > 1) {
                    if ((i % ((int) ((tmpArray.size / 5.0)) + 1)) == 0) {
                        font.draw(batch, "" + i, ((w / 4f - w / 20f) / tmpArray.size) * i + w / 20, h / 1.85f);
                    }
                }
            }
            batch.end();

            for (int i = 0; i < tmpArray.size - 1; i++) {
                shapeRenderer.line(
                        ((w / 4f - w / 20f) / tmpArray.size) * i + w / 20f
                        , ((h / 1.08f - h / 1.82f) / tmpArray.get(highest)) * tmpArray.get(i) + h / 1.82f
                        , ((w / 4f - w / 20f) / tmpArray.size) * (i + 1) + w / 20f
                        , ((h / 1.08f - h / 1.82f) / tmpArray.get(highest)) * tmpArray.get(i + 1) + h / 1.82f);
            }
        }

        shapeRenderer.end();
    }

    public void drawFonts() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect((w / 3.6f + (w / 4.7f - w / 5.4f) / 2) - w / 100
                , h / 50f - w / 100
                , w / 5.4f + (w / 100) * 2
                , (h / 2f - w / 100 + w / 100 + h / 2.23f) - h / 50f + (w / 100) * 2);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(w / 3.6f + (w / 4.7f - w / 5.4f) / 2
                , h / 50f
                , w / 5.4f
                , (h / 2f - w / 100 + w / 100 + h / 2.23f) - h / 50f);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "Settings", w / 3.35f, h / 1.07f);
        font.draw(batch, "______________________", w / 3.35f, h / 1.08f);
        font.draw(batch, "Game Nr: " + gameNr, w / 3.35f, h / 1.13f);
        font.draw(batch, "SnakeGame Nr: " + snakeNr, w / 3.35f, h / 1.18f);
        font.draw(batch, "Score: " + score, w / 3.35f, h / 1.23f);
        font.draw(batch, "Population: " + snakeGameInstance.population, w / 3.35f, h / 1.28f);
        font.setColor(1f, 0.3f, 0.3f, 1);
        int maxLength = reihen * spalten - startLength;
        font.draw(batch, "Highscore: " + snakeGameInstance.hiScore + " (Max: " + maxLength + ")", w / 3.35f, h / 1.34f);
        font.draw(batch, "Best Fitness Ever: " + fitnessDoubleToE(snakeGameInstance.bestSnake.fitness), w / 3.35f, h / 1.4f);
        font.setColor(Color.WHITE);
        font.draw(batch, "______________________", w / 3.35f, h / 1.45f);
        font.draw(batch, "Input Layer Nodes: " + inputLayerNodes, w / 3.35f, h / 1.72f);
        font.draw(batch, "Layer 2 Nodes: " + layer2Nodes, w / 3.35f, h / 1.82f);
        font.draw(batch, "Layer 3 Nodes: " + layer3Nodes, w / 3.35f, h / 1.93f);
        font.draw(batch, "Layer 4 Nodes: " + layer4Nodes, w / 3.35f, h / 2.05f);
        font.draw(batch, "Output Layer Nodes: " + outputLayerNodes, w / 3.35f, h / 2.18f);
        font.draw(batch, "______________________", w / 3.35f, h / 2.34f);
        font.draw(batch, "Mutation Probability: " + (float) mutationProbability, w / 3.35f, h / 2.55f);
        font.draw(batch, "Mutation Max: " + (float) mutationMax, w / 3.35f, h / 3.1f);
        font.draw(batch, "Population Size: " + POPULATIONSIZE, w / 3.35f, h / 3.4f);
        font.draw(batch, "______________________", w / 3.35f, h / 4.5f);
        font.draw(batch, "Rows: " + reihen, w / 3.35f, h / 5.5f);
        font.draw(batch, "Columns: " + spalten, w / 3.35f, h / 6.5f);
        font.draw(batch, "______________________", w / 3.35f, h / 7.5f);
        font.draw(batch, "Time Per Population: " + timePerPop, w / 3.35f, h / 12f);
        batch.end();
    }

    public void drawGame() {
        //Feld
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < (reihen + 2); i++) {
            for (int j = 0; j < (spalten + 2); j++) {
                if (i == 0 || j == 0 || i == (reihen + 2) - 1 || j == (spalten + 2) - 1) {
                    shapeRenderer.setColor(Color.GRAY);
                } else {
                    if ((i + j) % 2 == 0)
                        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
                    else
                        shapeRenderer.setColor(0.25f, 0.25f, 0.25f, 1);
                }
                shapeRenderer.rect(w / 2 + i * w / 2 / (reihen + 2)
                        , j * h / (spalten + 2)
                        , w / 2 / (reihen + 2)
                        , h / (spalten + 2));
            }
        }
        //Treats
        try {
            shapeRenderer.setColor(1, 1, 1, 1);
            shapeRenderer.rect(SnakeGame.treats.get(SnakeGame.treats.size - 1).x * w / 2 / (reihen + 2) + w / 2
                    , ((spalten + 2) - 1 - SnakeGame.treats.get(SnakeGame.treats.size - 1).y) * h / (spalten + 2)
                    , w / 2 / (reihen + 2)
                    , h / (spalten + 2));
        } catch (Exception ignored) {

        }
        //Schlange
        try {
            if (SnakeGame.snake != null && SnakeGame.snake.size > 0) {
                for (int i = 0; i < SnakeGame.snake.size; i++) {
                    try {
                        shapeRenderer.setColor(0.3f, 0.7f, 0.3f, 1);
                        shapeRenderer.rect(SnakeGame.snake.get(i).x * w / 2 / (reihen + 2) + w / 2
                                , ((spalten + 2) - 1 - SnakeGame.snake.get(i).y) * h / (spalten + 2)
                                , w / 2 / (reihen + 2)
                                , h / (spalten + 2));
                        if (i == 0) {
                            //Head
                            shapeRenderer.setColor(1f + ((energy) / (float) maxEnergy - 1), 0.3f + ((energy) / (float) maxEnergy - 1), 0.3f + ((energy) / (float) maxEnergy - 1), 1);
                            shapeRenderer.rect(SnakeGame.snake.get(i).x * w / 2 / (reihen + 2) + w / 2
                                    , ((spalten + 2) - 1 - SnakeGame.snake.get(i).y) * h / (spalten + 2)
                                    , w / 2 / (reihen + 2)
                                    , h / (spalten + 2));
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        } catch (Exception ignored) {

        }
        shapeRenderer.end();
    }

    public static String fitnessDoubleToE(double fitness) {
        DecimalFormat df;
        if(fitness >= 1000)
            df = new DecimalFormat("##0.E0");
        else
            df = new DecimalFormat("##0");
        return df.format(fitness);
    }
}
