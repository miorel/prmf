package com.googlecode.prmf;

class Vigilante extends Role {
	// TODO mark as final any fields you don't plan on changing
	private Team team;
	private String name;
	private Player target;

	public Vigilante(Team nteam) {
		name = "vigilante";
		team = nteam;
	}

	public void nightAction(Player target) {
		this.target = target;
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
