package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

public class VoteTracker {
	
	private int[] votes;
	private int noLynchVotes;
	private int numberOfPlayers;
	private Player[] players;
	
	public VoteTracker(Player[] players) {
		this.players = players;
		numberOfPlayers = players.length;
		noLynchVotes = 0;
		votes = new int[players.length];
		clearAllVotes();
	}

	private void clearAllVotes() {
		for (Player p : players)
		{
			p.setVote(-1);
		}
	}
	
	public void status(IOThread inputThread) {
		StringBuilder toPrint = new StringBuilder();
	
		for (int i=0;i<numberOfPlayers;++i)
		{
			StringBuilder toAdd = new StringBuilder();
			
			//must check if player is alive because player can !quit is set to dead.
			if (votes[i] == 0 || !players[i].isAlive()) continue;
			if (toPrint.length() > 0)
				toAdd.append(", ");
			toAdd.append(players[i].getName());
			toAdd.append(" has ");
			toAdd.append(votes[i]);
			if(votes[i] > 1)
				toAdd.append(" votes");
			else
				toAdd.append(" vote");
			toPrint.append(toAdd);
		}
		if (noLynchVotes > 0)
		{
			StringBuilder toAdd = new StringBuilder();
			if (toPrint.length() > 0)
				toAdd.append(", ");
			toAdd.append("No Lynch has ");
			toAdd.append(noLynchVotes);
			if(noLynchVotes == 1)
				toAdd.append(" vote");
			else
				toAdd.append(" votes");
			toPrint.append(toAdd);
		}
		if(toPrint.length() == 0)
		{
			toPrint.append("There are currently no votes.");
			
		}
		toPrint.append(".");
		inputThread.sendMessage("#UFPT", toPrint.toString());
	}
	
	// TODO probably better (i.e. OOP-ish) to pass in a Player object rather than an int voter 
	public int newVote(int voter, int voted , IOThread inputThread)
	{
		/*in Player, votes for -1 is no vote
		 * -2 is no lynch
		 *  0-# of players-1 represents the actual players
		 *  
		 *  the return types represent who was lynched: -1 is no majority, -2 is nolynch
		 *   >=0 is the player lynched
		 */

		//day.processVote(..) takes care of whether voted is valid player
		if (!players[voter].isAlive())
			return -1;
		if ( voted >= 0)
		{
			if( players[voter].getVote() >= 0)
			{
				--votes[players[voter].getVote()];
			}
			else if( players[voter].getVote() == -2)
			{
				--noLynchVotes;
			}
			players[voter].setVote(voted);
			++votes[voted];
		}
		else if( voted == -1)
		{
			if(players[voter].getVote() >= 0)
			{
				//uncount his previous vote
				--votes[players[voter].getVote()];
				players[voter].setVote(-1);
			}
			else if( players[voter].getVote() == -2)
			{
				--noLynchVotes;
				players[voter].setVote(-1);
			}
			else
			{
				// if command was to unvote AND players vote was already -1 .. 
				// he had no vote to begin with...!unvote command should do nothing
				return -1;
			}
		}
		else if( voted == -2)
		{
			if(players[voter].getVote() >= 0 )
			{
				--votes[players[voter].getVote()];
			}
			if( players[voter].getVote() != -2)
				++noLynchVotes;
			
			players[voter].setVote(-2);
		}
		
		status(inputThread);
		return checkMajority();
	}
	
	public int checkMajority() 
	{
		int result = -1;
		int needed = 0;
		for (Player p : players)
			if (p.isAlive())
				++needed;
		needed = needed/2 + 1;
		if (noLynchVotes >= needed)
			result = -2;
		for (int i=0;i<numberOfPlayers;++i)
			if(votes[i] >= needed)
				result = i;	
		return result;
	}
	
}
