package com.googlecode.prmf;

import java.util.List;

//TODO why not make this public?
abstract class Team {
	String name; //TODO default visibility is almost as bad as public
	List<Player> list;
	
	// TODO why is an array of players passed as an argument?
	public boolean hasWon(Player[] players) {
		//TODO functionality
		return false;
	}
	
	public boolean contains(Player player) {
		return list.contains(player);
	}
}
