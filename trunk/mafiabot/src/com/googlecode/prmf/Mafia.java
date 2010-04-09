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
		if(((MafiaTeam)team).agreeOnTarget())
		{
			System.err.println(getTarget() + " is the target");
			getTarget().setAlive(false);
		}
		else
			System.err.println("No target was selected.");
	}
	
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you get to kill someone every night. You win when the mafia achieve parity with non-mafia");
		System.err.println(toReturn);
		return toReturn.toString();
	}
}
