package com.googlecode.prfm;
class Vigilante implements Role{
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
