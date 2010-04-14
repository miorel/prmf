package com.googlecode.prmf.gamePlay;
import java.util.LinkedList;
import java.util.List;

class JesterTeam extends Team {
	private String name;
	private List<Player> list;

	public JesterTeam() {
		name = "JesterTeam";
		list = new LinkedList<Player>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean hasWon(Player[] players)
	{
		// TODO after each lynch, check if it's a jester who just got lynched~
		return false;
	}
	
	public boolean contains(Player player) {
		return list.contains(player);
	}
}
