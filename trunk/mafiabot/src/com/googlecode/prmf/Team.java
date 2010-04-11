package com.googlecode.prmf;

import java.util.LinkedList;
import java.util.List;

public abstract class Team {
	private String name; 
	private List<Player> list;
	
	// TODO why is an array of players passed as an argument?
	//most win conditions don't just involve the team in isolation, but rather the team with respect to the rest of the game
	//without knowing some things about the other players it's impossible to determine if you've won or not
	public boolean hasWon(Player[] players) {
		//TODO functionality
		return false;
	}

	
	public void addPlayer(Player p) {
		getList().add(p);
	}
	

	public boolean contains(Player player) {
		return list.contains(player);
	}

	public String getName() {
		return name;
	}
	
	public List<Player> getList()
	{
		System.out.println(list);
		return list;
	}
	
	public void createList()
	{
		list = new LinkedList<Player>();
	}
	
}
