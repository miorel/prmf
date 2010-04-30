/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.googlecode.prmf.corleone.game;

import com.googlecode.prmf.corleone.connection.IOThread;

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

	public MafiaGameState getState() { // TODO this method should be private
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
