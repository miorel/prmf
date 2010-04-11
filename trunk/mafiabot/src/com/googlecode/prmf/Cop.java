package com.googlecode.prmf;

class Cop extends Role {
	// TODO mark as final any fields you don't plan on changing
	private Team team;
	private String name;

	public Cop(Team nteam) {
		name = "cop";
		team = nteam;
	}

	public void nightAction(Player target) {
		System.out.println("i chek u");
	}

	public String getName() {
		return this.name;
	}

	public Team getTeam() {
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
