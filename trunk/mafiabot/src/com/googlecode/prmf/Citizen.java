package com.googlecode.prmf;
class Citizen extends Role{
	static String name;
	static Team team;
	public Citizen(Team nteam) {
		name = "citizen";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}
	public void nightAction(Player target) {}
}
