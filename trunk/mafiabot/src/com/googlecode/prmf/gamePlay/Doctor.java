package com.googlecode.prmf.gamePlay;

class Doctor extends Role {
	final private Town team;

	public Doctor(Town nteam) {
		setName("doctor");
		team = nteam;
	}

	public void nightAction(Player target) {
		setNightAction(true);
	}

	@Override
	public Town getTeam() {
		return this.team;
	}
	
	@Override
	public String resolveNightAction()
	{
		StringBuilder toReturn = new StringBuilder();
		getTarget().setNightLives(getTarget().getNightLives()+1);
		return toReturn.toString();
	}

	@Override
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to heal a player every night. Note that you cannot heal yourself. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}

}
