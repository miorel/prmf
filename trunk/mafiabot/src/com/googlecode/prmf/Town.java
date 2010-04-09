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
	
	public boolean hasWon(Player[] players) {
		boolean result = true;
		for(Player p: players) {
			// TODO instanceof is evil, try to avoid it
			if(p.getRole().getTeam() instanceof MafiaTeam && p.isAlive())
				result = false;
		}
		return result;

	}

	public boolean contains(Player player) {
		return list.contains(player);
	}
}
