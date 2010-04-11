package com.googlecode.prmf;

class Mafia extends Role {
	private MafiaTeam team;
	final private String name;
	//private Player target;

	public Mafia(MafiaTeam nteam) {
		name = "mafia";
		team = nteam;
		super.setTarget(null);
		super.setNightAction(false);
	}
	
	public boolean nightAction(String message, Player[] players)
	{
		boolean temp = super.nightAction(message, players);
		
		temp = temp && getTeam().agreeOnTarget();
		if(temp)
			setNightAction(true);
		return temp;
	}

	public String getName() {
		return this.name;
	}

	public MafiaTeam getTeam() {
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

	public String resolveNightAction() {
		StringBuilder toReturn = new StringBuilder();
		if(((MafiaTeam)team).agreeOnTarget())
		{
			toReturn.append(getTarget()).append(" is the target");
			getTarget().setAlive(false);
		}
		else
			toReturn.append("No target was selected.");
		return toReturn.toString();
	}
	
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you get to kill someone every night. You win when the mafia achieve parity with non-mafia");
		return toReturn.toString();
	}
}
