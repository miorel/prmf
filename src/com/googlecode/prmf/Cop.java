package com.googlecode.prmf;

//this class is poorly implemented
//it's a bad idea to write code relying on it until it gets fixed
@Deprecated
class Cop extends Role{
	private Team team;
	private String name;
	private Player target;
	public Cop(Team nteam) {
		name = "cop";
		team = nteam;
	}
	public void nightAction(Player target) {
		System.out.println(target.wasChecked);
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
