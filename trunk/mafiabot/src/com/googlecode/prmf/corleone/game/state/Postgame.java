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
package com.googlecode.prmf.corleone.game.state;

import java.util.LinkedList;

import com.googlecode.prmf.corleone.connection.IOThread;
import com.googlecode.prmf.corleone.game.Game;
import com.googlecode.prmf.corleone.game.Player;
import com.googlecode.prmf.corleone.game.team.Team;
import com.googlecode.prmf.merapi.util.Iterators;
import com.googlecode.prmf.merapi.util.Strings;

//TODO: add some more cool stuff to this class! better results, game summaries, etc; lots of ways to go with this.
public class Postgame implements MafiaGameState {
	public IOThread inputOutputThread;
	public Player[] players;

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
		//this prints a list of all the winners!
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
			ret.append(team.getName() + " consisting of: " + team.getPlayers() +" has won!");
			inputOutputThread.sendMessage(inputOutputThread.getChannel(),ret.toString());
		}

		LinkedList<String> playersRoles = roleReveal();
		inputOutputThread.sendMessage(inputOutputThread.getChannel(),Strings.join(", ", Iterators.iterator(playersRoles)));
	}

	public Player[] getPlayerList()
	{
		return players;
	}

	public void endState(Game game)
	{

	}

	private LinkedList<String> roleReveal()
	{
		LinkedList<String> playersRoles = new LinkedList<String>();
		for(Player p : players)
			playersRoles.add(p.getName()+" was "+ p.getRole().getName());
		return playersRoles;
	}
}