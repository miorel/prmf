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
	public boolean checkNightAction(String message)
	{
		boolean result = false;
		message = message.toLowerCase();
		if (message.substring(2).startsWith("check"))
			result = true;
		return result;
	}
	
	@Override
	public String resolveNightAction() {
		if (getTarget() == null)
			return "You didn't give a target!";
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(getTarget()).append(" is a ").append(getTarget().getRole().getName());
		return toReturn.toString();
	}

	@Override
	public String description() {
		//TODO reimplement using String.format()
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to investigate players to determine whether or not they are mafia. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}

}
