package com.googlecode.prmf.gamePlay;

class Doctor extends Role {
	final private Town team;

	public Doctor(Town nteam) {
		setName("doctor");
		team = nteam;
	}

	@Override
	public Town getTeam() {
		return this.team;
	}
	
	@Override
	public boolean checkNightAction(String message)
	{
		boolean result = false;
		message = message.toLowerCase();
		if (message.substring(2).startsWith("heal"))
			result = true;
		return result;
	}
	
	@Override
	public String resolveNightAction()
	{
		if (getTarget() == null)
			return "You didn't give a target!";
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
