package com.googlecode.prmf.gamePlay;

import java.util.LinkedList;
import java.util.List;

public abstract class Team {
	private List<Player> list;
	private String name;
	
	public Team() {
		list = new LinkedList<Player>();
	}
	
	// TODO why is an array of players passed as an argument?
	//most win conditions don't just involve the team in isolation, but rather the team with respect to the rest of the game
	//without knowing some things about the other players it's impossible to determine if you've won or not
	public boolean hasWon(Player[] players) {
		//TODO functionality
		return false;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	
	public void addPlayer(Player p) {
		getList().add(p);
	}
	

	public boolean contains(Player player) {
		return getList().contains(player);
	}
	
	public List<Player> getList()
	{
		return list;
	}
	
}