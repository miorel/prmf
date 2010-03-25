package com.googlecode.prmf;
class Mafia extends Role{
	static String name;
	static Team team;
	public Mafia(Team nteam) {
		name = "mafia";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}
	public void nightAction(Player target) {
		target.mafiaTarget = true;
	}
}
