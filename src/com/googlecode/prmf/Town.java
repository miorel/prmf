package com.googlecode.prmf;

class Town extends Team{
//	private String name;
	 
	public Town() {
		// name = "Town";
		// super.createList();
	}

	public String getName() {
		return "Town";
	}
	
	public boolean hasWon(Player[] players) {
		boolean result = true;
		for(Player p: players) {
			if(!p.getRole().getTeam().equals(this) && p.isAlive())
			{
				result = false;
				break;
			}
		}
		return result;

	}

	public boolean contains(Player player) {
		return getList().contains(player);
	}
}
