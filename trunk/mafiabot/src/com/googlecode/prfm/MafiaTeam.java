package com.googlecode.prfm;
import java.util.LinkedList;

class MafiaTeam implements Team{
	String name;
	LinkedList<Player> list;
	public MafiaTeam() {
		name = "MafiaTeam";
		list = new LinkedList<Player>();
	}
}
