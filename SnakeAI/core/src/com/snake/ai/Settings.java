package com.snake.ai;

public class Settings {

    //Neuronales Netzwerk Eigenschaften
    public static int bestSnakesArraySize = 500;

    public static boolean crossover = false;
    public static double mutationProbability = 0.5;//%
    public static double mutationMax = 0.2;

    //Neuronales Netzwerk Aussehen
    public static int inputLayerNodes = 24;
    public static int layer2Nodes = 18;
    public static int layer3Nodes = 18;
    public static int layer4Nodes = 0;
    public static int outputLayerNodes = 4;
    public static int layerMenge = 4;

    //Evolutions Eigenschaften
    public static int POPULATIONSIZE = 1000;

    public static int reihen = 10;
    public static int spalten = 10;

    public static int maxEnergy = 100;
    public static final int startLength = 5;
}
