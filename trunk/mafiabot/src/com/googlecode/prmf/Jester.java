package com.googlecode.prmf;

// TODO it appears that Role is not an interface at this time
class Jester implements Role{
	static String name;
	static Team team;
	public Jester(Team nteam) {
		name = "jester";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}
	public void nightAction(Player target) {}
}
