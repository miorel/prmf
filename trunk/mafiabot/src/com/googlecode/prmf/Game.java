package com.googlecode.prmf;

import com.googlecode.prmf.starter.InputThread;

public class Game{
	private Player[] players;
	private String gameStarter;
	private int numMafia =1;
	private boolean dayStart = true;
	private InputThread inputThread;
	private MafiaGameState state;
	
	Pregame pregame;
	Day day;
	Night night;
	
	public Game(String gameStarter, InputThread inputThread)	{
		this.gameStarter = gameStarter;
		this.inputThread = inputThread;
		pregame = new Pregame(gameStarter);
		state = pregame;
	}


	public void receiveMessage(String line)
	{
		if(state.receiveMessage(line, inputThread))
		{
			swapState();
		}
	}
	
	public Game(String gameStarter, InputThread inputThread, boolean dayStart, int numMafia)
	{
		this(gameStarter, inputThread);
		this.dayStart = dayStart;
		this.numMafia = numMafia;
	}
	
	public void swapState()
	{
		if(state instanceof Pregame)
		{
			players = pregame.getPlayerList();
			day = new Day(players);
			night = new Night(players);
			if(dayStart)
				state = day;
			else 
			{
				state = night;
			}
		}
		else if(state instanceof Day)
		{
			state = night;
		}
		else
		{
			state = day;
		}
	}
	
	public String getGameStarter()
	{
		return gameStarter;	
	}
	
}
