package com.googlecode.prfm;

public class Day {
	Timer timer;
	VoteTracker votes;
	
	public Day(int time, Player[] players)
	{
		timer = new Timer(time);
		votes = new VoteTracker(players);
		
	}
	
	public void parseMessage(String message)
	{
		
		//stuff
	}
}
