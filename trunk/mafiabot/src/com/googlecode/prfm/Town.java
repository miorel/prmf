package com.googlecode.prfm;
import java.util.LinkedList;

class Town implements Team{
	String name;
	LinkedList<Player> list;
	public Town (){
		name = "Town";
		list = new LinkedList<Player>();
	}
}
