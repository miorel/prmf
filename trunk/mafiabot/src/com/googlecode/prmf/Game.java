package com.googlecode.prmf;

public class Game {

	Player[] players;
	String gameStarter;
	
	public Game(String gameStarter)
	{
		this.gameStarter = gameStarter;
	}
	public void startGame()
	{
		Pregame pregame = new Pregame(gameStarter);
		pregame.startGame();
	}
}
