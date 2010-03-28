package com.googlecode.prmf;

public class Game {

	Players[] players;
	String gameStarter;
	
	public Game(String gameStarter)
	{
		this.gameStarter = gameStarter;
	}
	public static void startGame()
	{
		Pregame pregame = new Pregame(gameStarter);
		pregame.startGame();
	}
}
