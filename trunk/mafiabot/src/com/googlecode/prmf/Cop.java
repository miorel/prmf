package com.googlecode.prmf;

class Cop extends Role {
	// TODO mark as final any fields you don't plan on changing
	private Team team;
	private String name;
	private Player target;

	public Cop(Team nteam) {
		name = "cop";
		team = nteam;
	}

	public void nightAction(Player target) {
		System.out.println(target.wasChecked);
	}

	public String getName() {
		return this.name;
	}

	public Team getTeam() {
		return this.team;
	}

	public Player getTarget() {
		return this.target;
	}
}
