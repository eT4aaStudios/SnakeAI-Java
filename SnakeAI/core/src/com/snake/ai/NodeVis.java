package com.snake.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.snake.ai.main.currentScreen;
import static com.snake.ai.main.currentSnake;
import static com.snake.ai.main.h;

public class NodeVis implements Screen {

    ShapeRenderer shapeRenderer;
    BitmapFont font = new BitmapFont();
    static int highest;

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        font.setColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void render(float delta) {
        if (currentScreen) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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

                        shapeRenderer.line(Layer * 200 + 100, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(Layer).NodeArray.size * 8f - Node * 22f + 80,
                                (Layer + 1) * 200 + 100, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(Layer + 1).NodeArray.size * 8f - Weight * 22f + 80);
                    }
                }
            }
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (int Layer = 0; Layer < currentSnake.layerArray.size; Layer++) {
                for (int Node = 0; Node < currentSnake.layerArray.get(Layer).NodeArray.size; Node++) {
                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.circle(Layer * 200 + 100, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(Layer).NodeArray.size * 8f - Node * 22f + 80, 8);
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
                    shapeRenderer.circle(Layer * 200 + 100, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(Layer).NodeArray.size * 8f - Node * 22f + 80, 6);
                }
            }
            shapeRenderer.end();

            main.batch.begin();

            font.draw(main.batch, "InputLayer", 100 - font.getRegion().getRegionWidth() / 6f, h / 1.02f);
            font.draw(main.batch, "HiddenLayer 1", 200 + 100 - font.getRegion().getRegionWidth() / 6f, h / 1.02f);
            font.draw(main.batch, "HiddenLayer 2", 2 * 200 + 100 - font.getRegion().getRegionWidth() / 6f, h / 1.02f);
            font.draw(main.batch, "OutputLayer", 3 * 200 + 100 - font.getRegion().getRegionWidth() / 6f, h / 1.02f);

            font.draw(main.batch, "U", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 0 * 22f + 85);
            font.draw(main.batch, "D", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 1 * 22f + 85);
            font.draw(main.batch, "L", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 2 * 22f + 85);
            font.draw(main.batch, "R", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 3 * 22f + 85);
            main.batch.end();
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
