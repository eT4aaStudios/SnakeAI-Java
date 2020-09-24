package com.snake.ai;

import com.badlogic.gdx.ApplicationAdapter;
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

public class main extends ApplicationAdapter {
    //Best :

    //bias 0.4
    //biasOutput 0

    //inputLayerNodes = 16;
    //Layer2Nodes = 16;
    //Layer3Nodes = 16;
    //Layer4Nodes = 0;
    //outputLayerNodes = 4;

    SpriteBatch batch;
    Stage stage;
    TextButton buttonstart, buttonstop, buttonmax, buttonfreeze;
    Skin skin;
    float w, h;
    public static int foodpositionX, foodpositionY;
    public static int SnakeHeadX, SnakeHeadY;
    Array<Integer> felderarray;
    public static boolean freeze;
    public static Array<Snakes> bestSnakesArray;
    public static Snakes currentSnake;
    public static Array<Integer> layerNodeValueArray;

    //Neuronales Netzwerk Eigenschaften
    public static final double bias = 0.4d;
    public static final double biasOutput = -0.2d;

    //Evolutions Eigenschaften
    public static int POPULATIONSIZE = 100;
    public static final double mutationMin = -0.01;
    public static final double mutationMax = 0.01;
    public static final int bestSnakesArraySize = 5;

    //Neuronales Netzwerk Aussehen
    final static int inputLayerNodes = 16;
    final static int Layer2Nodes = 22;
    final static int Layer3Nodes = 16;
    final static int Layer4Nodes = 0;
    final static int outputLayerNodes = 4;
    static final int LayerMenge = 4;

    //Debugging Eigenschaften
    public static final boolean enableNodeLogging = false;
    public static final boolean enableSehrNahLogging = false;
    public static final boolean enableOutputLayerLogging = false;
    public static final boolean enableInputLayerLogging = false;

    public static final int reihen = nCols;
    public static final int spalten = nRows;

    Snake snake = new Snake();

    @Override
    public void create() {
        layerNodeValueArray = new Array<>();
        layerNodeValueArray.add(inputLayerNodes);
        layerNodeValueArray.add(Layer2Nodes);
        layerNodeValueArray.add(Layer3Nodes);
        layerNodeValueArray.add(Layer4Nodes);
        layerNodeValueArray.add(outputLayerNodes);
        for(int i = 0;i < layerNodeValueArray.size;i++)
            if(layerNodeValueArray.get(i) == 0)
                layerNodeValueArray.removeIndex(i);
        bestSnakesArray = new Array<>();
        snake.main2();
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
                Snake.Sleep_Time += 20;
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
        buttonfreeze.setSize(w / 4, h / 4);
        buttonfreeze.setPosition(w - buttonstop.getWidth() * 2, h / 1.6f);
        buttonfreeze.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                freeze = !freeze;
            }
        });
        stage.addActor(buttonstart);
        stage.addActor(buttonstop);
        stage.addActor(buttonmax);
        stage.addActor(buttonfreeze);

        //for(int i = 0;i < batchSize;i++) {
        //    Snakes snakes = new Snakes();
        //    bestSnakesArray.add(snakes);
        //}
        currentSnake = new Snakes();
        bestSnakesArray.add(currentSnake);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.7f, 1, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }


    @Override
    public void dispose() {
        batch.dispose();
    }


    public void berechneLayer() {
        InputLayerDetection newDetection = new InputLayerDetection();
        for (int i = 0; i < LayerMenge - 1; i++)
            berechneLayer(i);
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
            //Node Deren Vlaue geschrieben werden soll

            if (Layernumber == LayerMenge - 1) {
                currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(NodeLayer2).value = outputActivationFunction(sum);
                System.out.println(currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(NodeLayer2).value);
            } else
                currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(NodeLayer2).value = activationFunction(sum);
        }


        //Logging
        for (int k = 0; k < currentSnake.layerArray.get(Layernumber + 1).NodeArray.size; k++) {
            if (enableNodeLogging)
                System.out.println("Layer NR.: " + Layernumber + " ERGEBNIS: " + currentSnake.layerArray.get(Layernumber + 1).NodeArray.get(k).value);
        }
        if (enableNodeLogging)
            System.out.println("\n");
    }

    public double activationFunction(double x) {
        x += bias;

        //Sigmoid
        //return 1 / (1 + Math.exp(-x));

        //Tanh
        //return Math.tanh(x);

        //Relu
        return Math.max(0,x);

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
        return 1 / (1 + Math.exp(-x * 3));

        //Tanh (Best)
        //return Math.tanh(x);

        //Relu
        //return Math.max(0,x);

        //Relu 2.0
        //if (x > 0) {
        //    return 1;
        //} else {
        //    return 0;
        //}
    }
}
