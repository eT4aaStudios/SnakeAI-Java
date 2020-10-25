package com.snake.ai;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import static com.snake.ai.Snake.nCols;
import static com.snake.ai.Snake.nRows;

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
    TextButton buttonstart;
    TextButton buttonstop;
    TextButton buttonmax;
    static TextButton buttonfreeze;
    TextButton buttonshownodes;
    static TextButton savedSnakesScreenbutton;
    TextButton buttonreplay;
    Skin skin;
    public static float w;
    public static float h;
    public static int foodpositionX, foodpositionY;
    public static double SnakeHeadX, SnakeHeadY;
    Array<Integer> felderarray;
    public static boolean freeze;
    public static Snakes currentSnake;
    public static Array<Integer> layerNodeValueArray;
    public static boolean currentScreen;
    public static Array<allSnakes> allSnakesArrays;
    public static Array<Snakes> bestSnakesArray;
    public static boolean loadFromSavedSnake;
    public static int gameNr;
    public static int SnakeNr;
    public static boolean replay;
    public static BestSnakeEver bestSnakeEver = new BestSnakeEver();

    //Neuronales Netzwerk Eigenschaften
    public static double bias = 0d;
    public static double biasOutput = -0.4d;

    public static double mutationPropability = 5;//%
    public static double mutationMin = -0.5f;
    public static double mutationMax = 0.5f;
    public static int bestSnakesArraySize = 4;

    //Neuronales Netzwerk Aussehen
    static int inputLayerNodes = 24;
    static int Layer2Nodes = 20;
    static int Layer3Nodes = 12;
    static int Layer4Nodes = 0;
    static int outputLayerNodes = 4;
    static int LayerMenge = 4;

    //Debugging Eigenschaften
    public static boolean enableNodeLogging = false;
    public static boolean enableSehrNahLogging = false;
    public static boolean enableOutputLayerLogging = false;
    public static boolean enableInputLayerLogging = true;

    //Evolutions Eigenschaften
    public static int POPULATIONSIZE = 500;
    public static int FIRSTPOPULATIONSIZE = 500;

    public static int reihen = nCols;
    public static int spalten = nRows;

    Snake snake = new Snake();

    NodeVis NodeVis;
    SavedSnakes SavedSnakes;

    @Override
    public void create() {
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
        Snake.main2();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        felderarray = new Array<>();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        buttonstart = new TextButton("Langsamer", skin);
        buttonstart.setSize(w / 4, h / 4);
        buttonstart.setPosition(w / 6, h / 1.6f);
        buttonstart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Snake.Sleep_Time += 40;
            }
        });
        buttonstop = new TextButton("Schneller", skin);
        buttonstop.setSize(w / 4, h / 4);
        buttonstop.setPosition(buttonstart.getX(), h / 4f);
        buttonstop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Snake.Sleep_Time > 30)
                    Snake.Sleep_Time -= 30;
            }
        });
        buttonmax = new TextButton("Max Speed", skin);
        buttonmax.setSize(w / 4, h / 4);
        buttonmax.setPosition(w - buttonstop.getWidth() * 2, h / 4f);
        buttonmax.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Snake.Sleep_Time = 1;
            }
        });
        buttonfreeze = new TextButton("Freeze", skin);
        buttonfreeze.setSize(w / 4, h / 8);
        buttonfreeze.setPosition(((w - buttonstop.getWidth() * 3) / 4) * 3 + buttonfreeze.getWidth() * 2, h / 20f);
        buttonfreeze.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                freeze = !freeze;
            }
        });
        buttonreplay = new TextButton("Replay Best Snake", skin);
        buttonreplay.setSize(w / 4, h / 4);
        buttonreplay.setPosition(w - buttonstop.getWidth() * 2, h / 1.6f);
        buttonreplay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Replay");
                replay = true;
                Snake.gameOver = true;
                currentSnake = bestSnakeEver.bestSnakeEver;
            }
        });
        buttonshownodes = new TextButton("Show Nodes", skin);
        buttonshownodes.setSize(w / 4, h / 8);
        buttonshownodes.setPosition(((w - buttonstop.getWidth() * 3) / 4) * 2 + buttonshownodes.getWidth(), h / 20f);
        buttonshownodes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.this.setScreen(NodeVis);
                if (currentScreen) {
                    stage.clear();
                    stage.addActor(buttonshownodes);
                    stage.addActor(buttonstart);
                    stage.addActor(buttonstop);
                    stage.addActor(buttonmax);
                    stage.addActor(buttonfreeze);
                    stage.addActor(buttonreplay);
                    stage.addActor(savedSnakesScreenbutton);
                } else {
                    stage.clear();
                    stage.addActor(savedSnakesScreenbutton);
                    stage.addActor(buttonfreeze);
                    stage.addActor(buttonshownodes);
                }
                currentScreen = !currentScreen;
            }
        });
        savedSnakesScreenbutton = new TextButton("Show Saved Snakes", skin);
        savedSnakesScreenbutton.setSize(w / 4, h / 8);
        savedSnakesScreenbutton.setPosition((w - savedSnakesScreenbutton.getWidth() * 3) / 4, h / 20f);
        savedSnakesScreenbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (main.this.getScreen() != SavedSnakes)
                    main.this.setScreen(SavedSnakes);
                else {
                    main.this.setScreen(NodeVis);
                    Gdx.input.setInputProcessor(stage);
                    stage.addActor(buttonfreeze);
                    stage.addActor(savedSnakesScreenbutton);
                }
            }
        });

        stage.addActor(savedSnakesScreenbutton);
        stage.addActor(buttonshownodes);
        stage.addActor(buttonstart);
        stage.addActor(buttonstop);
        stage.addActor(buttonmax);
        stage.addActor(buttonfreeze);
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
        SavedSnakes = new SavedSnakes();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.4f, 0.7f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();

        if (this.getScreen() != SavedSnakes)
            stage.draw();
    }


    @Override
    public void dispose() {
        batch.dispose();
    }

    public void ResetLayers() {
        for (int i = 0; i < currentSnake.layerArray.size; i++) {
            for (int j = 0; j < currentSnake.layerArray.get(i).NodeArray.size; j++) {
                currentSnake.layerArray.get(i).NodeArray.get(j).value = 0d;
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
            double sum = 0d;
            for (int NodeLayer1 = 0; NodeLayer1 < currentSnake.layerArray.get(Layernumber).NodeArray.size; NodeLayer1++) {
                //weigth
                double weigth = currentSnake.layerArray.get(Layernumber).NodeArray.get(NodeLayer1).WeigthArray.get(NodeLayer2);
                //value
                double value = currentSnake.layerArray.get(Layernumber).NodeArray.get(NodeLayer1).value;
                sum += weigth * value;
            }

            //Node Deren Value geschrieben werden soll
            if (Layernumber == LayerMenge - 2) {
                currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(NodeLayer2).value = outputActivationFunction(sum);
            } else
                currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(NodeLayer2).value = activationFunction(sum);
        }
    }

    public double activationFunction(double x) {
        x += bias;

        //Sigmoid
        return 1d / (1d + Math.exp(-x));

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
        return Math.max(0d, x);

        //Relu 2.0
        //if (x > 0) {
        //    return 1;
        //} else {
        //    return 0;
        //}
    }
}
