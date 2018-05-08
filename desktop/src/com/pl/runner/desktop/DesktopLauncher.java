package com.pl.runner.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pl.runner.RunnerGame;
import com.pl.runner.utils.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.GAME_WIDTH;
		config.height = Constants.GAME_HEIGHT;
		config.title = Constants.GAME_TITLE;
		new LwjglApplication(new RunnerGame(), config);
	}
}
