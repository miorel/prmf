package com.googlecode.prmf;

//this class is poorly implemented
//it's a bad idea to write code relying on it until it gets fixed
@Deprecated
class Jester extends Role{
	static Team team;
	public Jester(Team nteam) {
		name = "jester";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}

	public void nightAction(Player target) {
	}
}
