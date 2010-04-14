package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;

import java.util.LinkedList;

public class Game{
	private Player[] players;
	private TimerThread timerThread;
	private String gameStarter;
	private IOThread inputThread;
	private MafiaGameState state;
	
	private Pregame pregame;
	private Postgame postgame;
	private boolean inProgress;
	
	public Game(String gameStarter, IOThread inputThread)	{
		this.gameStarter = gameStarter;
		this.inputThread = inputThread;
		pregame = new Pregame(gameStarter, inputThread);
		state = getPregame();
		timerThread = new TimerThread(inputThread);
		setProgress(false);
	}
	
	public void setProgress(boolean set)
	{
		this.inProgress = set;
	}
	
	public boolean isInProgress()
	{
		return inProgress;
	}

	public void receiveMessage(String line)
	{
		state.receiveMessage(this, line, inputThread);
	}
    //TODO: why is this receiving an IO thread? one was given in the constructor	
	public Game(String gameStarter, IOThread inputThread, boolean dayStart, int numMafia)
	{
		this(gameStarter, inputThread);
	}
 
	public boolean isOver() {
		boolean result = false;
		LinkedList<String> teamsWon = new LinkedList<String>();
		for(Player player : getPlayerList()) {
			if(player.getRole().getTeam().hasWon(players)){
				result = true;
				String teamName = player.getRole().getTeam().getName();
				if(!teamsWon.contains(teamName))
					teamsWon.add(teamName);
			}
		}
		if(result){
			postgame = new Postgame(inputThread);
			StringBuilder ret = new StringBuilder();
			ret.append("Team:");
			for(String teamName : teamsWon)
					ret.append(" " + teamName);
			ret.append(" has won");
			inputThread.sendMessage(inputThread.getChannel(),ret.toString());
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
		getTimerThread().getTimer().interrupt();
	}
	
	public MafiaGameState getState()
	{
		return state;
	}
	
	public void setState(MafiaGameState state)
	{
		if(isOver())
		{
			state = new Postgame(inputThread);
			for(int i=0;i<getPlayerList().length;++i)
			{
				inputThread.sendMessage("MODE",inputThread.getChannel(), "+v "+players[i].getName());
			}
			inputThread.sendMessage("MODE",inputThread.getChannel(), "-m");
		}
		else
		{
			if(state instanceof Day)
			{
				for(int i=0;i<getPlayerList().length;++i)
				{
					if(players[i].isAlive())
					inputThread.sendMessage("MODE",inputThread.getChannel(), "+v "+players[i].getName());
				}
			}
			else if(state instanceof Night)
			{
				for(int i=0;i<getPlayerList().length;++i)
				{
					inputThread.sendMessage("MODE",inputThread.getChannel(), "-v "+players[i].getName());
				}
	
			}
		}
		this.state = state;
		this.state.status();	
	}
	
	public Player[] getPlayerList()
	{
		if(isInProgress())
		{
			if (players == null)
				players = getPregame().getPlayerArray();
			return players;
		}	
		return getPregame().getPlayerArray();
	}
	public void startTimer()
	{
		timerThread = new TimerThread(inputThread);
		getTimerThread().getTimer().start();
	}
	
	public Pregame getPregame()
	{
		return pregame;
	}
	
	public Postgame getPostgame()
	{
		return postgame;
	}
	
}
