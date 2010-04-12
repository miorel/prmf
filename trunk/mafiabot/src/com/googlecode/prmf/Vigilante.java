package com.googlecode.prmf;

class Vigilante extends Role {
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

	public String getName() {
		return this.name;
	}

	public Team getTeam() {
		return this.team;
	}

	public Player getTarget() {
		return this.target;
	}

	
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to kill a player that you suspect is mafia. Bear in mind you can only do this once, so choose wisely. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}
}
