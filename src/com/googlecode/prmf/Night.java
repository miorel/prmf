package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;


public class Night implements MafiaGameState
{
	private Player[] players;
	private IOThread inputThread;
	private boolean[] actionComplete;
	
	public Night(Player[] players, IOThread inputThread)
	{
		this.players = players;
		this.inputThread = inputThread;
		actionComplete = new boolean[players.length];
	}
	
//	TODO add a timer to night
	
	public boolean receiveMessage(String line, IOThread inputThread)
	{
		
		// TODO why is the channel name hardcoded?
		inputThread.sendMessage("#UFPT" , " WE ARE IN NIGHT");
		
		//TODO parse message, do appropriate action
		return isNightOver()     ;
	}
	
	public void introduction()
	{
		//TODO tell each player his night message: role, night action, teammates if mafia, etc
	}
	
	public boolean isNightOver()
	{
		//TODO check if all actions are in
		return false;
	}
	
	
}
