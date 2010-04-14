package com.googlecode.prmf.gamePlay;

class Cop extends Role {
	final private Town team;

	public Cop(Town nteam) {
		setName("cop");
		team = nteam;
	}

	@Override
	public Town getTeam() {
		return this.team;
	}
	
	@Override
	public String resolveNightAction() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(getTarget()).append(" is a ").append(getTarget().getRole().getName());
		return toReturn.toString();
	}

	@Override
	public String description() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to investigate players to determine whether or not they are mafia. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}

}
