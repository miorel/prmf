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
package com.googlecode.prmf.gamePlay;

import java.util.LinkedList;

import com.googlecode.prmf.connection.IOThread;

public class Postgame implements MafiaGameState {
	// TODO default visibility is almost as bad as public
	IOThread inputOutputThread;
	Player[] players;

	public Postgame(IOThread inputOutputThread, Player[] players) {
		this.inputOutputThread = inputOutputThread;
		this.players = players;
		wrapUp();
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
	
	public void wrapUp()
	{
		// TODO declare things as what they're used not what they are
		LinkedList<Team> teamsWon = new LinkedList<Team>();
		for(Player player : getPlayerList())
		{
			Team current = player.getRole().getTeam();
			if (current.hasWon(getPlayerList()))
			{
				if(!teamsWon.contains(current))
					teamsWon.add(current);
			}
		}
		
		StringBuilder ret = new StringBuilder();
		ret.append("Team ");
		for(Team team : teamsWon) {
			//TODO don't + when you append()
			ret.append(" " + team.getName());
			ret.append(" consisting of: " + team.getPlayers());
		}
		ret.append(" has won!");
		inputOutputThread.sendMessage(inputOutputThread.getChannel(),ret.toString());
	}
	
	public Player[] getPlayerList()
	{
		return players;
	}
	
	public void endState(Game game)
	{
		
	}
}