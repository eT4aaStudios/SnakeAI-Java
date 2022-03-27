package com.snake.ai;

import static com.badlogic.gdx.Gdx.gl;
import static com.snake.ai.SnakeGame.energy;
import static com.snake.ai.main.batch;
import static com.snake.ai.main.bestSnakes;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gson;
import static com.snake.ai.main.h;
import static com.snake.ai.main.loadingSavedGame;
import static com.snake.ai.main.maxSpeedButton;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.setupLayerNodeArray;
import static com.snake.ai.main.shapeRenderer;
import static com.snake.ai.main.showSavedInstancesButton;
import static com.snake.ai.main.snakeGameInstance;
import static com.snake.ai.main.startTheGameButton;
import static com.snake.ai.main.w;

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

import java.util.Properties;

public class SavedSnakes extends SnakeScreen implements Screen {
    public static Preferences prefs;

    Stage savedStage;

    private static Table weckerscrollTable;
    private static Table weckerselectionContainer;
    public static ScrollPane weckerscrollPane;
    private static TextButton.TextButtonStyle buttonSelected;
    private static Button loadButton, deleteButton, saveButton;
    public static Skin skin2;
    public static TextureAtlas atlas;
    public static BitmapFont pixel10;

    Skin skin;
    FileHandle file;
    private static Properties properties;
    main main2;

    public SavedSnakes(main main) {
        prefs = Gdx.app.getPreferences("SnakeAiVersion2");
        this.main2 = main;

        //file = Gdx.files.internal("BestSnake.txt");
        //&properties = null;
        //&properties = new Properties();

        //InputStream in = null;
        //try {
        //    in = new BufferedInputStream(file.read());
        //    properties.loadFromXML(in);
        //} catch (Throwable t) {
        //    t.printStackTrace();
        //} finally {
        //    StreamUtils.closeQuietly(in);
        //}
        //file = null;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        savedStage = new Stage();
        Gdx.input.setInputProcessor(savedStage);
        prefs = Gdx.app.getPreferences("SnakeAiVersion2");

        saveButton = new TextButton("Save Current SnakeGame", skin);
        saveButton.setSize(showSavedInstancesButton.getWidth(), showSavedInstancesButton.getHeight());
        saveButton.setPosition(maxSpeedButton.getX(), showSavedInstancesButton.getY());
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveAsJson(snakeGameInstance, bestSnakes);
                loadSavedSnakeScrollPane();
            }
        });
        savedStage.addActor(saveButton);
        savedStage.addActor(startTheGameButton);
        savedStage.addActor(showSavedInstancesButton);

        loadSavedSnakeScrollPane();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.8f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        savedStage.act(Gdx.graphics.getDeltaTime());

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(w / 2, h, w / 2, 0);
        shapeRenderer.end();

        drawGame();

        savedStage.draw();
        batch.begin();

        batch.end();
    }

    public static void saveAsJson(SnakeGameInstance snakeGameInstance, Array<Snake> bestSnakes) {
        int activeGames = prefs.getInteger("ActiveGames") + 1;
        int totalNumberOfGames = prefs.getInteger("TotalNumberOfGames") + 1;

            System.out.println(settings.reihen);

        String json1 = gson.toJson(snakeGameInstance);
        BestSnakes bestSnakesClass = new BestSnakes();
        bestSnakesClass.setBestSnakes(bestSnakes);
        String json2 = gson.toJson(settings);
        String json3 = gson.toJson(bestSnakesClass);
        //FileUtils.writeStringToFile(new File("SnakeGame:" + totalNumberOfGames + ".txt"), json);

        prefs.putString("(SnakeGameInstance) SnakeGame:" + totalNumberOfGames, json1);
        prefs.putString("(Settings) SnakeGame:" + totalNumberOfGames, json2);
        prefs.putString("(BestSnakeArray) SnakeGame:" + totalNumberOfGames, json3);

        prefs.putInteger("ActiveGames", activeGames);
        prefs.putInteger("TotalNumberOfGames", totalNumberOfGames);
        prefs.flush();
    }

    public static SnakeGameInstance loadAJson(int id) {
        String dataFromPrefs = prefs.getString("(SnakeGameInstance) SnakeGame:" + id);
        return gson.fromJson(dataFromPrefs, SnakeGameInstance.class);
    }

    public void deleteAJson(int id) {
        freeze = true;
        prefs.remove("(SnakeGameInstance) SnakeGame:" + id);
        prefs.remove("(BestSnakeArray) SnakeGame:" + id);
        prefs.putInteger("ActiveGames", prefs.getInteger("ActiveGames") - 1);
        prefs.flush();
        freeze = false;
    }

    public void loadSavedSnakeScrollPane() {
        try {
            float position = weckerscrollPane.getScrollY();
            weckerscrollPane.clear();
            weckerscrollPane.setScrollY(position);
            weckerscrollPane.updateVisualScroll();
        } catch (Exception ignored) {

        }

        atlas = new TextureAtlas("scrollpane/textures.atlas");
        pixel10 = new BitmapFont(Gdx.files.internal("scrollpane/pixel.fnt"), atlas.findRegion("pixel"), false);
        skin2 = new Skin(atlas);
        skin2.add("default-font", pixel10);
        skin2.load(Gdx.files.internal("scrollpane//ui.json"));

        buttonSelected = new TextButton.TextButtonStyle();
        buttonSelected.up = new TextureRegionDrawable(skin2.getRegion("default-round-down"));

        weckerscrollTable = new Table();
        weckerscrollTable.setFillParent(true);
        savedStage.addActor(weckerscrollTable);

        weckerselectionContainer = new Table();

        int activeGames = prefs.getInteger("ActiveGames");
        int totalNumberOfGames = prefs.getInteger("TotalNumberOfGames");

        int counter = 0;
        for (int i = 0; i < activeGames; i++) {
            final int finalI = i;
            Group g = new Group();


            SnakeGameInstance snakeGameInstance = null;
            for(int j = counter;j < totalNumberOfGames + 1;j++) {
                if (prefs.contains("(SnakeGameInstance) SnakeGame:"+j)) {
                    snakeGameInstance = loadAJson(j);
                    counter = j + 1;
                    break;
                }
            }

            final SnakeGameInstance snakeGameInstanceFinal = snakeGameInstance;
            int finalCounter = counter - 1;


            loadButton = new TextButton("Load Game Nr.: " + i +
                    //"\nBest Fitness Ever: " + fitnessDoubleToE(snakeGameInstance.bestSnake.fitness) +
                    "\nHighScore: " + snakeGameInstance.bestSnake.score +
                    "\nPopulation: " + snakeGameInstance.population, skin);

            loadButton.setSize(w / 2 / 2f, h / 5);
            loadButton.setPosition(w / 2 / -6f, 0);
            loadButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    freeze = true;
                    loadingSavedGame = true;
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    main.snakeGameInstance = snakeGameInstanceFinal;
                    String dataFromPrefs1 = prefs.getString("(Settings) SnakeGame:" + (finalCounter));
                    String dataFromPrefs2 = prefs.getString("(BestSnakeArray) SnakeGame:" + (finalCounter));
                    settings = gson.fromJson(dataFromPrefs1, Settings.class);
                    settings.mutationMax = 0.025;
                    settings.mutationProbability = 5;
                    setupLayerNodeArray();
                    bestSnakes.clear();
                    bestSnakes = gson.fromJson(dataFromPrefs2, BestSnakes.class).getBestSnakes();
                    //Make a new Snake
                    energy = -1;
                    main.gameNr = finalI;
                    loadingSavedGame = false;
                    freeze = false;
                }
            });
            g.addActor(loadButton);

            deleteButton = new TextButton("Delete", skin);
            deleteButton.setSize(w / 2 / 6.5f, h / 5);
            deleteButton.setPosition(w / 2 / 2.7f, 0);
            deleteButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Deleted Game Nr.: " + (finalCounter));
                    deleteAJson(finalCounter);
                    loadSavedSnakeScrollPane();
                }
            });
            //if (finalI > 0)
                g.addActor(deleteButton);

            weckerselectionContainer.add(g).padBottom(4).size(w / 2 / 3.2f, h / 5f);
            weckerselectionContainer.row();
        }
        weckerselectionContainer.pack();
        weckerselectionContainer.setTransform(false);
        weckerselectionContainer.setOrigin(weckerselectionContainer.getWidth() / 1f, weckerselectionContainer.getHeight() / 40);

        weckerscrollPane = new ScrollPane(weckerselectionContainer, skin2);
        weckerscrollPane.setScrollingDisabled(true, false);
        weckerscrollPane.layout();
        weckerscrollTable.add(weckerscrollPane).size(w / 2, h / 1.3f).fill();
        weckerscrollTable.setPosition(w / 4 - w / 2, h / 10 - weckerscrollTable.getHeight() / 1.3f);
        weckerscrollPane.updateVisualScroll();

        Gdx.input.setInputProcessor(savedStage);
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

class BestSnakes {
    private Array<Snake> bestSnakes;

    public Array<Snake> getBestSnakes() {
        return bestSnakes;
    }

    public void setBestSnakes(Array<Snake> bestSnakes) {
        this.bestSnakes = bestSnakes;
    }
}