package com.googlecode.prmf;

class Vigilante extends Role{
	static Team team;
	public Vigilante(Team nteam) {
		name = "vigilante";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}
	public void nightAction(Player target) {
		target.wasVigged = true;
	}
}
