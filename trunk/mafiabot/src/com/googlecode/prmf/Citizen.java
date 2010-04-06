package com.googlecode.prmf;

//this class is poorly implemented
//it's a bad idea to write code relying on it until it gets fixed
class Citizen extends Role {	
	private Team team;
	private String name;
	public Citizen(Team nteam) {
		name = "citizen";
		team = nteam;
	}

	public void nightAction(Player target) {
	}
}
