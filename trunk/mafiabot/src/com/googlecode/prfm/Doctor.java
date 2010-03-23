package com.googlecode.prfm;
class Doctor implements Role{
	static String name;
	static Team team;
	public Doctor(Team nteam) {
		name = "doctor";
		team = nteam;
	}
	public void nightAction(Player target) {
		target.saved = true;
	}
}
