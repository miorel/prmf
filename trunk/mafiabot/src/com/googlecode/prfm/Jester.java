package com.googlecode.prfm;
class Jester implements Role{
	static String name;
	static Team team;
	public Jester(Team nteam) {
		name = "jester";
		team = nteam;
	}
	public void nightAction(Player target) {}
}
