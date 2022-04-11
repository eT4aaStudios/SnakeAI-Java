package com.snake.ai;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Settings {

    //Neuronales Netzwerk Eigenschaften
    public int autoSaveAt = 50;
    public int bestSnakesArraySize = 500;
    public boolean crossover = false;
    public double mutationProbability = 5;//%
    public double mutationMax = 0.2;
    //Neuronales Netzwerk Aussehen
    public int inputLayerNodes = 24;
    public int layer2Nodes = 20;
    public int layer3Nodes = 12;
    public int layer4Nodes = 0; //TODO create button
    public int outputLayerNodes = 4;
    public int layerMenge = 4;
    //Evolutions Eigenschaften
    public int POPULATIONSIZE = 1000;
    public int reihen = 10;
    public int spalten = 10;
    public int maxEnergy = 100;
    public int startLength = 5;

    public Settings() {
        if (Gdx.app.getType() != Application.ApplicationType.Android) {
            autoSaveAt = 500;
        }
    }
}
