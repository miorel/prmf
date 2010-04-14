package com.googlecode.prmf.gamePlay;

class JesterTeam extends Team {

	public JesterTeam() {
		setName("JesterTeam");
	}
	
	@Override
	public boolean hasWon(Player[] players)
	{
		// TODO after each lynch, check if it's a jester who just got lynched~
		return false;
	}
}
