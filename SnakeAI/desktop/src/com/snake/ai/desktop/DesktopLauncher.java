package com.snake.ai.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.snake.ai.main;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();

        cfg.setTitle("SnakeGame Ai by eT4aa");
        cfg.setIdleFPS(1);
        cfg.setWindowIcon("ic_launcher.png");
        cfg.useVsync(true);
        cfg.setWindowSizeLimits(1080, 540, 9999, 9999);
		/*cfg.height = 540;
		cfg.width = 1080;
		cfg.x = cfg.width / 2;
		cfg.y = cfg.height / 2;*/
        new Lwjgl3Application(new main(), cfg);
    }
}
