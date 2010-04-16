package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;

public class Postgame implements MafiaGameState {
	IOThread inputOutputThread;

	public Postgame(IOThread inputOutputThread) {
		this.inputOutputThread = inputOutputThread;
	}

	public boolean receiveMessage(Game game, String line)
	{
		return true;
	}	
	
	public void swapState(Game game, MafiaGameState newState)
	{
		game.setState(newState);
	}
	
	public void status()
	{
		inputOutputThread.sendMessage(inputOutputThread.getChannel(), "The game is now over");
	}
}