package com.snake.ai;

import static com.snake.ai.SnakeGame.treats;
import static com.snake.ai.main.batch;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.getButtonXPosition;
import static com.snake.ai.main.getButtonYPosition;
import static com.snake.ai.main.h;
import static com.snake.ai.main.layerNodeValueArray;
import static com.snake.ai.main.layout;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.shapeRenderer;
import static com.snake.ai.main.showNeuralNetworkButton;
import static com.snake.ai.main.slowerButton;
import static com.snake.ai.main.startTheGameButton;
import static com.snake.ai.main.w;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class NeuralNetworkVisualization extends SnakeScreen implements Screen {

    BitmapFont font = new BitmapFont();
    GlyphLayout glyphLayout = new GlyphLayout();
    static int highest;
    Stage neuralNetworkStage = new Stage();
    VisTextButton snakeSwitchVision;
    boolean snakeVision = false;
    public static Array<Point> snakeVisionFieldArray = new Array<>();


    public NeuralNetworkVisualization() {
        snakeSwitchVision = new VisTextButton("Change to\nSnake Vision");
        snakeSwitchVision.setSize(slowerButton.getWidth(), slowerButton.getHeight());
        snakeSwitchVision.setPosition(getButtonXPosition(1), getButtonYPosition(0));
        snakeSwitchVision.getLabel().setFontScale(w / 1100);
        snakeSwitchVision.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                snakeVision = !snakeVision;
                if (!snakeVision)
                    snakeSwitchVision.setText("Change to\nSnake Vision");
                else
                    snakeSwitchVision.setText("Change to\nHuman Vision");
            }
        });
    }

    @Override
    public void show() {
        font.setColor(0f, 0f, 0f, 1f);
        neuralNetworkStage.addActor(startTheGameButton);
        neuralNetworkStage.addActor(showNeuralNetworkButton);
        neuralNetworkStage.addActor(snakeSwitchVision);
        Gdx.input.setInputProcessor(neuralNetworkStage);
    }

    @Override
    public void render(float delta) {
        try {
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.line(w / 2, h, w / 2, 0);
            shapeRenderer.end();

            if (snakeVision) {
                drawBackground();
            } else {
                drawGame();
            }
            try {
                if (snakeVision)
                    drawSnakeVision();
            } catch (Exception e) {
                e.printStackTrace();
                shapeRenderer.end();
            }

            snakeStage.act();
            snakeStage.draw();
            batch.begin();
            layout.setText(font, "Paused: " + freeze);
            font.draw(batch, layout,
                    w / 2 + (settings.spalten + 1) * w / 2 / (settings.reihen + 2) - layout.width
                    , settings.reihen * h / settings.spalten - h / (settings.spalten + 2) / 2 + layout.height / 2);
            batch.end();

            drawLines();
            drawCircles();
            drawLabels();
        } catch (Exception ignored) {
            shapeRenderer.end();
        }
        neuralNetworkStage.draw();

    }

    private void drawBackground() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.6f, 1, 1);
        for (int i = 0; i < (settings.reihen + 2); i++) {
            for (int j = 0; j < (settings.spalten + 2); j++) {

                shapeRenderer.rect(w / 2 + i * w / 2 / (settings.reihen + 2)
                        , j * h / (settings.spalten + 2)
                        , w / 2 / (settings.reihen + 2)
                        , h / (settings.spalten + 2));
            }
        }
        shapeRenderer.end();
    }

    public void drawSnakeVision() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        try {
            for (int i = 0; i < 4; i++) {
                Point p = snakeVisionFieldArray.get(i);
                if (p != null) {
                    shapeRenderer.setColor((float) p.value, (float) p.value, (float) p.value, 1);
                    shapeRenderer.rect(w / 2 + p.x * w / 2 / (settings.reihen + 2)
                            , p.y * h / (settings.spalten + 2)
                            , w / 2 / (settings.reihen + 2)
                            , h / (settings.spalten + 2));
                }
            }
        } catch (Exception ignored) {

        }

        //Treats
        try {
            for (int i = 0 + 8; i < 8 + 4 + 4; i++) {
                Point p = snakeVisionFieldArray.get(i);
                if (p != null) {
                    shapeRenderer.setColor(1, 1, 1,1);
                    shapeRenderer.rect(treats.get(treats.size - 1).x * w / 2 / (settings.reihen + 2) + w / 2
                            , ((settings.spalten + 2) - 1 - treats.get(treats.size - 1).y) * h / (settings.spalten + 2)
                            , w / 2 / (settings.reihen + 2)
                            , h / (settings.spalten + 2));
                }
            }
        } catch (Exception ignored) {

        }

        //Snake
        try {
            for (int i = 0 + 8 + 8; i < 8 + 8 + 4; i++) {
                Point p = snakeVisionFieldArray.get(i);
                if (p != null) {
                    shapeRenderer.setColor(0.3f, 0.7f, 0.3f, 1);
                    shapeRenderer.rect(p.x * w / 2 / (settings.reihen + 2) + w / 2
                            , ((settings.spalten + 2) - 1 - p.y) * h / (settings.spalten + 2)
                            , w / 2 / (settings.reihen + 2)
                            , h / (settings.spalten + 2));
                }
            }
        } catch (Exception ignored) {

        }

        /*try {
            for (int i = 0; i < snakeVisionFieldArray.size; i++) {
                Point p = snakeVisionFieldArray.get(i);
                if (p.x >= 0 && p.x <= settings.reihen + 1 &&
                        p.y >= 0 && p.y <= settings.spalten + 1) {
                    if (p.value == -1) {
                        shapeRenderer.setColor(1, 0, 0, 1);
                        shapeRenderer.rect(treats.get(treats.size - 1).x * w / 2 / (settings.reihen + 2) + w / 2
                                , ((settings.spalten + 2) - 1 - treats.get(treats.size - 1).y) * h / (settings.spalten + 2)
                                , w / 2 / (settings.reihen + 2)
                                , h / (settings.spalten + 2));
                    } else if (p.value == -2) {
                        shapeRenderer.setColor((float) p.value, (float) p.value, (float) p.value, 1);
                        shapeRenderer.rect(w / 2 + p.x * w / 2 / (settings.reihen + 2)
                                , (settings.spalten - p.y + 1) * h / (settings.spalten + 2)
                                , w / 2 / (settings.reihen + 2)
                                , h / (settings.spalten + 2));
                    } else if (p.value == -3) {
                        shapeRenderer.setColor(0, 1, 0, 1);
                        shapeRenderer.rect(w / 2 + p.x * w / 2 / (settings.reihen + 2)
                                , (settings.spalten - p.y + 1) * h / (settings.spalten + 2)
                                , w / 2 / (settings.reihen + 2)
                                , h / (settings.spalten + 2));
                    }
                }
            }
        } catch (Exception ignored) {

        }*/
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        snakeStage.getViewport().update(width,height);
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

    private void drawLines() {
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int layer = 0; layer < currentSnake.layerArray.size - 1; layer++) {
            for (int node = 0; node < currentSnake.layerArray.get(layer).nodeArray.size; node++) {
                for (int weight = 0; weight < currentSnake.layerArray.get(layer).nodeArray.get(node).weightArray.size; weight++) {
                    if (currentSnake.layerArray.get(layer).nodeArray.get(node).weightArray.get(weight) > 0) {
                        shapeRenderer.setColor(Color.RED);
                        Gdx.gl20.glLineWidth(currentSnake.layerArray.get(layer).nodeArray.get(node).weightArray.get(weight).floatValue() * 1);
                    } else {
                        shapeRenderer.setColor(Color.BLUE);
                        Gdx.gl20.glLineWidth(currentSnake.layerArray.get(layer).nodeArray.get(node).weightArray.get(weight).floatValue() * -1);
                    }

                    float width = w / 170;
                    float verticalDistance = ((h - (showNeuralNetworkButton.getY() + showNeuralNetworkButton.getHeight() + h / 30 * 2)) - currentSnake.layerArray.get(0).nodeArray.size * width) / (currentSnake.layerArray.get(0).nodeArray.size + 1);
                    float position = node * width + (node + 1) * verticalDistance;
                    float max = currentSnake.layerArray.get(layer).nodeArray.size * width + (currentSnake.layerArray.get(layer).nodeArray.size + 1) * verticalDistance;
                    float maxLayer0 = currentSnake.layerArray.get(0).nodeArray.size * width + (currentSnake.layerArray.get(0).nodeArray.size + 1) * verticalDistance;

                    float position2 = weight * width + (weight + 1) * verticalDistance;
                    float max2 = currentSnake.layerArray.get(layer + 1).nodeArray.size * width + (currentSnake.layerArray.get(layer + 1).nodeArray.size + 1) * verticalDistance;

                    shapeRenderer.rectLine(layer * (w / 2 / 3.4f) + (w / 2 / 50), position + (maxLayer0 - max) / 2 + showNeuralNetworkButton.getY() + showNeuralNetworkButton.getHeight() + h / 30 + width / 2,
                            (layer + 1) * (w / 2 / 3.4f) + (w / 2 / 50), position2 + (maxLayer0 - max2) / 2 + showNeuralNetworkButton.getY() + showNeuralNetworkButton.getHeight() + h / 30 + width / 2,(int) w/1200f);
                }
            }
        }
        shapeRenderer.end();
    }

    private void drawCircles() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int layer = 0; layer < currentSnake.layerArray.size; layer++) {
            for (int node = 0; node < currentSnake.layerArray.get(layer).nodeArray.size; node++) {
                shapeRenderer.setColor(Color.BLACK);
                float width1 = w / 170;
                float width2 = w / 220;

                float verticalDistance = ((h - (showNeuralNetworkButton.getY() + showNeuralNetworkButton.getHeight() + h / 30 * 2)) - currentSnake.layerArray.get(0).nodeArray.size * width1) / (currentSnake.layerArray.get(0).nodeArray.size + 1);
                float position = node * width1 + (node + 1) * verticalDistance;
                float max = currentSnake.layerArray.get(layer).nodeArray.size * width1 + (currentSnake.layerArray.get(layer).nodeArray.size + 1) * verticalDistance;
                float maxLayer0 = currentSnake.layerArray.get(0).nodeArray.size * width1 + (currentSnake.layerArray.get(0).nodeArray.size + 1) * verticalDistance;
                shapeRenderer.circle(layer * (w / 2 / 3.4f) + (w / 2 / 50), getPositionY(layer, node, width1) - (width2 - width1), width1);


                if (layer == 0) {
                    float value = (float) (currentSnake.layerArray.get(0).nodeArray.get(node).value);
                    shapeRenderer.setColor(value * 3, value / 1.2f, value / 1.2f, 1f);
                } else if (layer == currentSnake.layerArray.size - 1) {
                    if (node == highest) {
                        float value = (float) (currentSnake.layerArray.get(0).nodeArray.get(node).value);
                        shapeRenderer.setColor(value * 3, value / 1.2f, value / 1.2f, 1f);
                    } else {
                        shapeRenderer.setColor(Color.BLACK);
                    }
                } else {
                    float value = (float) (currentSnake.layerArray.get(0).nodeArray.get(node).value);
                    shapeRenderer.setColor(value / 1.2f, value / 1.2f, value * 3, 1f);
                }

                shapeRenderer.circle(layer * (w / 2 / 3.4f) + (w / 2 / 50), getPositionY(layer, node, width2) - (width2 - width1) / 2, width2);
            }
        }
        shapeRenderer.end();
    }

    private void drawLabels() {
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "InputLayer", w / 100f, h / 1.02f);
        font.draw(batch, "HiddenLayer 1", w / 8f, h / 1.02f);
        font.draw(batch, "HiddenLayer 2", w / 3.8f, h / 1.02f);
        font.draw(batch, "OutputLayer", w / 2.43f, h / 1.02f);

        glyphLayout.setText(font, "U");
        font.getData().setScale(w/1100);
        font.draw(batch, glyphLayout, 3 * (w / 2 / 3.2f) + (w / 2 / 50), getPositionY(layerNodeValueArray.size - 1, 1, w / 170) - glyphLayout.height);
        glyphLayout.setText(font, "D");
        font.draw(batch, glyphLayout, 3 * (w / 2 / 3.2f) + (w / 2 / 50), getPositionY(layerNodeValueArray.size - 1, 2, w / 170) - glyphLayout.height);
        glyphLayout.setText(font, "L");
        font.draw(batch, glyphLayout, 3 * (w / 2 / 3.2f) + (w / 2 / 50), getPositionY(layerNodeValueArray.size - 1, 3, w / 170) - glyphLayout.height);
        glyphLayout.setText(font, "R");
        font.draw(batch, glyphLayout, 3 * (w / 2 / 3.2f) + (w / 2 / 50), getPositionY(layerNodeValueArray.size - 1, 4, w / 170) - glyphLayout.height);
        batch.end();
    }

    private float getPositionY(int layer, int node, float width) {
        float verticalDistance = ((h - (showNeuralNetworkButton.getY() + showNeuralNetworkButton.getHeight() + h / 30 * 2)) - currentSnake.layerArray.get(0).nodeArray.size * width) / (currentSnake.layerArray.get(0).nodeArray.size + 1);
        float position = node * width + (node + 1) * verticalDistance;
        float max = currentSnake.layerArray.get(layer).nodeArray.size * width + (currentSnake.layerArray.get(layer).nodeArray.size + 1) * verticalDistance;
        float maxLayer0 = currentSnake.layerArray.get(0).nodeArray.size * width + (currentSnake.layerArray.get(0).nodeArray.size + 1) * verticalDistance;

        return position + (maxLayer0 - max) / 2 + showNeuralNetworkButton.getY() + showNeuralNetworkButton.getHeight() + h / 30;
    }
}
