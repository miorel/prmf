package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

public abstract class Role {
	private Team team;
	private String name;
	private Player target;
	//TODO implement some sort of priority system, so the bot knows which roles' night actions come first
	
	void nightAction(String message, Player[] players) {
		//TODO check night action for validity here instead of before?
		if (!checkNightAction(message))
			return;
		String[] splitMessage = message.split(" ");
		String targetName = splitMessage[1];
		for (Player p : players)
			if (p.getName().equals(targetName))
				this.target = p;
	}
	
	boolean hasNightAction()
	{
		return true;
	}
	
	public String getName()
	{
		return name;
	}
	
	boolean checkNightAction(String message)
	{
		return true;
	}
	
	String description()
	{
		return "You are a " + getName() + "!\n";
	}
}
