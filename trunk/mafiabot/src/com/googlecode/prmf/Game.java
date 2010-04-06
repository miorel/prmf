package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

public class Game{
	private Player[] players;
	private TimerThread timerThread;
	private String gameStarter;
	private int numMafia =1; // TODO this field is never used
	private boolean dayStart = true;
	private IOThread inputThread;
	private MafiaGameState state;
	
	Pregame pregame;
	Day day;
	Night night;
	
	public Game(String gameStarter, IOThread inputThread)	{
		this.gameStarter = gameStarter;
		this.inputThread = inputThread;
		pregame = new Pregame(gameStarter);
		state = pregame;
		timerThread = new TimerThread(inputThread);
	}


	public void receiveMessage(String line)
	{
		if(state.receiveMessage(line, inputThread))
		{
			swapState();
		}
	}
	
	public Game(String gameStarter, IOThread inputThread, boolean dayStart, int numMafia)
	{
		this(gameStarter, inputThread);
		this.dayStart = dayStart;
		this.numMafia = numMafia;
	}
	
	// TODO this method is idiotic
	/*
	 * why don't you add a swapState() method to the state class and then this
	 * entire ugly thing can become
	 * 
	 * public void swapState() {
	 *     state.swapState()
	 * }
	 * 
	 * better yet, make the states change the state from within their receiveMessage
	 * implementation
	 */
	
	public void swapState()
	{
		if(state instanceof Pregame)
		{
			players = pregame.getPlayerList();
			day = new Day(players, inputThread);
			timerThread.timer.start();
			night = new Night(players, inputThread);
			if(dayStart)
				state = day;
			else 
			{
				state = night;
			}
		}
		else if(state instanceof Day)
		{
			night = new Night(players, inputThread);
			state = night;
		}
		else
		{
			day = new Day(players, inputThread);
			//TODO: make sure timer isn't already started~
			timerThread.timer.start();
			state = day;
		}
	}
	
	public String getGameStarter()
	{
		return gameStarter;	
	}
	
	public void stopTimer()
	{
		timerThread.timer.interrupt();
	}
	
}
