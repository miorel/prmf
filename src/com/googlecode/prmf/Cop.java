package com.googlecode.prmf;
class Cop extends Role{
	static String name;
	static Team team;
	public Cop(Team nteam) {
		name = "cop";
		team = nteam;
	}
	public void nightAction(Player target) {
		System.out.println(target.checked);
	}
}
