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
	
	boolean checkNightAction(String message)
	{
		String[] splitMessage = message.split(" ");
		if(splitMessage.length >= 2 && splitMessage[0].equals(":~kill"))
			return true;
		return false;
	}
	
	public void resolveNightAction()
	{
		//TODO kill the target~!
	}
}
