package com.googlecode.prmf;

class Town extends Team{
	private String name;
	 
	public Town (){
		name = "Town";
		super.createList();
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
		return getList().contains(player);
	}
}
