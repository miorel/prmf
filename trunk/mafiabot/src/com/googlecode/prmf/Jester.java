package com.googlecode.prmf;
// This is TWW2's favorite class.

class Jester extends Role{
	static Team team;
	public Jester(Team nteam) {
		name = "jester";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}

	public void nightAction(Player target) {
	}
}
