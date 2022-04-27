package com.snake.ai;

import static com.badlogic.gdx.Gdx.gl;
import static com.snake.ai.main.batch;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.getButtonXPosition;
import static com.snake.ai.main.getButtonYPosition;
import static com.snake.ai.main.h;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.showSettingsButton;
import static com.snake.ai.main.slowerButton;
import static com.snake.ai.main.snakeGame;
import static com.snake.ai.main.w;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class SettingsScreen implements Screen {

    Stage settingsStage = new Stage();
    static Array<VisTextField> textFieldArray = new Array<>();
    Array<VisLabel> labelArray = new Array<>();
    Array<VisLabel> labelArray2 = new Array<>();
    VisTextButton saveButton;

    public SettingsScreen() {
        saveButton = new VisTextButton("Save Settings");
        saveButton.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        saveButton.setPosition(getButtonXPosition(3), getButtonYPosition(0));
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                freeze = true;
                saveSettings();
                snakeGame.initGrid();
                currentSnake = new Snake();
                snakeGame.startNewGame();
                for (int i = 0; i < textFieldArray.size; i++) {
                    settingsStage.unfocusAll();
                }
            }
        });
        settingsStage.addActor(saveButton);

        labelArray.add(createLabel(true, "Fitness Function"));
        labelArray.add(createLabel(true, "Autosave"));
        labelArray.add(createLabel(true, "Best Snakes Array Size"));
        labelArray.add(createLabel(true, "Crossover"));
        labelArray.add(createLabel(true, "Mutation Probability"));
        labelArray.add(createLabel(true, "Mutation Max"));
        labelArray.add(createLabel(true, "Input Layer Nodes"));
        labelArray.add(createLabel(true, "Layer 2 Nodes"));
        labelArray.add(createLabel(true, "Layer 3 Nodes"));
        labelArray.add(createLabel(true, "Layer 4 Nodes"));
        labelArray.add(createLabel(true, "Output Layer Nodes"));
        labelArray.add(createLabel(true, "Number of Layers"));
        labelArray.add(createLabel(true, "Populationsize"));
        labelArray.add(createLabel(true, "Rows"));
        labelArray.add(createLabel(true, "Columns"));
        labelArray.add(createLabel(true, "Max Energy"));
        labelArray.add(createLabel(true, "Start Length"));

        labelArray2.add(createLabel(false, "This formula is used to give a Fitness Score after Snakes death\nGiven Variables:\nScore Steps\nAllowed Operations:\n() + - * / ^ sqrt() sin() cos() tan()"));
        labelArray2.add(createLabel(false, "How much generations until the next autosave will be performed"));
        labelArray2.add(createLabel(false, "From this array new offspring will be created"));
        labelArray2.add(createLabel(false, "When enabled 2 chromosomes of 2 snakes will be used instead of 1"));
        labelArray2.add(createLabel(false, "How often is mutation happening (%)"));
        labelArray2.add(createLabel(false, "The maximum amount that can be changed during a mutation"));
        labelArray2.add(createLabel(false, "How many sensors the snake has"));
        labelArray2.add(createLabel(false, "How many nodes are in the first hidden layer"));
        labelArray2.add(createLabel(false, "How many nodes are in the second hidden layer"));
        labelArray2.add(createLabel(false, "How many nodes are in the third hidden layer"));
        labelArray2.add(createLabel(false, "How many possible actions the snake has"));
        labelArray2.add(createLabel(false, "Inputlayer + Hiddenlayers + Outputlayer"));
        labelArray2.add(createLabel(false, "How many snakes one generation has"));
        labelArray2.add(createLabel(false, "How many rows has the gameboard"));
        labelArray2.add(createLabel(false, "How many columns has the gameboard"));
        labelArray2.add(createLabel(false, "How many steps does one snake have before it dies"));
        labelArray2.add(createLabel(false, "How long is the snake at the beginning"));

        textFieldArray.add(createNewTextField("" + settings.fitnessFunction));
        textFieldArray.add(createNewTextField("" + settings.autoSaveAt));
        textFieldArray.add(createNewTextField("" + settings.bestSnakesArraySize));
        textFieldArray.add(createNewTextField("" + settings.crossover));
        textFieldArray.add(createNewTextField("" + settings.mutationProbability));
        textFieldArray.add(createNewTextField("" + settings.mutationMax));
        textFieldArray.add(createNewTextField("" + settings.inputLayerNodes,true));
        textFieldArray.add(createNewTextField("" + settings.layer2Nodes));
        textFieldArray.add(createNewTextField("" + settings.layer3Nodes));
        textFieldArray.add(createNewTextField("" + settings.layer4Nodes));
        textFieldArray.add(createNewTextField("" + settings.outputLayerNodes,true));
        textFieldArray.add(createNewTextField("" + settings.layerMenge));
        textFieldArray.add(createNewTextField("" + settings.POPULATIONSIZE));
        textFieldArray.add(createNewTextField("" + settings.reihen));
        textFieldArray.add(createNewTextField("" + settings.spalten));
        textFieldArray.add(createNewTextField("" + settings.maxEnergy));
        textFieldArray.add(createNewTextField("" + settings.startLength));

        TextureAtlas atlas = new TextureAtlas("scrollpane/textures.atlas");
        BitmapFont pixel10 = new BitmapFont(Gdx.files.internal("scrollpane/pixel.fnt"), atlas.findRegion("pixel"), false);
        Skin skin2 = new Skin(atlas);
        skin2.add("default-font", pixel10);
        skin2.load(Gdx.files.internal("scrollpane//ui.json"));

        TextButton.TextButtonStyle buttonSelected = new TextButton.TextButtonStyle();
        buttonSelected.up = new TextureRegionDrawable(skin2.getRegion("default-round-down"));

        Table weckerscrollTable = new Table();
        weckerscrollTable.setFillParent(true);
        settingsStage.addActor(weckerscrollTable);

        Table weckerselectionContainer = new Table();

        for (int i = 0; i < textFieldArray.size; i++) {
            Group g = new Group();
            g.addActor(labelArray.get(i));
            g.addActor(textFieldArray.get(i));

            Group g2 = new Group();
            g2.addActor(labelArray2.get(i));

            weckerselectionContainer.add(g).size(w / 2, h / 6);
            weckerselectionContainer.add(g2).size(w / 2, h / 6);
            weckerselectionContainer.row();
        }
        weckerselectionContainer.pack();
        weckerselectionContainer.setTransform(false);
        weckerselectionContainer.setOrigin(weckerselectionContainer.getWidth() / 1f, weckerselectionContainer.getHeight() / 1);

        ScrollPane weckerscrollPane = new ScrollPane(weckerselectionContainer, skin2);
        weckerscrollPane.setScrollingDisabled(true, false);
        weckerscrollPane.layout();
        weckerscrollTable.add(weckerscrollPane).size(w, h / 1.3f).fill();
        weckerscrollTable.setPosition(0, h / 10 - weckerscrollTable.getHeight() / 1.3f);
        weckerscrollPane.updateVisualScroll();
    }

    private VisTextField createNewTextField(String startValue) {
        return createNewTextField(startValue, false);
    }

    private VisTextField createNewTextField(String startValue, boolean disabled) {
        VisTextField textField = new VisTextField(startValue);
        textField.setSize(w / 2.5f, h / 15);
        textField.setPosition((w / 2 - textField.getWidth()) / 2, 0);
        textField.setAlignment(Align.center);
        textField.setDisabled(disabled);
        return textField;
    }


    private VisLabel createLabel(boolean mode1, String text) {
        VisLabel label = new VisLabel(text);
        label.setSize(w / 2, h / 15);
        if (mode1)
            label.setPosition(0, h / 15);
        else
            label.setPosition(0, 0);
        label.setAlignment(Align.center);
        label.setBounds(label.getX(), label.getY(), label.getWidth(), label.getHeight());

        return label;
    }

    @Override
    public void show() {
        settingsStage.addActor(showSettingsButton);
        Gdx.input.setInputProcessor(settingsStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.2f, 0.2f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        settingsStage.act();
        settingsStage.draw();
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

    private void saveSettings() {
        settings.fitnessFunction = textFieldArray.get(0).getText();
        settings.autoSaveAt = Integer.parseInt(textFieldArray.get(1).getText());
        settings.bestSnakesArraySize = Integer.parseInt(textFieldArray.get(2).getText());
        settings.crossover = Boolean.parseBoolean(textFieldArray.get(3).getText());
        settings.mutationProbability = Double.parseDouble(textFieldArray.get(4).getText());
        settings.mutationMax = Double.parseDouble(textFieldArray.get(5).getText());
        settings.inputLayerNodes = Integer.parseInt(textFieldArray.get(6).getText());
        settings.layer2Nodes = Integer.parseInt(textFieldArray.get(7).getText());
        settings.layer3Nodes = Integer.parseInt(textFieldArray.get(8).getText());
        settings.layer4Nodes = Integer.parseInt(textFieldArray.get(9).getText());
        settings.outputLayerNodes = Integer.parseInt(textFieldArray.get(10).getText());
        settings.layerMenge = Integer.parseInt(textFieldArray.get(11).getText());
        settings.POPULATIONSIZE = Integer.parseInt(textFieldArray.get(12).getText());
        settings.reihen = Integer.parseInt(textFieldArray.get(13).getText());
        settings.spalten = Integer.parseInt(textFieldArray.get(14).getText());
        settings.maxEnergy = Integer.parseInt(textFieldArray.get(15).getText());
        settings.startLength = Integer.parseInt(textFieldArray.get(16).getText());
    }

    public static void resetTextFieldText() {
        textFieldArray.get(0).setText("" + settings.fitnessFunction);
        textFieldArray.get(1).setText("" + settings.autoSaveAt);
        textFieldArray.get(2).setText("" + settings.bestSnakesArraySize);
        textFieldArray.get(3).setText("" + settings.crossover);
        textFieldArray.get(4).setText("" + settings.mutationProbability);
        textFieldArray.get(5).setText("" + settings.mutationMax);
        textFieldArray.get(6).setText("" + settings.inputLayerNodes);
        textFieldArray.get(7).setText("" + settings.layer2Nodes);
        textFieldArray.get(8).setText("" + settings.layer3Nodes);
        textFieldArray.get(9).setText("" + settings.layer4Nodes);
        textFieldArray.get(10).setText("" + settings.outputLayerNodes);
        textFieldArray.get(11).setText("" + settings.layerMenge);
        textFieldArray.get(12).setText("" + settings.POPULATIONSIZE);
        textFieldArray.get(13).setText("" + settings.reihen);
        textFieldArray.get(14).setText("" + settings.spalten);
        textFieldArray.get(15).setText("" + settings.maxEnergy);
        textFieldArray.get(16).setText("" + settings.startLength);
    }


}
