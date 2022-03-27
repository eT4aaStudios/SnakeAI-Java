package com.snake.ai;

import static com.snake.ai.SettingsScreen.resetTextFieldText;
import static com.snake.ai.SnakeGame.gameOver;
import static com.snake.ai.SnakeGame.sleepTime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    static Button startTheGameButton;
    static Button switchGraphButton;
    static TextButton slowerButton;
    static TextButton fasterButton;
    static TextButton maxSpeedButton;
    static TextButton showNeuralNetworkButton;
    static TextButton showSavedInstancesButton;
    static TextButton replayBestSnakeButton;
    static TextButton showSettingsButton;
    static TextButton helpButton;
    static TextButton scoreboardButton;
    static Skin skin;
    public static float w;
    public static float h;
    public static int foodPositionX, foodPositionY;
    public static int snakeHeadX, snakeHeadY;
    Array<Integer> fieldArray;
    public static boolean freeze = true;
    public static Snake currentSnake;
    public static Array<Integer> layerNodeValueArray;
    public static Array<Snake> snakeArray;
    static ShapeRenderer shapeRenderer;
    public static BitmapFont font;
    public static GlyphLayout layout = new GlyphLayout();
    public static boolean graphmode1;
    public static int populationsSinceLastSave;
    public static Random r = ThreadLocalRandom.current();
    public static Evolution evo;
    InputLayerDetection newDetection;
    public static int averageSteps;
    public static int gameNr = -1;
    public static int SnakeNr;
    public static boolean replay, requestReplayStop;
    public static Array<Snake> bestSnakes = new Array<>();
    public static Gson gson = new Gson();
    public static SnakeGameInstance snakeGameInstance = new SnakeGameInstance();
    SnakeGame snakeGame;
    NeuralNetworkVisualization NeuralNetworkVisualization;
    SavedSnakes SavedSnakes;
    SettingsScreen settingsScreen;
    SnakeScreen snakeScreen;
    public static Settings settings = new Settings();
    public static boolean loadingSavedGame;

    @Override
    public void create() {
        setupLayerNodeArray();
        snakeGame = new SnakeGame(this);
        for (int i = 0; i < layerNodeValueArray.size; i++) {
            if (layerNodeValueArray.get(i) == 0) {
                layerNodeValueArray.removeIndex(i);
            }
        }
        System.out.println("Trainable Weights: " + (settings.inputLayerNodes * settings.layer2Nodes + settings.layer2Nodes * settings.layer3Nodes + settings.layer3Nodes * settings.layer4Nodes + settings.layer4Nodes * settings.outputLayerNodes));
        System.out.println("Trainable Biases: " + (settings.inputLayerNodes + settings.layer2Nodes + settings.layer3Nodes + settings.layer4Nodes));
        System.out.println("Trainable Parameters Sum: " + (settings.inputLayerNodes * settings.layer2Nodes + settings.layer2Nodes * settings.layer3Nodes + settings.layer3Nodes * settings.layer4Nodes + settings.layer4Nodes * settings.outputLayerNodes +
                settings.inputLayerNodes + settings.layer2Nodes + settings.layer3Nodes + settings.layer4Nodes));
        System.out.println("Mutated Parameters expected: " + (settings.inputLayerNodes * settings.layer2Nodes + settings.layer2Nodes * settings.layer3Nodes + settings.layer3Nodes * settings.layer4Nodes + settings.layer4Nodes * settings.outputLayerNodes +
                settings.inputLayerNodes + settings.layer2Nodes + settings.layer3Nodes + settings.layer4Nodes) * (settings.mutationProbability / 100));

        snakeArray = new Array<>();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        fieldArray = new Array<>();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(w / 1100);
        evo = new Evolution();

        startTheGameButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("transparent2.png"))));
        startTheGameButton.setSize(w / 2, h);
        startTheGameButton.setPosition(w / 2, 0);
        startTheGameButton.addListener(new ClickListener() {
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
        switchGraphButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("transparent2.png"))));
        switchGraphButton.setSize(w / 3.9f, h / 2.23f);
        switchGraphButton.setPosition(w / 100, h / 2f);
        switchGraphButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                graphmode1 = !graphmode1;
            }
        });
        slowerButton = new TextButton("Slower", skin);
        slowerButton.setSize(w / 12, h / 8);
        slowerButton.setPosition(getButtonXPosition(0),getButtonYPosition(2));
        slowerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sleepTime += 40;
            }
        });
        fasterButton = new TextButton("Faster", skin);
        fasterButton.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        fasterButton.setPosition(getButtonXPosition(1),getButtonYPosition(2));
        fasterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (sleepTime > 30)
                    sleepTime -= 30;
            }
        });
        maxSpeedButton = new TextButton("Max Velocity", skin);
        maxSpeedButton.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        maxSpeedButton.setPosition(getButtonXPosition(2),getButtonYPosition(2));
        maxSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sleepTime = 0;
            }
        });
        replayBestSnakeButton = new TextButton("Replay Best\nSnake", skin);
        replayBestSnakeButton.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        replayBestSnakeButton.setPosition(getButtonXPosition(0),getButtonYPosition(1));
        replayBestSnakeButton.getLabel().setFontScale(w / 1100);
        replayBestSnakeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (replay) {
                    requestReplayStop = true;
                    replayBestSnakeButton.setText(" Replay Best SnakeGame\n(Active: false)");
                } else {
                    replay = true;
                    replayBestSnakeButton.setText(" Replay Best SnakeGame\n(Active: true)");
                }
                gameOver = true;
                currentSnake = snakeGameInstance.bestSnake;

            }
        });
        showNeuralNetworkButton = new TextButton("Show Neural\nNetwork", skin);
        showNeuralNetworkButton.setSize(slowerButton.getWidth(), h / 8);
        showNeuralNetworkButton.setPosition(getButtonXPosition(0),getButtonYPosition(0));
        showNeuralNetworkButton.getLabel().setFontScale(replayBestSnakeButton.getLabel().getFontScaleX());
        showNeuralNetworkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (main.this.getScreen() != NeuralNetworkVisualization) {
                    main.this.setScreen(NeuralNetworkVisualization);
                } else {
                    main.this.setScreen(snakeScreen);
                }
            }
        });
        showSavedInstancesButton = new TextButton("Show Saved\nInstances", skin);
        showSavedInstancesButton.setSize(slowerButton.getWidth(), h / 8);
        showSavedInstancesButton.setPosition(getButtonXPosition(1),getButtonYPosition(0));
        showSavedInstancesButton.getLabel().setFontScale(replayBestSnakeButton.getLabel().getFontScaleX());
        showSavedInstancesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (main.this.getScreen() != SavedSnakes) {
                    main.this.setScreen(SavedSnakes);
                } else {
                    main.this.setScreen(snakeScreen);
                }
            }
        });
        showSettingsButton = new TextButton("Edit Settings", skin);
        showSettingsButton.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        showSettingsButton.setPosition(getButtonXPosition(2),getButtonYPosition(0));
        showSettingsButton.getLabel().setFontScale(w / 1100);
        showSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                freeze = true;
                if (main.this.getScreen() != settingsScreen) {
                    main.this.setScreen(settingsScreen);
                } else {
                    main.this.setScreen(snakeScreen);
                    resetTextFieldText();
                }
            }
        });
        helpButton = new TextButton("Help", skin);
        helpButton.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        helpButton.setPosition(getButtonXPosition(2),getButtonYPosition(1));
        helpButton.getLabel().setFontScale(w / 1100);
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        scoreboardButton = new TextButton("Scoreboard", skin);
        scoreboardButton.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        scoreboardButton.setPosition(getButtonXPosition(1),getButtonYPosition(1));
        scoreboardButton.getLabel().setFontScale(w / 1100);
        scoreboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        stage.addActor(switchGraphButton);
        stage.addActor(startTheGameButton);
        stage.addActor(showSavedInstancesButton);
        stage.addActor(showNeuralNetworkButton);
        stage.addActor(slowerButton);
        stage.addActor(fasterButton);
        stage.addActor(maxSpeedButton);
        stage.addActor(replayBestSnakeButton);
        stage.addActor(showSettingsButton);
        stage.addActor(scoreboardButton);
        stage.addActor(helpButton);

        currentSnake = new Snake();
        for (int i = 0; i < settings.bestSnakesArraySize; i++) {
            bestSnakes.add(currentSnake);
        }
        snakeGameInstance.bestSnake = new Snake();

        NeuralNetworkVisualization = new NeuralNetworkVisualization();
        SavedSnakes = new SavedSnakes(this);
        settingsScreen = new SettingsScreen();
        snakeScreen = new SnakeScreen();
        newDetection = new InputLayerDetection();

        this.setScreen(snakeScreen);
    }

    public static float getButtonXPosition(int numberOfButtonX) {
        return (((w / 100) * 2 + w / 3.9f) - slowerButton.getWidth() * 3) / 4 * (numberOfButtonX + 1) + slowerButton.getWidth() * numberOfButtonX;
    }

    public static float getButtonYPosition(int numberOfButtonY) {
        return ((h / 2f - w / 100) - slowerButton.getHeight() * 3) / 4 * (numberOfButtonY + 1) + slowerButton.getHeight() * numberOfButtonY;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.8f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    public static void setupLayerNodeArray() {
        layerNodeValueArray = new Array<>();
        layerNodeValueArray.add(settings.inputLayerNodes);
        layerNodeValueArray.add(settings.layer2Nodes);
        layerNodeValueArray.add(settings.layer3Nodes);
        if (settings.layer4Nodes != 0)
            layerNodeValueArray.add(settings.layer4Nodes);
        layerNodeValueArray.add(settings.outputLayerNodes);
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
        for (int i = 0; i < settings.layerMenge - 1; i++) {
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
            if (layerNumber == settings.layerMenge - 2)
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

    public static String fitnessDoubleToE(double fitness) {
        DecimalFormat df;
        if (fitness >= 1000)
            df = new DecimalFormat("##0.E0");
        else
            df = new DecimalFormat("##0");
        return df.format(fitness);
    }
}
