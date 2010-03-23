package com.googlecode.prfm;
class Mafia implements Role{
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
