package com.googlecode.prmf;

import com.googlecode.prmf.starter.InputThread;

public interface MafiaGameState {
	
	public boolean receiveMessage(String message, InputThread inputThread);
	
}
