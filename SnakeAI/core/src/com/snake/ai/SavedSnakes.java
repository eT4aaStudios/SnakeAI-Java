/*package com.snake.ai;

import static com.badlogic.gdx.Gdx.gl;
import static com.snake.ai.SnakeGame.energy;
import static com.snake.ai.SnakeGame.snake;
import static com.snake.ai.main.androidConnection;
import static com.snake.ai.main.batch;
import static com.snake.ai.main.bestSnakes;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.fieldArray;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gameNr;
import static com.snake.ai.main.getButtonXPosition;
import static com.snake.ai.main.getButtonYPosition;
import static com.snake.ai.main.h;
import static com.snake.ai.main.isThisAndroid;
import static com.snake.ai.main.layerNodeValueArray;
import static com.snake.ai.main.loadingSavedGame;
import static com.snake.ai.main.log;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.setupLayerNodeArray;
import static com.snake.ai.main.shapeRenderer;
import static com.snake.ai.main.showSavedInstancesButton;
import static com.snake.ai.main.snakeArray;
import static com.snake.ai.main.snakeGame;
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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StreamUtils;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

public class SavedSnakes extends SnakeScreen implements Screen {
    public static Preferences prefs;

    Stage savedStage;

    private static Table weckerscrollTable;
    private static Table weckerselectionContainer;
    public static ScrollPane weckerscrollPane;
    private static TextButton.TextButtonStyle buttonSelected;
    private static VisTextButton loadButton, deleteButton, saveOverwriteButton,saveCopyButton;
    public static Skin skin2;
    public static TextureAtlas atlas;
    public static BitmapFont pixel10;
    public static Array<Integer> activeGamesArray = new Array<>();

    Skin skin;
    FileHandle file;
    private static Properties properties;
    main main2;

    public SavedSnakes(main main) {
        prefs = Gdx.app.getPreferences("SnakeAiVersion2");
        this.main2 = main;

        file = Gdx.files.internal("bestSnake.txt");
        properties = new Properties();

        InputStream in = null;
        try {
            in = new BufferedInputStream(file.read());
            properties.loadFromXML(in);
            if(!prefs.contains("ActiveGames")) {
                prefs.clear();
                prefs.flush();
                prefs.putString("(SnakeGameInstance) SnakeGame:" + 1, properties.getProperty("(SnakeGameInstance) SnakeGame:" + 1));
                prefs.putString("(Settings) SnakeGame:" + 1, properties.getProperty("(Settings) SnakeGame:" + 1));
                prefs.putString("(BestSnakeArray) SnakeGame:" + 1, properties.getProperty("(BestSnakeArray) SnakeGame:" + 1));
                System.out.println("Writing from File");

                prefs.putInteger("ActiveGames",1);
                prefs.putInteger("TotalNumberOfGames", 1);
                prefs.flush();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            StreamUtils.closeQuietly(in);
        }
        file = null;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        savedStage = new Stage();
        Gdx.input.setInputProcessor(savedStage);
        prefs = Gdx.app.getPreferences("SnakeAiVersion2");
        saveOverwriteButton = new VisTextButton("Save &\nOverwrite");
        saveOverwriteButton.setSize(showSavedInstancesButton.getWidth(), showSavedInstancesButton.getHeight());
        saveOverwriteButton.setPosition(getButtonXPosition(2), getButtonYPosition(0));
        saveOverwriteButton.getLabel().setFontScale(w / 1100);
        saveOverwriteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveAsJson(snakeGameInstance, bestSnakes);
                loadSavedSnakeScrollPane();
            }
        });
        saveCopyButton = new VisTextButton("Save &\nMake new");
        saveCopyButton.setSize(showSavedInstancesButton.getWidth(), showSavedInstancesButton.getHeight());
        saveCopyButton.setPosition(getButtonXPosition(3), getButtonYPosition(0));
        saveCopyButton.getLabel().setFontScale(w / 1100);
        saveCopyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveAsJson(snakeGameInstance, bestSnakes,true);
                loadSavedSnakeScrollPane();
            }
        });

        savedStage.addActor(saveOverwriteButton);
        savedStage.addActor(saveCopyButton);
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
        saveAsJson(snakeGameInstance,bestSnakes,false);
    }

    public static void saveAsJson(SnakeGameInstance snakeGameInstance, Array<Snake> bestSnakes,boolean makeNew) {
        int activeGames;
        int totalNumberOfGames;
        if (!makeNew && activeGamesArray.contains(snakeGameInstance.gameNr, false)) {
            activeGames = prefs.getInteger("ActiveGames", 1);
            totalNumberOfGames = prefs.getInteger("TotalNumberOfGames");
        } else {
            activeGames = prefs.getInteger("ActiveGames", 1) + 1;
            totalNumberOfGames = prefs.getInteger("TotalNumberOfGames") + 1;
            activeGamesArray.add(activeGames - 1);
        }
        snakeGameInstance.gameNr = totalNumberOfGames;
        main.gameNr = activeGames - 1;

        //String json1 = gson.toJson(snakeGameInstance);
        BestSnakes bestSnakesClass = new BestSnakes();
        bestSnakesClass.setBestSnakes(bestSnakes);
        //String json2 = gson.toJson(settings);
        //String json3 = gson.toJson(bestSnakesClass);
        //FileUtils.writeStringToFile(new File("SnakeGame:" + totalNumberOfGames + ".txt"), json);

        //prefs.putString("(SnakeGameInstance) SnakeGame:" + totalNumberOfGames, json1);
        //prefs.putString("(Settings) SnakeGame:" + totalNumberOfGames, json2);
        //prefs.putString("(BestSnakeArray) SnakeGame:" + totalNumberOfGames, json3);

        prefs.putInteger("ActiveGames", activeGames);
        prefs.putInteger("TotalNumberOfGames", totalNumberOfGames);
        prefs.flush();

        log("Saved!");
    }

    //public static SnakeGameInstance loadAJson(int id) {
    //    String dataFromPrefs = prefs.getString("(SnakeGameInstance) SnakeGame:" + id);
    //    return gson.fromJson(dataFromPrefs, SnakeGameInstance.class);
    //}

    public void deleteAJson(int id, int finalI) {
        prefs.remove("(SnakeGameInstance) SnakeGame:" + id);
        prefs.remove("(BestSnakeArray) SnakeGame:" + id);
        prefs.remove("(Settings) SnakeGame:" + id);

        prefs.putInteger("ActiveGames", prefs.getInteger("ActiveGames", 1) - 1);
        prefs.flush();
        activeGamesArray.removeIndex(finalI);
        log("Deleted!");
    }

    public void loadSavedSnakeScrollPane() {
        try {
            float position = weckerscrollPane.getScrollY();
            weckerscrollPane.clear();
            weckerscrollPane.setScrollY(position);
            weckerscrollPane.updateVisualScroll();
        } catch (Exception e) {

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

        int activeGames = prefs.getInteger("ActiveGames", 1);
        int totalNumberOfGames = prefs.getInteger("TotalNumberOfGames");

        int counter = 0;
        for (int i = 0; i < activeGames; i++) {
            final int finalI = i;
            Group g = new Group();


            SnakeGameInstance snakeGameInstance = null;
            for (int j = counter; j < totalNumberOfGames + 1; j++) {
                if (prefs.contains("(SnakeGameInstance) SnakeGame:" + j)) {
                    //snakeGameInstance = loadAJson(j);
                    counter = j + 1;
                    break;
                }
            }

            final SnakeGameInstance snakeGameInstanceFinal = snakeGameInstance;
            int finalCounter = counter - 1;
            activeGamesArray.add(finalCounter);

            loadButton = new VisTextButton("Load Game Nr.: " + i +
                    "\nHighScore: " + snakeGameInstance.bestSnake.score +
                    "\nPopulation: " + snakeGameInstance.population);

            loadButton.setSize(w / 2 / 2f, h / 5);
            loadButton.setPosition(w / 2 / -6f, 0);
            loadButton.getLabel().setFontScale(w / 1100);
            loadButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    freeze = true;
                    loadingSavedGame = true;
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    main.snakeGameInstance = snakeGameInstanceFinal;
                    main.snakeGameInstance.gameNr = finalCounter;
                    String dataFromPrefs1 = prefs.getString("(Settings) SnakeGame:" + (finalCounter));
                    String dataFromPrefs2 = prefs.getString("(BestSnakeArray) SnakeGame:" + (finalCounter));
                    //settings = gson.fromJson(dataFromPrefs1, Settings.class);
                    setupLayerNodeArray();
                    bestSnakes.clear();
                    //bestSnakes = gson.fromJson(dataFromPrefs2, BestSnakes.class).getBestSnakes();
                    //Make a new Snake
                    energy = -1;
                    main.gameNr = finalI;
                    snakeGame.initGrid();
                    loadingSavedGame = false;
                    freeze = false;
                    log("Loaded!");
                }
            });
            g.addActor(loadButton);

            deleteButton = new VisTextButton("Delete");
            deleteButton.setSize(w / 2 / 6.5f, h / 5);
            deleteButton.setPosition(w / 2 / 2.7f, 0);
            deleteButton.getLabel().setFontScale(w / 1100);
            deleteButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Deleted Game Nr.: " + (finalCounter));
                    deleteAJson(finalCounter, finalI);
                    loadSavedSnakeScrollPane();
                }
            });
            if (finalI > 0)
                g.addActor(deleteButton);

            weckerselectionContainer.add(g).padBottom(4).size(w / 2 / 3.2f, h / 5f);
            weckerselectionContainer.row();
        }
        Group g = new Group();
        VisTextButton newGameButton = new VisTextButton("New Instance");
        newGameButton.setSize(w / 2 / 2f, h / 5);
        newGameButton.setPosition(w / 2 / -6f, 0);
        newGameButton.getLabel().setFontScale(w / 1100);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                freeze = true;
                settings = new Settings();
                setupLayerNodeArray();
                snakeGameInstance = new SnakeGameInstance();
                snakeGame = new SnakeGame(main2);
                //gameThread.stop();

                if (isThisAndroid()) {
                    snakeGame.androidConnection = androidConnection;
                    if (!androidConnection.isMyServiceRunning())
                        androidConnection.startService();
                } else {
                    //gameThread = new Thread(snakeGame);
                    //gameThread.setPriority(Thread.MAX_PRIORITY);
                    //gameThread.start();
                    //gameThread.setName("SnakeAiCalculating");
                }

                for (int i = 0; i < layerNodeValueArray.size; i++) {
                    if (layerNodeValueArray.get(i) == 0) {
                        layerNodeValueArray.removeIndex(i);
                    }
                }

                snakeArray = new Array<>();
                fieldArray = new Array<>();

                currentSnake = new Snake();
                bestSnakes = new Array<>();
                snakeGameInstance.bestSnake = new Snake();
                gameNr = -1;

                snake = new Array<>();
                snakeGame.startNewGame();

                log("New Instance created!");
                log("Edit the settings\nand start calculating!");

            }
        });
        g.addActor(newGameButton);
        weckerselectionContainer.add(g).padBottom(4).size(w / 2 / 3.2f, h / 5f);
        weckerselectionContainer.row();

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
}*/