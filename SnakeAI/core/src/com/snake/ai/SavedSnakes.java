package com.snake.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

import static com.badlogic.gdx.Gdx.gl;
import static com.snake.ai.Snake.hiScore;
import static com.snake.ai.Snake.population;
import static com.snake.ai.Snake.snakeNr;
import static com.snake.ai.main.POPULATIONSIZE;
import static com.snake.ai.main.allSnakesArrays;
import static com.snake.ai.main.batch;
import static com.snake.ai.main.bestSnakeEver;
import static com.snake.ai.main.bestSnakesArray;
import static com.snake.ai.main.bestSnakesArraySize;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.h;
import static com.snake.ai.main.loadFromSavedSnake;
import static com.snake.ai.main.w;

public class SavedSnakes implements Screen {
    public static Preferences prefs;

    Stage savedStage;

    private static Table WeckerscrollTable;
    private static Table WeckerselectionContainer;
    public static ScrollPane WeckerscrollPane;
    private static TextButton.TextButtonStyle buttonSelected;
    private static Button loadSavedSnake;
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
        prefs.clear();
        prefs.flush();

        saveCurrentSnake = new TextButton("Save Current Snake", skin);
        saveCurrentSnake.setSize(w / 4, h / 8);
        saveCurrentSnake.setPosition(((w - saveCurrentSnake.getWidth() * 2) / 4) * 2 + saveCurrentSnake.getWidth(), h / 20f);
        saveCurrentSnake.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveCurrentSnake();
            }
        });
        savedStage.addActor(saveCurrentSnake);
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


        for (int i = 0; i < prefs.getInteger("GameMenge"); i++) {
            Group g = new Group();
            loadSavedSnake = new TextButton("Load Game Nr.: " + i +
                    "\nAverage Fitness: " + prefs.getFloat("gameNr " + i + "average Fitness") +
                    "\nBest Fitness Ever: " + prefs.getFloat("gameNr " + i + "bestSnakeEver.fitness") +
                    "\nHighScore: " + prefs.getInteger("gameNr " + i + "hiScore") +
                    "\nPoulation: " + prefs.getInteger("gameNr " + i + "population"), skin);
            loadSavedSnake.setSize(w / 1.3f, h / 8);
            loadSavedSnake.setPosition(w / -4.5f, 0);
            final int finalI = i + 1;
            loadSavedSnake.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("loadSavedSnake");
                    loadBestArraySnake(finalI);
                }
            });
            g.addActor(loadSavedSnake);

            WeckerselectionContainer.add(g).padBottom(4).size(w / 3.2f, h / 6.5f);
            WeckerselectionContainer.row();
        }
        WeckerselectionContainer.pack();
        WeckerselectionContainer.setTransform(false);
        WeckerselectionContainer.setOrigin(WeckerselectionContainer.getWidth() / 1f, WeckerselectionContainer.getHeight() / 40);

        WeckerscrollPane = new ScrollPane(WeckerselectionContainer, skin2);
        WeckerscrollPane.setScrollingDisabled(true, false);
        WeckerscrollPane.layout();
        WeckerscrollTable.add(WeckerscrollPane).size(w / 1f, h / 1.3f).fill();
        WeckerscrollTable.setPosition(0, h / 10 - WeckerscrollTable.getHeight() / 1.3f);
        WeckerscrollPane.setScrollY(position);
        WeckerscrollPane.updateVisualScroll();

        Gdx.input.setInputProcessor(savedStage);
    }

    public void saveCurrentSnake() {
        prefs.putInteger("GameMenge", prefs.getInteger("GameMenge") + 1);

        int gameNr = prefs.getInteger("GameMenge");
        saveEinstellungen(gameNr);

        int maxFitness = 0;
        for (int m = 0; m < allSnakesArrays.get(0).allSnakesArray.size; m++) {
            maxFitness += allSnakesArrays.get(0).allSnakesArray.get(m).fitness;
        }
        prefs.putFloat("gameNr " + gameNr + "average Fitness", maxFitness / POPULATIONSIZE);
        prefs.putInteger("gameNr " + gameNr + "hiScore", hiScore);
        prefs.putInteger("gameNr " + gameNr + "bestSnakeEver.fitness", bestSnakeEver.bestSnakeEver.fitness);

        prefs.putInteger("gameNr " + gameNr + "population", population);

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
        loadSavedSnakeScrollPane();
    }

    public void saveEinstellungen(int gameNr) {
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
        loadFromSavedSnake = true;
        loadEinstellungen(gameNr);
        main.gameNr = gameNr;

        bestSnakesArray.clear();
        for (int i = 0; i < bestSnakesArraySize; i++) {
            main.SnakeNr = i;
            currentSnake = new Snakes();
            bestSnakesArray.add(currentSnake);
        }
        allSnakes allSnakes = new allSnakes();
        allSnakesArrays.add(allSnakes);
        allSnakes.allSnakesArray.add(currentSnake);

        snakeNr = 0;
        Snake.gameOver = true;
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


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.7f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        savedStage.act(Gdx.graphics.getDeltaTime());

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
