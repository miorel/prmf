package com.googlecode.prmf.gamePlay;

class Jester extends Role {
	final private JesterTeam team;

	public Jester(JesterTeam nteam) {
		setName("jester");
		team = nteam;
	}

	public void nightAction(Player target) {
	}
	
	@Override
	public boolean checkNightAction(String message)
	{
		return false;
	}

	@Override
	public JesterTeam getTeam() {
		return this.team;
	}

	@Override
	public String description()
	{
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have no special powers. You win if and only if you are lynched by the town.");
		return toReturn.toString();
	}
}
