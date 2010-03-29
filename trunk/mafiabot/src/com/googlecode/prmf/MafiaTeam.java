package com.googlecode.prmf;
import java.util.LinkedList;

class MafiaTeam extends Team{
	String name;
	LinkedList<Player> list;
	public MafiaTeam() {
		name = "MafiaTeam";
		list = new LinkedList<Player>();
	}
	
	public boolean hasWon(Player[] players)
	{
		//itt we check for victory
		//if mafia make up at least 50% of the living players return true
		int nonMafia = 0, mafia = 0;
		for (Player p : players)
		{
			if (this.contains(p))
				++mafia;
			else
				++nonMafia;
		}
		return !(nonMafia > mafia);
		// TODO could also do nonMafia <= mafia, in fact that might be more intuitive
	}
	
	public boolean contains(Player player)
	{
		return list.contains(player);
	}
}
