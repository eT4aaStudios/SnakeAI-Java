package com.snake.ai.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.snake.ai.main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Snake Ai by eT4aa";
		cfg.height = 750;
		cfg.width = 800;
		cfg.x = (int) (cfg.width / -1.5f);
		cfg.vSyncEnabled = true;
		new LwjglApplication(new main(), cfg);
	}
}
