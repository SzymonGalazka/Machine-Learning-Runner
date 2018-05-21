package com.pl.runner;


import com.badlogic.gdx.Game;
import com.pl.runner.screens.GameplayScreen;

public class RunnerGame extends Game {
	private int generation;
	@Override
	public void create () {
	setScreen(new GameplayScreen(this));
	generation++;
	}


	public int getGeneration() {
		return generation;
	}
}
