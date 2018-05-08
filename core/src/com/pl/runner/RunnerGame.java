package com.pl.runner;


import com.badlogic.gdx.Game;
import com.pl.runner.screens.GameplayScreen;

public class RunnerGame extends Game {
	@Override
	public void create () {
	setScreen(new GameplayScreen());
	}

}
