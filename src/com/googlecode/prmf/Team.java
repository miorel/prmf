package com.googlecode.prmf;

import java.util.List;

public abstract class Team {
	private String name; //TODO default visibility is almost as bad as public
	private List<Player> list;
	
	// TODO why is an array of players passed as an argument?
	public boolean hasWon(Player[] players) {
		//TODO functionality
		return false;
	}
	
	public void addPlayer(Player p)
	{
		list.add(p);
	}
	
	public boolean contains(Player player) {
		return list.contains(player);
	}
	
	public String getName()
	{
		return name;
	}
}
