package com.snake.ai;

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

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.snake.ai.SavedSnakes.prefs;
import static com.snake.ai.Snake.Sleep_Time;
import static com.snake.ai.Snake.gameOver;
import static com.snake.ai.Snake.hiScore;
import static com.snake.ai.Snake.population;
import static com.snake.ai.Snake.score;
import static com.snake.ai.Snake.snakeNr;
import static com.snake.ai.Snake.startlength;
import static com.snake.ai.Snake.timePerPop;

public class main extends Game {
    //Best :

    //bias 0.4
    //biasOutput -0.4

    //inputLayerNodes = 16;
    //Layer2Nodes = 16;
    //Layer3Nodes = 16;
    //Layer4Nodes = 0;
    //outputLayerNodes = 4;

    public static SpriteBatch batch;
    Stage stage;
    static Button buttonStart;
    Button buttonGraph;
    TextButton buttonLangsamer;
    TextButton buttonSchneller;
    TextButton buttonMax;
    static TextButton buttonshownodes;
    static TextButton savedSnakesScreenbutton;
    TextButton buttonreplay;
    Skin skin;
    public static float w;
    public static float h;
    public static int foodpositionX, foodpositionY;
    public static int SnakeHeadX, SnakeHeadY;
    Array<Integer> felderarray;
    public static boolean freeze = true;
    public static Snakes currentSnake;
    public static Array<Integer> layerNodeValueArray;
    public static boolean currentScreen;
    public static Array<allSnakes> allSnakesArrays;
    public static Array<Snakes> bestSnakesArray;
    static ShapeRenderer shapeRenderer;
    BitmapFont font;
    public static Array<Integer> averageFitnessArray;
    public static Array<Integer> hiscoreArray;
    public static boolean graphmode1;
    public static int populationsSinceLastSave;
    public static Random r = ThreadLocalRandom.current();

    public static boolean loadFromSavedSnake, loadBestSnakeEver;
    public static int gameNr = -1;
    public static int SnakeNr;
    public static boolean replay, requestReplayStop;

    public static BestSnakeEver bestSnakeEver = new BestSnakeEver();

    //Neuronales Netzwerk Eigenschaften
    public static double bias = 0f;
    public static double biasOutput = 0;
    public static int bestSnakesArraySize = 20;

    public static double mutationPropability = 5f;//%
    public static double mutationMin = -1f;
    public static double mutationMax = 1f;

    //Neuronales Netzwerk Aussehen
    static int inputLayerNodes = 25;
    static int Layer2Nodes = 18;
    static int Layer3Nodes = 18;
    static int Layer4Nodes = 0;
    static int outputLayerNodes = 4;
    static int LayerMenge = 4;

    //Debugging Eigenschaften
    public static boolean enableNodeLogging = false;
    public static boolean enableSehrNahLogging = false;
    public static boolean enableOutputLayerLogging = false;
    public static boolean enableInputLayerLogging = false;
    public static boolean enableNewPopulationLogging = false;

    //Evolutions Eigenschaften
    public static int POPULATIONSIZE = 1000;
    public static int FIRSTPOPULATIONSIZE = 1000;

    public static int reihen = 22;
    public static int spalten = 22;

    Snake snake;

    NodeVis NodeVis;
    SavedSnakes SavedSnakes;

    @Override
    public void create() {
        snake = new Snake(this);
        layerNodeValueArray = new Array<>();
        layerNodeValueArray.add(inputLayerNodes);
        layerNodeValueArray.add(Layer2Nodes);
        layerNodeValueArray.add(Layer3Nodes);
        layerNodeValueArray.add(Layer4Nodes);
        layerNodeValueArray.add(outputLayerNodes);
        for (int i = 0; i < layerNodeValueArray.size; i++)
            if (layerNodeValueArray.get(i) == 0)
                layerNodeValueArray.removeIndex(i);
        allSnakesArrays = new Array<>();
        bestSnakesArray = new Array<>();
        averageFitnessArray = new Array<>();
        averageFitnessArray.add(0);
        hiscoreArray = new Array<>();
        hiscoreArray.add(0);
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        felderarray = new Array<>();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(w / 1100);


        buttonStart = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("transparent2.png"))));
        buttonStart.setSize(w / 2, h);
        buttonStart.setPosition(w / 2, 0);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameOver) {
                    snake.startNewGame();
                    freeze = false;
                } else {
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
        buttonLangsamer.setSize(w / 8, h / 8);
        buttonLangsamer.setPosition(w / 100, h / 3f);
        buttonLangsamer.getLabel().setFontScale(w / 1100);
        buttonLangsamer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sleep_Time += 40;
            }
        });
        buttonSchneller = new TextButton("Faster", skin);
        buttonSchneller.setSize(buttonLangsamer.getWidth(), buttonLangsamer.getHeight());
        buttonSchneller.setPosition(buttonLangsamer.getX(), h / 5.3f);
        buttonSchneller.getLabel().setFontScale(buttonLangsamer.getLabel().getFontScaleX());
        buttonSchneller.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Sleep_Time > 30)
                    Sleep_Time -= 30;
            }
        });
        buttonMax = new TextButton("Max Speed", skin);
        buttonMax.setSize(buttonLangsamer.getWidth(), buttonLangsamer.getHeight());
        buttonMax.setPosition(w / 7f, buttonLangsamer.getY());
        buttonMax.getLabel().setFontScale(buttonLangsamer.getLabel().getFontScaleX());
        buttonMax.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sleep_Time = 0;
            }
        });
        buttonreplay = new TextButton(" Replay Best Snake\n(Active: " + replay + ")", skin);
        buttonreplay.setSize(buttonLangsamer.getWidth(), buttonLangsamer.getHeight());
        buttonreplay.setPosition(buttonMax.getX(), buttonSchneller.getY());
        buttonreplay.getLabel().setFontScale(buttonLangsamer.getLabel().getFontScaleX());
        buttonreplay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (replay) {
                    requestReplayStop = true;
                    buttonreplay.setText(" Replay Best Snake\n(Active: false)");
                } else {
                    replay = true;
                    buttonreplay.setText(" Replay Best Snake\n(Active: true)");
                }
                gameOver = true;
                currentSnake = bestSnakeEver.bestSnakeEver;

            }
        });
        buttonshownodes = new TextButton("Show Nodes", skin);
        buttonshownodes.setSize(buttonLangsamer.getWidth(), h / 8);
        buttonshownodes.setPosition(buttonMax.getX(), h / 50f);
        buttonshownodes.getLabel().setFontScale(buttonLangsamer.getLabel().getFontScaleX());
        buttonshownodes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.this.setScreen(NodeVis);
                if (currentScreen) {
                    stage.clear();
                    stage.addActor(buttonGraph);
                    stage.addActor(buttonStart);
                    stage.addActor(savedSnakesScreenbutton);
                    stage.addActor(buttonshownodes);
                    stage.addActor(buttonLangsamer);
                    stage.addActor(buttonSchneller);
                    stage.addActor(buttonMax);
                    stage.addActor(buttonreplay);
                } else {
                    stage.clear();
                    stage.addActor(buttonStart);
                    stage.addActor(savedSnakesScreenbutton);
                    stage.addActor(buttonshownodes);
                }
                currentScreen = !currentScreen;
            }
        });
        savedSnakesScreenbutton = new TextButton("Show Saved Snakes", skin);
        savedSnakesScreenbutton.setSize(buttonLangsamer.getWidth(), h / 8);
        savedSnakesScreenbutton.setPosition(buttonLangsamer.getX(), buttonshownodes.getY());
        savedSnakesScreenbutton.getLabel().setFontScale(buttonLangsamer.getLabel().getFontScaleX());
        savedSnakesScreenbutton.addListener(new ClickListener() {
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
                    stage.addActor(savedSnakesScreenbutton);
                    stage.addActor(buttonshownodes);
                    stage.addActor(buttonLangsamer);
                    stage.addActor(buttonSchneller);
                    stage.addActor(buttonMax);
                    stage.addActor(buttonreplay);
                }
            }
        });

        stage.addActor(buttonGraph);
        stage.addActor(buttonStart);
        stage.addActor(savedSnakesScreenbutton);
        stage.addActor(buttonshownodes);
        stage.addActor(buttonLangsamer);
        stage.addActor(buttonSchneller);
        stage.addActor(buttonMax);
        stage.addActor(buttonreplay);

        currentSnake = new Snakes();
        for (int i = 0; i < bestSnakesArraySize; i++) {
            bestSnakesArray.add(currentSnake);
        }

        allSnakes allSnakes = new allSnakes();
        allSnakesArrays.add(allSnakes);
        allSnakes.allSnakesArray.add(currentSnake);

        bestSnakeEver.bestSnakeEver = currentSnake;

        NodeVis = new NodeVis();
        SavedSnakes = new SavedSnakes(this);
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

        if (prefs.getString("time").equals("")) {
            prefs.putString("time", String.valueOf(Gdx.graphics.getDeltaTime()));
        } else {
            prefs.putString("time", String.valueOf(Double.parseDouble(prefs.getString("time")) + Gdx.graphics.getDeltaTime()));
        }
        prefs.flush();

        drawGame();

        if (this.getScreen() != SavedSnakes) {
            if (!currentScreen) {
                drawFonts();
                drawGraph();
            }
            stage.draw();
        }
        batch.begin();
        font.draw(batch, "Paused: " + freeze, w / 1.12f, h / 1.075f);
        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public void ResetLayers() {
        for (int i = 0; i < currentSnake.layerArray.size; i++) {
            for (int j = 0; j < currentSnake.layerArray.get(i).NodeArray.size; j++) {
                currentSnake.layerArray.get(i).NodeArray.get(j).value = 0;
            }
        }
    }

    public void berechneLayer() {
        ResetLayers();
        InputLayerDetection newDetection = new InputLayerDetection();
        for (int i = 0; i < LayerMenge - 1; i++) {
            berechneLayer(i);
        }

        for (int Layernumber = 0; Layernumber < LayerMenge; Layernumber++) {
            //Logging
            for (int k = 0; k < currentSnake.layerArray.get(Layernumber).NodeArray.size; k++) {
                if (enableNodeLogging) {
                    System.out.println("Layer NR.: " + Layernumber + " Node NR.: " + k + " ERGEBNIS: " + currentSnake.layerArray.get(Layernumber).NodeArray.get(k).value);
                }
            }
            if (enableNodeLogging) {
                System.out.println("\n");
            }
        }
    }

    public void berechneLayer(int Layernumber) {
        for (int NodeLayer2 = 0; NodeLayer2 < currentSnake.layerArray.get(Layernumber + 1).NodeArray.size; NodeLayer2++) {
            double sum = 0;
            for (int NodeLayer1 = 0; NodeLayer1 < currentSnake.layerArray.get(Layernumber).NodeArray.size; NodeLayer1++) {
                //weigth
                double weigth = currentSnake.layerArray.get(Layernumber).NodeArray.get(NodeLayer1).WeigthArray.get(NodeLayer2);
                //value
                double value = currentSnake.layerArray.get(Layernumber).NodeArray.get(NodeLayer1).value;
                sum += weigth * value;
            }

            //Node deren Value geschrieben werden soll
            if (Layernumber == LayerMenge - 2)
                currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(NodeLayer2).value = outputActivationFunction(sum);
            else
                currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(NodeLayer2).value = activationFunction(sum);

        }
    }

    public double activationFunction(double x) {
        x += bias;

        //Sigmoid
        return 1 / (1 + Math.exp(-x));

        //Tanh
        //return Math.tanh(x);

        //Relu
        //return Math.max(0, x);

        //Relu 2.0 (Best)
        //if (x > 0) {
        //    return 1;
        //} else {
        //    return 0;
        //}
    }

    public double outputActivationFunction(double x) {
        x += biasOutput;

        //Sigmoid
        //return 1 / (1 + Math.exp(-x));

        //Tanh (Best)
        //return Math.tanh(x);

        //Relu
        return Math.max(0, x);

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
            tmpArray = new Array<>(hiscoreArray);
        } else {
            tmpArray = new Array<>(averageFitnessArray);
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

                    font.draw(batch, "" + i, w / 80, ((h / 1.08f - h / 1.75f) / tmpArray.get(highest)) * i + h / 1.75f);
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
                , buttonshownodes.getY() - w / 100
                , w / 5.4f + (w / 100) * 2
                , (h / 2f - w / 100 + w / 100 + h / 2.23f) - buttonshownodes.getY() + (w / 100) * 2);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(w / 3.6f + (w / 4.7f - w / 5.4f) / 2
                , buttonshownodes.getY()
                , w / 5.4f
                , (h / 2f - w / 100 + w / 100 + h / 2.23f) - buttonshownodes.getY());
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "Settings", w / 3.35f, h / 1.07f);
        font.draw(batch, "______________________", w / 3.35f, h / 1.08f);
        font.draw(batch, "Game Nr: " + gameNr, w / 3.35f, h / 1.13f);
        font.draw(batch, "Snake Nr: " + snakeNr, w / 3.35f, h / 1.18f);
        font.draw(batch, "Score: " + score, w / 3.35f, h / 1.23f);
        font.draw(batch, "Population: " + population, w / 3.35f, h / 1.28f);
        font.setColor(1f, 0.3f, 0.3f, 1);
        int maxLength = (reihen - 2) * (spalten - 2) - startlength;
        font.draw(batch, "Highscore: " + hiScore + " (Max: " + maxLength + ")", w / 3.35f, h / 1.34f);
        font.draw(batch, "Best Fitness Ever: " + bestSnakeEver.bestSnakeEver.fitness, w / 3.35f, h / 1.4f);
        font.setColor(Color.WHITE);
        font.draw(batch, "______________________", w / 3.35f, h / 1.45f);
        font.draw(batch, "Bias: " + (float) bias, w / 3.35f, h / 1.55f);
        font.draw(batch, "Output Bias: " + (float) biasOutput, w / 3.35f, h / 1.63f);
        font.draw(batch, "Input Layer Nodes: " + inputLayerNodes, w / 3.35f, h / 1.72f);
        font.draw(batch, "Layer 2 Nodes: " + Layer2Nodes, w / 3.35f, h / 1.82f);
        font.draw(batch, "Layer 3 Nodes: " + Layer3Nodes, w / 3.35f, h / 1.93f);
        font.draw(batch, "Layer 4 Nodes: " + Layer4Nodes, w / 3.35f, h / 2.05f);
        font.draw(batch, "Output Layer Nodes: " + outputLayerNodes, w / 3.35f, h / 2.18f);
        font.draw(batch, "______________________", w / 3.35f, h / 2.34f);
        font.draw(batch, "Mutation Probability: " + (float) mutationPropability, w / 3.35f, h / 2.55f);
        font.draw(batch, "Mutation Min: " + (float) mutationMin, w / 3.35f, h / 2.8f);
        font.draw(batch, "Mutation Max: " + (float) mutationMax, w / 3.35f, h / 3.1f);
        font.draw(batch, "Population Size: " + POPULATIONSIZE, w / 3.35f, h / 3.4f);
        font.draw(batch, "First Population Size: " + FIRSTPOPULATIONSIZE, w / 3.35f, h / 3.8f);
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
        for (int i = 0; i < reihen; i++) {
            for (int j = 0; j < spalten; j++) {
                if (i == 0 || j == 0 || i == reihen - 1 || j == spalten - 1) {
                    shapeRenderer.setColor(Color.GRAY);
                } else {
                    if ((i + j) % 2 == 0)
                        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
                    else
                        shapeRenderer.setColor(0.25f, 0.25f, 0.25f, 1);
                }
                shapeRenderer.rect(w / 2 + i * w / 2 / reihen
                        , j * h / spalten
                        , w / 2 / reihen
                        , h / spalten);
            }
        }
        //Treats
        if (Snake.treats != null && Snake.treats.size > 0) {
            try {
                shapeRenderer.setColor(1, 1, 1, 1);
                shapeRenderer.rect(Snake.treats.get(Snake.treats.size - 1).x * w / 2 / reihen + w / 2
                        , (spalten - 1 - Snake.treats.get(Snake.treats.size - 1).y) * h / spalten
                        , w / 2 / reihen
                        , h / spalten);
            } catch (Exception ignored) {

            }
        }
        //Schlange
        try {
            if (Snake.snake != null && Snake.snake.size() > 0) {
                for (int i = 0; i < Snake.snake.size(); i++) {
                    try {
                        shapeRenderer.setColor(0.3f, 0.7f, 0.3f, 1);
                        shapeRenderer.rect(Snake.snake.get(i).x * w / 2 / reihen + w / 2
                                , (spalten - 1 - Snake.snake.get(i).y) * h / spalten
                                , w / 2 / reihen
                                , h / spalten);
                        if (i == 0) {
                            shapeRenderer.setColor(1f, 0.3f, 0.3f, 1);
                            shapeRenderer.rect(Snake.snake.get(i).x * w / 2 / reihen + w / 2
                                    , (spalten - 1 - Snake.snake.get(i).y) * h / spalten
                                    , w / 2 / reihen
                                    , h / spalten);
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        } catch (Exception ignored) {

        }
        shapeRenderer.end();
    }
}
