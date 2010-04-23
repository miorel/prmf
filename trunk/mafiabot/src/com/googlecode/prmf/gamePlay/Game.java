package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;

public class Game{
	private Player[] players;
	private TimerThread timerThread;
	private String gameStarter;
	private IOThread inputOutputThread;
	private MafiaGameState state;
	
	private Pregame pregame;
	private Postgame postgame;
	private boolean inProgress;
	
	public Game(String gameStarter, IOThread inputOutputThread)	{
		this.gameStarter = gameStarter;
		this.inputOutputThread = inputOutputThread;
		pregame = new Pregame(gameStarter, inputOutputThread);
		state = getPregame();
		timerThread = new TimerThread(inputOutputThread);
		setProgress(false);
	}
	
	public void setProgress(boolean set) {
		this.inProgress = set;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void receiveMessage(String line) {
		state.receiveMessage(this, line);
	}

	public Game(String gameStarter, IOThread inputOutputThread, boolean dayStart, int numMafia) {
		this(gameStarter, inputOutputThread);
	}
 
	public boolean isOver() {
		boolean result = false;

		for(Player player : getPlayerList()) {
			if(player.getRole().getTeam().hasWon(players)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public IOThread getIOThread()
	{
		return inputOutputThread;
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
		state.endState(this);
	}
	
	public MafiaGameState getState()
	{
		return state;
	}
	
	public void setState(MafiaGameState state)
	{
		if(isOver())
		{
			state = new Postgame(inputOutputThread, getPlayerList());
			for(int i=0;i<getPlayerList().length;++i) { // TODO rewrite using sexier foreach syntax
				inputOutputThread.sendMessage("MODE",inputOutputThread.getChannel(), "+v "+players[i].getName());
			}
			inputOutputThread.sendMessage("MODE",inputOutputThread.getChannel(), "-m");
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
	
	public void startTimer() {
		// TODO the fact that you have to do it like this is just weird
		timerThread = new TimerThread(inputOutputThread);
		getTimerThread().getTimer().start();
	}

	// TODO this is a bad method, it shouldn't exist imo
	public Pregame getPregame() {
		return pregame;
	}

	// TODO this is a bad method, it shouldn't exist imo
	public Postgame getPostgame() {
		return postgame;
	}
	
}
