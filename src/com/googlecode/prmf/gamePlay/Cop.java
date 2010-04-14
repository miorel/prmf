package com.googlecode.prmf.gamePlay;

class Cop extends Role {
	final private Town team;
	final private String name;

	public Cop(Town nteam) {
		name = "cop";
		team = nteam;
	}

	public void nightAction(Player target) {
		System.out.println("i chek u");
	}

	public String getName() {
		return this.name;
	}

	public Town getTeam() {
		return this.team;
	}
	
	public String resolveNightAction() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(getTarget()).append(" is a ").append(getTarget().getRole().getName());
		return toReturn.toString();
	}

	public String description() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to investigate players to determine whether or not they are mafia. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}

}
