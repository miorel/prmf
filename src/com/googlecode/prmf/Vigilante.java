package com.googlecode.prmf;
class Vigilante implements Role{
	static String name;
	static Team team;
	public Vigilante(Team nteam) {
		name = "doctor";
		team = nteam;
	}
	public void nightAction(Player target) {
		target.vigilanted = true;
	}
}
