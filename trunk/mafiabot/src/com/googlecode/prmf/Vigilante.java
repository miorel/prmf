package com.googlecode.prmf;

//this class is poorly implemented
//it's a bad idea to write code relying on it until it gets fixed
class Vigilante extends Role{
	private Team team;
	private String name;
	private Player target;
	public Vigilante(Team nteam) {
		name = "vigilante";
		team = nteam;
	}
	public void nightAction(Player target) {
		this.target = target;
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
