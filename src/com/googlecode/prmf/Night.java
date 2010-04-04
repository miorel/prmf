package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;


public class Night implements MafiaGameState
{
	Player[] players;
	
	public Night(Player[] players, IOThread inputThread)
	{
		
	}
	
	public boolean receiveMessage(String line, IOThread inputThread)
	{
		// TODO why is the channel name hardcoded?
		inputThread.sendMessage("#UFPT" , " WE ARE IN NIGHT");
		return false;
	}
}
