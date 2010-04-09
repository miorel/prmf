package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

public class Game{
	private Player[] players;
	private TimerThread timerThread;
	private String gameStarter;
	private IOThread inputThread;
	private MafiaGameState state;
	
	Pregame pregame;
	Day day;
	Night night;
	
	public Game(String gameStarter, IOThread inputThread)	{
		this.gameStarter = gameStarter;
		this.inputThread = inputThread;
		pregame = new Pregame(gameStarter, inputThread);
		state = pregame;
		timerThread = new TimerThread(inputThread);
	}


	public void receiveMessage(String line)
	{
		if(state.receiveMessage(this, line, inputThread))
		{
			//swapState();
			;
		}
	}
	
	public Game(String gameStarter, IOThread inputThread, boolean dayStart, int numMafia)
	{
		this(gameStarter, inputThread);
	}

	public void swapState()
	{
		state.swapState(this);
	}
 
	public boolean isOver() {
		boolean result = false;
		for(Player player: players) {
			if(player.getRole().getTeam().hasWon(players))
				result = true;
		}
		return result;
	}

	public String getGameStarter() {
		return gameStarter;
	}
	
	public TimerThread getTimerThread()
	{
		return timerThread;
	}

	public void stopTimer() {
		getTimerThread().timer.interrupt();
	}
	
	public MafiaGameState getState()
	{
		return state;
	}
	
	public void setState(MafiaGameState state)
	{
		this.state = state;
	}
	
	public MafiaGameState getDay()
	{
		return day;
	}
	
	public MafiaGameState getNight()
	{
		return night;
	}
	
	public Player[] getPlayerList()
	{
		if(players == null)
			players = pregame.getPlayerList();
		return players;
			
	}
	
	public void makeDay()
	{
		day = new Day(getPlayerList(), inputThread);
	}
	
	public void makeNight()
	{
		night = new Night(getPlayerList(), inputThread);
	}
	
	public void startTimer()
	{
		timerThread = new TimerThread(inputThread);
		getTimerThread().timer.start();
	}
	
}
