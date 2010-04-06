package com.googlecode.prmf;
import java.util.LinkedList;
import java.util.List;

class JesterTeam extends Team {
	String name; //TODO default visibility is almost as bad as public
	List<Player> list;

	public JesterTeam() {
		name = "JesterTeam";
		list = new LinkedList<Player>();
	}
	
	public boolean hasWon(Player[] players)
	{
		// TODO the argument passed to this method is never actually used... 
		for (Player p : list)
		{
			if (p.wasLynched)
				return true;
		}
		return false;
	}
	
	public boolean contains(Player player)
	{
		return list.contains(player);
	}
}