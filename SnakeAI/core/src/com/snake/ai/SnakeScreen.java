package com.snake.ai;

import static com.snake.ai.SnakeGame.energy;
import static com.snake.ai.SnakeGame.score;
import static com.snake.ai.SnakeGame.snakeNr;
import static com.snake.ai.SnakeGame.timePerPop;
import static com.snake.ai.main.batch;
import static com.snake.ai.main.fasterButton;
import static com.snake.ai.main.fitnessDoubleToE;
import static com.snake.ai.main.font;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gameNr;
import static com.snake.ai.main.graphmode1;
import static com.snake.ai.main.h;
import static com.snake.ai.main.helpButton;
import static com.snake.ai.main.layout;
import static com.snake.ai.main.maxSpeedButton;
import static com.snake.ai.main.replayBestSnakeButton;
import static com.snake.ai.main.scoreboardButton;
import static com.snake.ai.main.settings;
import static com.snake.ai.main.shapeRenderer;
import static com.snake.ai.main.showNeuralNetworkButton;
import static com.snake.ai.main.showSavedInstancesButton;
import static com.snake.ai.main.showSettingsButton;
import static com.snake.ai.main.slowerButton;
import static com.snake.ai.main.snakeGameInstance;
import static com.snake.ai.main.startTheGameButton;
import static com.snake.ai.main.switchGraphButton;
import static com.snake.ai.main.w;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class SnakeScreen implements Screen {
    Stage snakeStage = new Stage();

    @Override
    public void show() {
        snakeStage.addActor(switchGraphButton);
        snakeStage.addActor(startTheGameButton);
        snakeStage.addActor(showSavedInstancesButton);
        snakeStage.addActor(showNeuralNetworkButton);
        snakeStage.addActor(slowerButton);
        snakeStage.addActor(fasterButton);
        snakeStage.addActor(maxSpeedButton);
        snakeStage.addActor(replayBestSnakeButton);
        snakeStage.addActor(showSettingsButton);
        snakeStage.addActor(scoreboardButton);
        snakeStage.addActor(helpButton);

        Gdx.input.setInputProcessor(snakeStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.8f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(w / 2, h, w / 2, 0);
        shapeRenderer.end();

        drawGame();

        try {
            drawFonts();
            drawGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }

        snakeStage.act();
        snakeStage.draw();
        batch.begin();
        layout.setText(font, "Paused: " + freeze);
        font.draw(batch, layout,
                w / 2 + (settings.spalten + 1) * w / 2 / (settings.reihen + 2) - layout.width
                , settings.reihen * h / settings.spalten - h / (settings.spalten + 2) / 2 + layout.height / 2);
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


    public void drawGraph() {
        Array<Integer> tmpArray;
        if (!graphmode1) {
            tmpArray = snakeGameInstance.hiscoreArray;
        } else {
            tmpArray = snakeGameInstance.averageFitnessArray;
        }


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(0
                , h / 2f - w / 100
                , (w / 100) * 2 + w / 3.9f
                , (w / 100) * 2 + h / 2.23f);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(slowerButton.getX()
                , h / 2f
                , w / 3.9f
                , h / 2.23f);
        shapeRenderer.end();


        if (tmpArray.size > 1) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.WHITE);
            //X Achse
            shapeRenderer.line(w / 20
                    , h / 1.82f
                    , w / 4f
                    , h / 1.82f);
            //Y Achse
            shapeRenderer.line(w / 20
                    , h / 1.82f
                    , w / 20
                    , h / 1.08f);

            int highest = 0;
            for (int i = 0; i < tmpArray.size; i++) {
                if (tmpArray.get(i) >= tmpArray.get(highest))
                    highest = i;
            }

            batch.begin();
            for (int i = 0; i < tmpArray.get(highest) + 1; i++) {
                if ((i % ((int) ((tmpArray.get(highest) / 5.0)) + 1)) == 0) {
                    font.setUseIntegerPositions(false);
                    font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

                    if (!graphmode1)
                        font.draw(batch, "" + i, w / 80, ((h / 1.08f - h / 1.75f) / tmpArray.get(highest)) * i + h / 1.75f);
                    else
                        font.draw(batch, "" + fitnessDoubleToE(i), w / 80, ((h / 1.08f - h / 1.75f) / tmpArray.get(highest)) * i + h / 1.75f);
                }
            }

            for (int i = 0; i < tmpArray.size; i++) {
                if (tmpArray.size > 1) {
                    if ((i % ((int) ((tmpArray.size / 5.0)) + 1)) == 0) {
                        font.draw(batch, "" + i, ((w / 4f - w / 20f) / tmpArray.size) * i + w / 20, h / 1.85f);
                    }
                }
            }
            batch.end();

            for (int i = 0; i < tmpArray.size - 1; i++) {
                shapeRenderer.line(
                        ((w / 4f - w / 20f) / tmpArray.size) * i + w / 20f
                        , ((h / 1.08f - h / 1.82f) / tmpArray.get(highest)) * tmpArray.get(i) + h / 1.82f
                        , ((w / 4f - w / 20f) / tmpArray.size) * (i + 1) + w / 20f
                        , ((h / 1.08f - h / 1.82f) / tmpArray.get(highest)) * tmpArray.get(i + 1) + h / 1.82f);
            }
        }

        shapeRenderer.end();
    }

    public void drawFonts() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect((w / 3.6f + (w / 4.7f - w / 5.4f) / 2) - w / 100
                , h / 50f - w / 100
                , w / 5.4f + (w / 100) * 2
                , (h / 2f - w / 100 + w / 100 + h / 2.23f) - h / 50f + (w / 100) * 2);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(w / 3.6f + (w / 4.7f - w / 5.4f) / 2
                , h / 50f
                , w / 5.4f
                , (h / 2f - w / 100 + w / 100 + h / 2.23f) - h / 50f);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "Settings", w / 3.35f, h / 1.07f);
        font.draw(batch, "______________________", w / 3.35f, h / 1.08f);
        font.draw(batch, "Game Nr: " + gameNr, w / 3.35f, h / 1.13f);
        font.draw(batch, "SnakeGame Nr: " + snakeNr, w / 3.35f, h / 1.18f);
        font.draw(batch, "Score: " + score, w / 3.35f, h / 1.23f);
        font.draw(batch, "Population: " + snakeGameInstance.population, w / 3.35f, h / 1.28f);
        font.setColor(1f, 0.3f, 0.3f, 1);
        int maxLength = settings.reihen * settings.spalten - settings.startLength;
        font.draw(batch, "Highscore: " + snakeGameInstance.hiScore + " (Max: " + maxLength + ")", w / 3.35f, h / 1.34f);
        font.draw(batch, "Best Fitness Ever: " + fitnessDoubleToE(snakeGameInstance.bestSnake.fitness), w / 3.35f, h / 1.4f);
        font.setColor(Color.WHITE);
        font.draw(batch, "______________________", w / 3.35f, h / 1.45f);
        font.draw(batch, "Input Layer Nodes: " + settings.inputLayerNodes, w / 3.35f, h / 1.72f);
        font.draw(batch, "Layer 2 Nodes: " + settings.layer2Nodes, w / 3.35f, h / 1.82f);
        font.draw(batch, "Layer 3 Nodes: " + settings.layer3Nodes, w / 3.35f, h / 1.93f);
        font.draw(batch, "Layer 4 Nodes: " + settings.layer4Nodes, w / 3.35f, h / 2.05f);
        font.draw(batch, "Output Layer Nodes: " + settings.outputLayerNodes, w / 3.35f, h / 2.18f);
        font.draw(batch, "______________________", w / 3.35f, h / 2.34f);
        font.draw(batch, "Mutation Probability: " + (float) settings.mutationProbability, w / 3.35f, h / 2.55f);
        font.draw(batch, "Mutation Max: " + (float) settings.mutationMax, w / 3.35f, h / 3.1f);
        font.draw(batch, "Population Size: " + settings.POPULATIONSIZE, w / 3.35f, h / 3.4f);
        font.draw(batch, "______________________", w / 3.35f, h / 4.5f);
        font.draw(batch, "Rows: " + settings.reihen, w / 3.35f, h / 5.5f);
        font.draw(batch, "Columns: " + settings.spalten, w / 3.35f, h / 6.5f);
        font.draw(batch, "______________________", w / 3.35f, h / 7.5f);
        font.draw(batch, "Time Per Population: " + timePerPop, w / 3.35f, h / 12f);
        batch.end();
    }

    public void drawGame() {
        //Feld
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < (settings.reihen + 2); i++) {
            for (int j = 0; j < (settings.spalten + 2); j++) {
                if (i == 0 || j == 0 || i == (settings.reihen + 2) - 1 || j == (settings.spalten + 2) - 1) {
                    shapeRenderer.setColor(Color.GRAY);
                } else {
                    if ((i + j) % 2 == 0)
                        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
                    else
                        shapeRenderer.setColor(0.25f, 0.25f, 0.25f, 1);
                }
                shapeRenderer.rect(w / 2 + i * w / 2 / (settings.reihen + 2)
                        , j * h / (settings.spalten + 2)
                        , w / 2 / (settings.reihen + 2)
                        , h / (settings.spalten + 2));
            }
        }
        //Treats
        try {
            shapeRenderer.setColor(1, 1, 1, 1);
            shapeRenderer.rect(SnakeGame.treats.get(SnakeGame.treats.size - 1).x * w / 2 / (settings.reihen + 2) + w / 2
                    , ((settings.spalten + 2) - 1 - SnakeGame.treats.get(SnakeGame.treats.size - 1).y) * h / (settings.spalten + 2)
                    , w / 2 / (settings.reihen + 2)
                    , h / (settings.spalten + 2));
        } catch (Exception ignored) {

        }
        //Schlange
        try {
            if (SnakeGame.snake != null && SnakeGame.snake.size > 0) {
                for (int i = 0; i < SnakeGame.snake.size; i++) {
                    try {
                        shapeRenderer.setColor(0.3f, 0.7f, 0.3f, 1);
                        shapeRenderer.rect(SnakeGame.snake.get(i).x * w / 2 / (settings.reihen + 2) + w / 2
                                , ((settings.spalten + 2) - 1 - SnakeGame.snake.get(i).y) * h / (settings.spalten + 2)
                                , w / 2 / (settings.reihen + 2)
                                , h / (settings.spalten + 2));
                        if (i == 0) {
                            //Head
                            shapeRenderer.setColor(1f + ((energy) / (float) settings.maxEnergy - 1), 0.3f + ((energy) / (float) settings.maxEnergy - 1), 0.3f + ((energy) / (float) settings.maxEnergy - 1), 1);
                            shapeRenderer.rect(SnakeGame.snake.get(i).x * w / 2 / (settings.reihen + 2) + w / 2
                                    , ((settings.spalten + 2) - 1 - SnakeGame.snake.get(i).y) * h / (settings.spalten + 2)
                                    , w / 2 / (settings.reihen + 2)
                                    , h / (settings.spalten + 2));
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        } catch (Exception ignored) {

        }
        shapeRenderer.end();
    }
}
