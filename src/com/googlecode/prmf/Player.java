package com.googlecode.prmf;

public class Player {
	private String name;
	private Role role;

	private boolean isAlive;
	private int votedFor;


	public Player(String name) {
		setName(name);
		isAlive = true;
		role = null;
		votedFor = -1;
	}
	
	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj instanceof Player) {
			Player temp = (Player) obj;
			ret = this.getName().equals(temp.getName());
		}
		return ret;
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

	public int hashCode() {
		return name.hashCode();
	}

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
}
