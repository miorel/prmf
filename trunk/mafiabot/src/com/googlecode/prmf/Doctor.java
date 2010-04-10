package com.googlecode.prmf;

class Doctor extends Role {
	// TODO mark as final any fields you don't plan on changing
	private Team team;
	private String name;
	private Player target;

	public Doctor(Team nteam) {
		name = "doctor";
		team = nteam;
	}

	public void nightAction(Player target) {
		System.out.println("i save u");
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
		toReturn.append("As a ").append(getName()).append(", you have the ability to heal a player every night. Note that you cannot heal yourself. You win when all threats to the town are eliminated");
		return toReturn.toString();
	}

}
