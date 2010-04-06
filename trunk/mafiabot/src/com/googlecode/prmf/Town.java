package com.googlecode.prmf;
import java.util.LinkedList;
import java.util.List;

class Town extends Team{
	private String name;
	private List<Player> list; 
	public Town (){
		name = "Town";
		list = new LinkedList<Player>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean hasWon(Player[] players)
	{
		for (Player p : players)
		{
			if (p.role.getTeam() instanceof MafiaTeam)
				return false;
		}
		return true;
	
	}
	
	public boolean contains(Player player)
	{
		return list.contains(player);
	}
}
