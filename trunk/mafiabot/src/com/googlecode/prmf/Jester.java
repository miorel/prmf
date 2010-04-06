package com.googlecode.prmf;

//this class is poorly implemented
//it's a bad idea to write code relying on it until it gets fixed

class Jester extends Role{
	private Team team;
	private String name;
	private Player target;
	public Jester(Team nteam) {
		name = "jester"; //TODO: Jester not capital.
		team = nteam;

	}

	public void nightAction(Player target) {
	}
	public String getName()
	{
		return this.name;
	}
	
	public Team getTeam()
	{
		return this.team;
	}
	
	public Player getTarget()
	{
		return this.target;
	}
}
