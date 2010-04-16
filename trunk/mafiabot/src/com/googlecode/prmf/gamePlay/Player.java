package com.googlecode.prmf.gamePlay;

public class Player {
	private String name;
	private Role role;

	private boolean isAlive;
	private int votedFor;
	private int nightLives;
	private boolean targetted;

	public Player(String name) {
		setName(name);
		isAlive = true;
		role = null;
		votedFor = -1;
		targetted = false;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj instanceof Player) {
			Player temp = (Player) obj;
			ret = this.getName().equals(temp.getName());
		}
		return ret;
	}
	
	public void endNight()
	{
		if (getNightLives() < 0)
			setAlive(false);
		resetNightLives();
		targetted = false;
	}

	public String getName() {
		return name;
	}

	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role)
	{
		this.role = role;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isAlive()
	{
		return isAlive;
	}
	
	public int getVote()
	{
		return votedFor;
	}
	
	public void setAlive(boolean status)
	{
		this.isAlive = status;
	}
	
	public void setVote(int vote)
	{
		this.votedFor = vote;
	}
	
	public void resetNightLives()
	{
		setNightLives(0);
	}
	
	public void setNightLives(int setTo)
	{
		targetted = true;
		nightLives = setTo;
	}
	
	public int getNightLives()
	{
		return nightLives;
	}
	
	public boolean getTargetted()
	{
		return targetted;
	}
}
