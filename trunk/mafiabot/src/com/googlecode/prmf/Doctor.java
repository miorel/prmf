package com.googlecode.prmf;

//this class is poorly implemented
//it's a bad idea to write code relying on it until it gets fixed

class Doctor extends Role{
	private Team team;
	private String name;
	private Player target;
	public Doctor(Team nteam) {
		name = "doctor";
		team = nteam;

	}
	public void nightAction(Player target) {
		target.wasSaved = true;
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
