package com.googlecode.prmf;

public abstract class Role {
	private Team team;
	private String name;
	private Player target;
	//TODO implement some sort of priority system, so the bot knows which roles' night actions come first
	//REBUTTAL aren't they technically simultaneous?
	//eh i dont think so... if agent alters someone, that needs to happen before cop checks right?
	void nightAction(String message, Player[] players) {
		//TODO check night action for validity here instead of before?
		if (!checkNightAction(message))
			return;
		String[] splitMessage = message.split(" ");
		String targetName = splitMessage[1];
		for (Player p : players)
			if (p.getName().equals(targetName))
			{
				this.target = p;
				break;
			}
	}
	
	boolean hasNightAction() {
		return true;
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

	boolean checkNightAction(String message) {
		return true;
	}

	String description() {
		return "You are a " + getName() + "!\n";
	}

	void resolveNightAction() {

	}
}
