package com.googlecode.prmf;

class Player {
	// TODO default visibility is almost as bad as public
	String name;
	Role role;

	boolean isAlive;
	boolean isMafiaTarget;
	boolean wasSaved;
	boolean wasChecked;
	boolean wasVigged;
	boolean wasLynched;
	int votedFor;
	// ID is the player's position in the list of players
	int ID;
	// TODO there is no reason for a player object to know its position in the list of players

	public Player(String name) {
		this.name = name;
		isAlive = true;
		role = null;
		votedFor = -1; // TODO what does this mean?
	}
	
	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj instanceof Player) {
			Player temp = (Player) obj;
			ret = this.name.equals(temp.name);
		}
		return ret;
	}

	public String getName() {
		return name;
	}

	public Role getRole() {
		return role;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return name;
	}
}
