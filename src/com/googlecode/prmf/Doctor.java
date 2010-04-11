package com.googlecode.prmf;

class Doctor extends Role {
	final private Team team;
	final private String name;

	public Doctor(Team nteam) {
		name = "doctor";
		team = nteam;
	}

	public void nightAction(Player target) {
		setNightAction(true);
		System.out.println("i save u");
	}

	public String getName() {
		return this.name;
	}

	public Team getTeam() {
		return this.team;
	}
	
	public String resolveNightAction()
	{
		StringBuilder toReturn = new StringBuilder();
		getTarget().setNightLives(getTarget().getNightLives()+1);
		return toReturn.toString();
	}

	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to heal a player every night. Note that you cannot heal yourself. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}

}
