package com.googlecode.prmf;

// this class is poorly implemented
// it's a bad idea to write code relying on it until it gets fixed
@Deprecated
class Mafia extends Role{
	static Team team;
	public Mafia(Team nteam) {
		name = "mafia";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}
	public void nightAction(Player target) {
		target.isMafiaTarget = true;
	}
}
