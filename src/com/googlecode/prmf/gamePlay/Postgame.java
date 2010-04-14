package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;

public class Postgame implements MafiaGameState {
	IOThread inputThread;

	public Postgame(IOThread inputThread) {
		this.inputThread = inputThread;
	}

	public boolean receiveMessage(Game game, String line, IOThread inputThread)
	{
		return true;
	}	
	
	public void swapState(Game game, MafiaGameState newState)
	{
		game.setState(newState);
	}
	
	public void status()
	{
		inputThread.sendMessage(inputThread.getChannel(), "The game is now over");
	}
}