package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

public interface MafiaGameState {
	public boolean receiveMessage(Game game, String message, IOThread inputThread);
	
	public void status();
}
