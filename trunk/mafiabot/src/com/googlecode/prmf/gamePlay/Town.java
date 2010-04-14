package com.googlecode.prmf.gamePlay;

class Town extends Team{

	public Town() {
		setName("Town");
	}

	@Override
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
}
