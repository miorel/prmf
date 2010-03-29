package com.googlecode.prmf;
import java.util.LinkedList;

class Town extends Team{
	String name;
	LinkedList<Player> list;
	public Town (){
		name = "Town";
		list = new LinkedList<Player>();
	}
	public boolean hasWon(Player[] players)
	{
		for (Player p : players)
		{
			if (p.role.team.name.equals("MafiaTeam"))
				return false;
		}
		return true;
	
	}
	
	public boolean contains(Player player)
	{
		return list.contains(player);
	}
}
