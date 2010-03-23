package com.googlecode.prfm;
import java.util.LinkedList;
class JesterTeam implements Team{
	String name;
	LinkedList<Player> list;
	public JesterTeam() {
		name = "JesterTeam";
		list = new LinkedList<Player>();
	}
}
