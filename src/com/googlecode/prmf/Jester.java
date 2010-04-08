package com.googlecode.prmf;

class Jester extends Role {
	// TODO mark as final any fields you don't plan on changing
	private Team team;
	private String name;
	private Player target;

	public Jester(Team nteam) {
		name = "jester"; // TODO Jester not capital.
		team = nteam;
	}

	public void nightAction(Player target) {
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
