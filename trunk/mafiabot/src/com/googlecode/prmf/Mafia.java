package com.googlecode.prmf;
class Mafia extends Role{
	static String name;
	static Team team;
	public Mafia(Team nteam) {
		name = "mafia";
		team = nteam;
	}
	public void nightAction(Player target) {
		target.mafiaTarget = true;
	}
}
