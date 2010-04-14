package com.googlecode.prmf.gamePlay;

class Citizen extends Role {
	final private Town team;
	final private String name;

	public Citizen(Town nteam) {
		name = "citizen";
		team = nteam;
		super.setNightAction(true);
	}
	
	public void resetNightAction()
	{
		
	}

	public void nightAction(Player target) {
	}
	
	public boolean hasNightAction()
	{
		return false;
	}

	public String getName() {
		return this.name;
	}

	public Town getTeam() {
		return this.team;
	}

	public Player getTarget() {
		return super.getTarget();
	}
	
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have no special powers. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}
}
