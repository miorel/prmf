package com.googlecode.prmf;

class Jester extends Role {
	// TODO mark as final any fields you don't plan on changing
	private Team team;
	private String name;
	private Player target;

	public Jester(Team nteam) {
		name = "jester";
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
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have no special powers. You win if and only if you are lynched by the town.");
		return toReturn.toString();
	}
}
