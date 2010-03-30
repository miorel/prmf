package com.googlecode.prmf;

class Cop extends Role{
	static String name;
	static Team team;
	public Cop(Team nteam) {
		name = "cop";
		team = nteam;
		// TODO team is a class (i.e. static) variable
		// whereas nteam is an instance variable
		// this seems bad
	}
	public void nightAction(Player target) {
		System.out.println(target.checked);
	}
}
