package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;

public interface MafiaGameState {
	public boolean receiveMessage(Game game, String message, IOThread inputThread);
	
	public void status();
}
