package com.googlecode.prmf;

public class VoteTracker {
	
	int[] votes;
	int noLynchVotes;
	int notVoting;
	int numberOfPlayers;
	Player[] players;
	
	public VoteTracker(Player[] players) {
		this.players = players;
		notVoting = players.length;
		numberOfPlayers = players.length;
		noLynchVotes = 0;
	}
	
	public void status() {
		String toPrint = "";
		for (int i=0;i<numberOfPlayers;++i)
		{
			String toAdd = "";
			//must check if player is alive because player can !quit is set to dead.
			if (votes[i] == 0 && players[i].isAlive) continue;
			if (toPrint.length() > 0)
				toAdd += ", ";
			toAdd += players[i].name;
			toAdd += " has ";
			toAdd += i;
			toAdd += " votes";
			toPrint += toAdd;
		}
		if (noLynchVotes > 0)
		{
			String toAdd = "";
			toAdd += "No Lynch has ";
			toAdd += noLynchVotes;
		}
		toPrint += ".";
	}
	
	// TODO probably better (i.e. OOP-ish) to pass in a Player object rather than an int voter 
	public int newVote(int voter, int voted)
	{
		/*in Player, votes for -1 is no vote
		 * -2 is no lynch
		 *  0-# of players-1 represents the actual players
		 *  
		 *  the return types represent who was lynched: -1 is no majority, -2 is nolynch
		 *   >=0 is the player lynched
		 */

		//day.processVote(..) takes care of whether voted is valid player
		
		if ( voted >= 0)
		{
			if( players[voter].votedFor >= 0)
			{
				--votes[players[voter].votedFor];
			}
			else if( players[voter].votedFor == -2)
			{
				--noLynchVotes;
			}
			players[voter].votedFor = voted;
			++votes[voted];
		}
		else if( voted == -1)
		{
			if(players[voter].votedFor >= 0)
			{
				//uncount his previous vote
				--votes[players[voter].votedFor];
				players[voter].votedFor = -1;
			}
			else if( players[voter].votedFor == -2)
			{
				--noLynchVotes;
				players[voter].votedFor = -1;
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
			if(players[voter].votedFor >= 0 )
			{
				--votes[players[voter].votedFor];
			}
			if( players[voter].votedFor != -2)
				++noLynchVotes;
			
			players[voter].votedFor = -2;
		}

		status();
		return checkMaj();
	}
	
	public int checkMaj()
	{
		int needed = numberOfPlayers/2 + 1;
		if (noLynchVotes > needed)
			return -2;
		for (int i=0;i<numberOfPlayers;++i)
			if(votes[i] > needed)
				return i;	
		return -1;
	}
	
}
