package com.googlecode.prfm;

class Player
{
	String name;
	Role role;
	boolean isAlive;
	boolean mafiaTarget;
	boolean saved;
	boolean checked;
	boolean vigged;
	int votedFor;
	//ID is the player's position in the list of players
	int ID;
	
	public Player(String name)
	{
		this.name = name;
		isAlive = true;
		role = null;
		votedFor = -1;
	}
	
	boolean equals(Player p)
	{
		if (this.name.equals(p.name))
			return true;
		return false;
	
	}
	
	public String toString()
	{
		return name;
	}

}
