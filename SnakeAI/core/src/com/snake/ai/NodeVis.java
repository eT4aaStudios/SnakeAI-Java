package com.snake.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.snake.ai.main.currentScreen;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.h;
import static com.snake.ai.main.shapeRenderer;
import static com.snake.ai.main.w;

public class NodeVis implements Screen {

    BitmapFont font = new BitmapFont();
    static int highest;

    @Override
    public void show() {
        font.setColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void render(float delta) {
        if (currentScreen) {
            try {
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.line(w / 2, h, w / 2, 0);
                for (int Layer = 0; Layer < currentSnake.layerArray.size - 1; Layer++) {
                    for (int Node = 0; Node < currentSnake.layerArray.get(Layer).NodeArray.size; Node++) {
                        for (int Weight = 0; Weight < currentSnake.layerArray.get(Layer).NodeArray.get(Node).WeigthArray.size; Weight++) {
                            if (currentSnake.layerArray.get(Layer).NodeArray.get(Node).WeigthArray.get(Weight) > 0) {
                                shapeRenderer.setColor(Color.RED);
                                Gdx.gl20.glLineWidth(currentSnake.layerArray.get(Layer).NodeArray.get(Node).WeigthArray.get(Weight).floatValue() * 1);
                            } else {
                                shapeRenderer.setColor(Color.BLUE);
                                Gdx.gl20.glLineWidth(currentSnake.layerArray.get(Layer).NodeArray.get(Node).WeigthArray.get(Weight).floatValue() * -1);
                            }

                            shapeRenderer.line(Layer * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer).NodeArray.size * h / 56f - Node * h / 37f,
                                    (Layer + 1) * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer + 1).NodeArray.size * h / 56f - Weight * h / 37f);
                        }
                    }
                }
                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                for (int Layer = 0; Layer < currentSnake.layerArray.size; Layer++) {
                    for (int Node = 0; Node < currentSnake.layerArray.get(Layer).NodeArray.size; Node++) {
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.circle(Layer * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer).NodeArray.size * h / 56f - Node * h / 37f, w / 170);
                        if (Layer == 0) {
                            float value = (float) (currentSnake.layerArray.get(0).NodeArray.get(Node).value);
                            shapeRenderer.setColor(value * 3, value / 1.2f, value / 1.2f, 1f);
                        } else if (Layer == currentSnake.layerArray.size - 1) {
                            if (Node == highest) {
                                float value = (float) (currentSnake.layerArray.get(0).NodeArray.get(Node).value);
                                shapeRenderer.setColor(value * 3, value / 1.2f, value / 1.2f, 1f);
                            } else
                                shapeRenderer.setColor(Color.BLACK);
                        } else {
                            float value = (float) (currentSnake.layerArray.get(0).NodeArray.get(Node).value);
                            shapeRenderer.setColor(value / 1.2f, value / 1.2f, value * 3, 1f);
                        }
                        shapeRenderer.circle(Layer * (w / 2 / 3.4f) + (w / 2 / 50), h / 2.6f + currentSnake.layerArray.get(Layer).NodeArray.size * h / 56f - Node * h / 37f, w / 220);
                    }
                }
                shapeRenderer.end();

                main.batch.begin();

                font.draw(main.batch, "InputLayer", w / 100f, h / 1.02f);
                font.draw(main.batch, "HiddenLayer 1", w / 8f, h / 1.02f);
                font.draw(main.batch, "HiddenLayer 2", w / 3.8f, h / 1.02f);
                font.draw(main.batch, "OutputLayer", w / 2.43f, h / 1.02f);

                font.draw(main.batch, "U", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).NodeArray.size * h / 53f - 0 * h / 28f);
                font.draw(main.batch, "D", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).NodeArray.size * h / 53f - 1 * h / 28f);
                font.draw(main.batch, "L", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).NodeArray.size * h / 53f - 2 * h / 28f);
                font.draw(main.batch, "R", 3 * (w / 2 / 3.2f) + (w / 2 / 50), h / 2 + currentSnake.layerArray.get(3).NodeArray.size * h / 53f - 3 * h / 28f);
                main.batch.end();
            }catch (Exception ignored) {

            }
        }
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
