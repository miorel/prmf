package com.googlecode.prmf.gamePlay;

public interface MafiaGameState {
	public boolean receiveMessage(Game game, String message);
	public void status();
	public void endState(Game game);
}
