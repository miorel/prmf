package com.googlecode.prmf;

import java.util.LinkedList;
import java.util.List;

class MafiaTeam extends Team {
	private String name;
	private List<Player> list; 
	public MafiaTeam() {
		name = "MafiaTeam";
		list = new LinkedList<Player>();
	}

	public String getName() {
		return name;
	}
	
	public boolean hasWon(Player[] players)
	{
		//itt we check for victory
		//if mafia make up at least 50% of the living players return true
		int nonMafia = 0, mafia = 0;
		for (Player p : players)
		{
			if (!p.isAlive())
				continue;
			if (p.getRole().getTeam().equals(this)) // TODO this looks like a bug to me
				++mafia;
			else
				++nonMafia;
		}
		return nonMafia <= mafia;
	}
	
	public boolean contains(Player player) {
		return list.contains(player);
	}
}
