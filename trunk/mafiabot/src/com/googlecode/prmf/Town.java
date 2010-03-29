package com.googlecode.prmf;
import java.util.LinkedList;

class Town extends Team{
	String name; //TODO default visibility is almost as bad as public
	LinkedList<Player> list; //TODO declare things as what they're used not as what they are
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
