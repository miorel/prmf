package com.googlecode.prfm;
class Citizen implements Role{
	static String name;
	static Team team;
	public Citizen(Team nteam) {
		name = "citizen";
		team = nteam;
	}
	public void nightAction(Player target) {}
}
