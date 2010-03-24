package com.googlecode.prmf;
import java.util.LinkedList;

class Town implements Team{
	String name;
	LinkedList<Player> list;
	public Town (){
		name = "Town";
		list = new LinkedList<Player>();
	}
}
