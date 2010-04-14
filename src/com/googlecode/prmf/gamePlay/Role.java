package com.googlecode.prmf.gamePlay;

public abstract class Role {
	private Team team;
	private String name;
	private Player target;
	private boolean didNightAction;

	//TODO implement some sort of priority system, so the bot knows which roles' night actions come first
	//REBUTTAL aren't they technically simultaneous?
	//eh i dont think so... if agent alters someone, that needs to happen before cop checks right?
	boolean nightAction(String message, Player[] players) {
		boolean toReturn = false;
		if (!checkNightAction(message))
			return toReturn;
		String[] splitMessage = message.split(" ");
		String targetName = splitMessage[1];
		for (Player p : players)
			if (p.getName().equals(targetName))
			{
				this.target = p;
				toReturn = true;
				setNightAction(true);
				break;
			}
		return toReturn;
			
	}
	
	public void resetNightAction()
	{
		setNightAction(false);
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
	
	public void setTarget(Player p)
	{
		this.target = p;
	}

	boolean checkNightAction(String message) {
		return true;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public abstract String description();

	public String resolveNightAction() {
		return "";
	}
	
	public void setNightAction(boolean done)
	{
		didNightAction = done;
	}
	
	public boolean didNightAction()
	{
		return didNightAction;
	}

}
