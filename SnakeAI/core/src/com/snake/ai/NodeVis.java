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

                    if (Layer == 0 || Layer == currentSnake.layerArray.size - 1) {
                        double highest = 0d;
                        int id = 0;
                        for (int i = 0; i < currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.size; i++) {
                            if (currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value > highest) {
                                highest = currentSnake.layerArray.get(currentSnake.layerArray.size - 1).NodeArray.get(i).value;
                                id = i;
                            }
                        }
                        if((currentSnake.layerArray.get(Layer).NodeArray.get(Node).value >= 0.9d && Layer == 0) || Node == id) {
                            shapeRenderer.setColor(Color.GREEN);
                        }else
                            shapeRenderer.setColor(Color.WHITE);
                    }else {
                        if(currentSnake.layerArray.get(Layer).NodeArray.get(Node).value >= 0.7d)
                            shapeRenderer.setColor(0,0.7f,0.8f,0.9f);
                        else
                            shapeRenderer.setColor(Color.GRAY);
                    }
                    shapeRenderer.circle(Layer * 200 + 100, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(Layer).NodeArray.size * 8f - Node * 22f + 80, 6);
                }
            }
            shapeRenderer.end();

            main.batch.begin();

            font.draw(main.batch, "InputLayer", 100 - font.getRegion().getRegionWidth() / 6f,  h / 1.02f);
            font.draw(main.batch, "HiddenLayer 1", 200 + 100 - font.getRegion().getRegionWidth() / 6f, h / 1.02f);
            font.draw(main.batch, "HiddenLayer 2", 2 * 200 + 100 - font.getRegion().getRegionWidth() / 6f, h / 1.02f);
            font.draw(main.batch, "OutputLayer", 3 * 200 + 100 - font.getRegion().getRegionWidth() / 6f, h / 1.02f);

            font.draw(main.batch, "R", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 0 * 22f + 85);
            font.draw(main.batch, "U", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 1 * 22f + 85);
            font.draw(main.batch, "L", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 2 * 22f + 85);
            font.draw(main.batch, "D", 3 * 200 + 115, Gdx.graphics.getHeight() / 2f + currentSnake.layerArray.get(3).NodeArray.size * 8f - 3 * 22f + 85);
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
