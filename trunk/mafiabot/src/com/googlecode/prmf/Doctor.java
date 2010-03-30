package com.googlecode.prmf;
class Doctor extends Role{
	static String name;
	static Team team;
	public Doctor(Team nteam) {
		name = "doctor";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}
	public void nightAction(Player target) {
		target.wasSaved = true;
	}
}
