package com.googlecode.prmf;
class Vigilante extends Role{
	static String name;
	static Team team;
	public Vigilante(Team nteam) {
		name = "vigilante";
		team = nteam;
	}
	public void nightAction(Player target) {
		target.vigged = true;
	}
}
