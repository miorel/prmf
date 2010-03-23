package com.googlecode.prmf;

public class VoteTracker {
	
	int[] votes;
	int noLynchVotes;
	int notVoting;
	int numberOfPlayers;
	Player[] players;
	public VoteTracker(Player[] players){
		this.players = players;
		notVoting = players.length;
		numberOfPlayers = players.length;
	}
	
	public void status()
	{
		String toPrint = "";
		for (int i=0;i<numberOfPlayers;++i)
		{
			String toAdd = "";
			if (votes[i] == 0) continue;
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
	
	public int newVote(Player voter, Player voted)
	{
		/*in Player, votes for -1 is no vote
		 * -2 is no lynch
		 *  0-# of players-1 represents the actual players
		 *  
		 *  the return types represent who was lynched: -1 is no majority, -2 is nolynch
		 *   >=0 is the player lynched
		 */
		if (!voter.isAlive)
			return -1;
		if (!voted.isAlive)
			return -1;
		switch (voter.votedFor)
		{
		case -2:
			--noLynchVotes;
			break;
		case -1:
			break;
		default:
			--votes[voter.votedFor];
		}
		++votes[voted.ID];
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
