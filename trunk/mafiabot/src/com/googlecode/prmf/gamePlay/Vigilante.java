package com.googlecode.prmf.gamePlay;

class Vigilante extends Role {
	private final Town team;

	public Vigilante(Town nteam) {
		setName("vigilante");
		team = nteam;
	}

	@Override
	public Town getTeam() {
		return this.team;
	}
	
	@Override
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to kill a player at night. Bear in mind you can only do this once, so choose wisely. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}
}
