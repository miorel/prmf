package com.googlecode.prmf;

import com.googlecode.prmf.starter.InputThread;


public class Night implements MafiaGameState
{
	Player[] players;
	
	public Night(Player[] players)
	{
		
	}
	
	public boolean receiveMessage(String line, InputThread inputThread)
	{
		return false;
	}
}
