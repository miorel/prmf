package com.googlecode.prmf.gamePlay;

class Citizen extends Role {
	final private Town team;

	public Citizen(Town nteam) {
		setName("citizen");
		team = nteam;
		setNightAction(true);
	}
	
	@Override
	public void resetNightAction()
	{
		setNightAction(true);
	}

	public void nightAction(Player target) {
	}
	
	@Override
	public boolean hasNightAction()
	{
		return false;
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
		toReturn.append("As a ").append(getName()).append(", you have no special powers. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}
}