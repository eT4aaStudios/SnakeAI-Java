package com.snake.ai;

import static com.snake.ai.main.batch;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.h;
import static com.snake.ai.main.layout;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.shapeRenderer;
import static com.snake.ai.main.showNeuralNetworkButton;
import static com.snake.ai.main.startTheGameButton;
import static com.snake.ai.main.w;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class NeuralNetworkVisualization extends SnakeScreen implements Screen {

    BitmapFont font = new BitmapFont();
    static int highest;

    Stage neuralNetworkStage = new Stage();

    @Override
    public void show() {
        font.setColor(0f, 0f, 0f, 1f);
        neuralNetworkStage.addActor(startTheGameButton);
        neuralNetworkStage.addActor(showNeuralNetworkButton);
        Gdx.input.setInputProcessor(neuralNetworkStage);
    }

    @Override
    public void render(float delta) {
        try {
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.line(w / 2, h, w / 2, 0);
            shapeRenderer.end();

            drawGame();

            snakeStage.act();
            snakeStage.draw();
            batch.begin();
            layout.setText(font, "Paused: " + freeze);
            font.draw(batch, layout,
                    w / 2 + (settings.spalten + 1) * w / 2 / (settings.reihen + 2) - layout.width
                    , settings.reihen * h / settings.spalten - h / (settings.spalten + 2) / 2 + layout.height / 2);
            batch.end();

            drawNeuralNetwork();
            drawLabels();
        } catch (Exception ignored) {

        }
        neuralNetworkStage.draw();

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

    private void drawNeuralNetwork() {
        shapeRenderer.setColor(1,1,1,1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int Layer = 0; Layer < currentSnake.layerArray.size - 1; Layer++) {
            for (int Node = 0; Node < currentSnake.layerArray.get(Layer).nodeArray.size; Node++) {
                for (int Weight = 0; Weight < currentSnake.layerArray.get(Layer).nodeArray.get(Node).weightArray.size; Weight++) {
                    if (currentSnake.layerArray.get(Layer).nodeArray.get(Node).weightArray.get(Weight) > 0) {
                        shapeRenderer.setColor(Color.RED);
                        Gdx.gl20.glLineWidth(currentSnake.layerArray.get(Layer).nodeArray.get(Node).weightArray.get(Weight).floatValue() * 1);
                    } else {
                        shapeRenderer.setColor(Color.BLUE);
                        Gdx.gl20.glLineWidth(currentSnake.layerArray.get(Layer).nodeArray.get(Node).weightArray.get(Weight).floatValue() * -1);
                    }

                    shapeRenderer.line(Layer * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer).nodeArray.size * h / 56f - Node * h / 37f,
                            (Layer + 1) * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer + 1).nodeArray.size * h / 56f - Weight * h / 37f);
                }
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int Layer = 0; Layer < currentSnake.layerArray.size; Layer++) {
            for (int Node = 0; Node < currentSnake.layerArray.get(Layer).nodeArray.size; Node++) {
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.circle(Layer * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer).nodeArray.size * h / 56f - Node * h / 37f, w / 170);
                if (Layer == 0) {
                    float value = (float) (currentSnake.layerArray.get(0).nodeArray.get(Node).value);
                    shapeRenderer.setColor(value * 3, value / 1.2f, value / 1.2f, 1f);
                } else if (Layer == currentSnake.layerArray.size - 1) {
                    if (Node == highest) {
                        float value = (float) (currentSnake.layerArray.get(0).nodeArray.get(Node).value);
                        shapeRenderer.setColor(value * 3, value / 1.2f, value / 1.2f, 1f);
                    } else
                        shapeRenderer.setColor(Color.BLACK);
                } else {
                    float value = (float) (currentSnake.layerArray.get(0).nodeArray.get(Node).value);
                    shapeRenderer.setColor(value / 1.2f, value / 1.2f, value * 3, 1f);
                }
                shapeRenderer.circle(Layer * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer).nodeArray.size * h / 56f - Node * h / 37f, w / 220);
            }
        }
        shapeRenderer.end();

    }

    private void drawLabels() {
        batch.begin();
        font.setColor(1,1,1,1);
        font.draw(batch, "InputLayer", w / 100f, h / 1.02f);
        font.draw(batch, "HiddenLayer 1", w / 8f, h / 1.02f);
        font.draw(batch, "HiddenLayer 2", w / 3.8f, h / 1.02f);
        font.draw(batch, "OutputLayer", w / 2.43f, h / 1.02f);

        font.draw(batch, "U", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).nodeArray.size * h / 53f - 0 * h / 28f);
        font.draw(batch, "D", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).nodeArray.size * h / 53f - 1 * h / 28f);
        font.draw(batch, "L", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).nodeArray.size * h / 53f - 2 * h / 28f);
        font.draw(batch, "R", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).nodeArray.size * h / 53f - 3 * h / 28f);
        batch.end();
    }
}
