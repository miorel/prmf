package com.googlecode.prmf;

// this class is poorly implemented
// it's a bad idea to write code relying on it until it gets fixed

class Mafia extends Role{
	private Team team;
	private String name;
	private Player target;
	public Mafia(Team nteam) {
		name = "mafia";
		team = nteam;
		target = null;
	}
	public void nightAction(Player target) {
		this.target = target;
	}
	
	public String getName()
	{
		return name;
	}
}
