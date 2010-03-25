package com.googlecode.prmf;
import java.util.LinkedList;
class JesterTeam implements Team{
	String name;
	LinkedList<Player> list;
	public JesterTeam() {
		name = "JesterTeam";
		list = new LinkedList<Player>();
	}
	
	public boolean hasWon(Player[] players)
	{
		for (Player p : list)
		{
			if (p.wasLynched)
				return true;
		}
		return false;
	}
	
	public boolean contains(Player player)
	{
		for (Player p : list)
		{
			if (p.equals(player))
				return true;
		}
		return false;
	}
}
