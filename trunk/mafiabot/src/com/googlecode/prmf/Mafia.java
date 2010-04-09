package com.googlecode.prmf;

class Mafia extends Role {
	private Team team;
	final private String name;
	//private Player target;

	public Mafia(Team nteam) {
		name = "mafia";
		team = nteam;
		super.setTarget(null);
	}

	public String getName() {
		return this.name;
	}

	public Team getTeam() {
		return this.team;
	}

	public Player getTarget() {
		return super.getTarget();
	}

	boolean checkNightAction(String message) {
		String[] splitMessage = message.split(" ");
		if(splitMessage.length >= 2 && splitMessage[0].equals(":~kill"))
			return true;
		return false;
	}

	public void resolveNightAction() {
		System.err.println(getTarget() + " is the target");
		getTarget().setAlive(false);
	}
}
