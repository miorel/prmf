package com.googlecode.prmf;

class Jester extends Role {
	final private Team team;
	final private String name;

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

	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have no special powers. You win if and only if you are lynched by the town.");
		return toReturn.toString();
	}
}
