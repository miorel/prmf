package com.googlecode.prmf;

class Citizen extends Role {
	// TODO mark as final any fields you don't plan on changing
	private Team team;
	private String name;
	private Player target;

	public Citizen(Team nteam) {
		name = "citizen";
		team = nteam;
	}

	public void nightAction(Player target) {
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
		toReturn.append("You are a " + getName() + "! ");
		toReturn.append("As a " + getName() + ", you have no special powers. You win when all threats to the town are eliminated");
		return toReturn.toString();
	}
}
