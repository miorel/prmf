package com.googlecode.prfm;

public class Day {
	Timer timer;
	VoteTracker votes;
	Player[] players;
	
	public Day(int time, Player[] players)
	{
		timer = new Timer(time);
		votes = new VoteTracker(players);
		this.players = players;
		
	}
	
	public void parseMessage(String message)
	{
		
		//stuff
	}
	
	public int processVote()
	{
		
		return -1;
	}
}
