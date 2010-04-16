package com.googlecode.prmf.gamePlay;

class Mafia extends Role {
	private MafiaTeam team;

	public Mafia(MafiaTeam nteam) {
		setName("mafia");
		team = nteam;
		setTarget(null);
		setNightAction(false);
	}
	
	@Override
	public boolean nightAction(String message, Player[] players)
	{
		boolean temp = super.nightAction(message, players);
		temp = temp && getTeam().agreeOnTarget();
		if(temp)
			setNightAction(true);
		return temp;
	}

	@Override
	public MafiaTeam getTeam() {
		return this.team;
	}

	@Override
	public Player getTarget() {
		return super.getTarget();
	}

	@Override
	public boolean checkNightAction(String message) {
		String[] splitMessage = message.split(" ");
		if(splitMessage.length >= 2 && splitMessage[0].equals(":~kill"))
			return true;
		return false;
	}

	@Override
	public String resolveNightAction() {
		StringBuilder toReturn = new StringBuilder();
		if(getTeam().agreeOnTarget())
		{
			toReturn.append(getTarget()).append(" is the target");
			getTarget().setNightLives(getTarget().getNightLives()-1);
		}
		else
			toReturn.append("No target was selected.");
		return toReturn.toString();
	}
	
	@Override
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you get to kill someone every night. You win when the mafia achieve parity with non-mafia. ");
		toReturn.append(getTeam());
		return toReturn.toString();
	}
}
