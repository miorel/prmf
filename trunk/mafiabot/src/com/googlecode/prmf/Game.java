package com.googlecode.prmf;

public class Game {
	//TODO default visibility is almost as bad as public
	Player[] players;
	String gameStarter;
	int numMafia =1;
	boolean dayStart = true;
	
	public Game(String gameStarter)	{
		this.gameStarter = gameStarter;
		
	}

	public void startGame() {
		Pregame pregame = new Pregame(gameStarter);
		pregame.startGame();
		players = pregame.getPlayerList();
		Day day = new Day(players);
		Night night = new Night(players);
	}
	
	public Game(String gameStarter, boolean dayStart, int numMafia)
	{
		this(gameStarter);
		this.dayStart = dayStart;
		this.numMafia = numMafia;
	}
}
