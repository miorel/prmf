package com.googlecode.prmf;
class Citizen extends Role{
	static String name;
	static Team team;
	public Citizen(Team nteam) {
		name = "citizen";
		team = nteam;
	}
	public void nightAction(Player target) {}
}
