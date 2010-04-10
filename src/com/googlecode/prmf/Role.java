package com.googlecode.prmf;

public abstract class Role {
	private Team team;
	private String name;
	private Player target;
	//TODO implement some sort of priority system, so the bot knows which roles' night actions come first
	//REBUTTAL aren't they technically simultaneous?
	//eh i dont think so... if agent alters someone, that needs to happen before cop checks right?
	boolean nightAction(String message, Player[] players) {
		//TODO check night action for validity here instead of before?
		System.err.println("entering night action");
		boolean toReturn = false;
		if (!checkNightAction(message))
			return toReturn;
		String[] splitMessage = message.split(" ");
		String targetName = splitMessage[1];
		System.err.println(targetName);
		for (Player p : players)
			if (p.getName().equals(targetName))
			{
				System.err.println("there should be a target now");
				this.target = p;
				toReturn = true;
				break;
			}
		return toReturn;
			
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
		System.err.println("TARGET SHOULD BE: " + target);
		return this.target;
	}
	
	public void setTarget(Player p)
	{
		this.target = p;
	}

	boolean checkNightAction(String message) {
		return true;
	}

	public abstract String description();

	void resolveNightAction() {
		if(!hasNightAction()) 
			return;

	}
}
