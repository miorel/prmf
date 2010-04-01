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
		pregame = new Pregame(gameStarter , inputThread);
		state = pregame;
		// TODO: add day and night constructors
	}

	public void pregameMessage(String line) 
	{
		pregame.receiveMessage(line);
	}
	
	public void dayMessage(String line)
	{
		day.receiveMessage(line);
	}
	
	public void nightMessage(String line)
	{
		night.receiveMessage(line);
	}
	
	public void receiveMessage(String line)
	{
		if(state instanceof Pregame)
		{
			pregameMessage(line);
		}
		else if(state instanceof Day)
		{
			dayMessage(line);
		}
		else if(state instanceof Night)
		{
			nightMessage(line);
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
			if(dayStart)
				state = day;
			else 
				state = night;
		}
		else if(state instanceof Day)
			state = night;
		else
			state = day;
	}
	
	
}
