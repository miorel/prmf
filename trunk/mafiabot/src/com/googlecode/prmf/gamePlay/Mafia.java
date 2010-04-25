/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
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
		if (getTarget() == null)
			return "You didn't give a target!";
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
	public String description() {
		// TODO reimplement using String.format()
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you get to kill someone every night. You win when the mafia achieve parity with non-mafia. ");
		toReturn.append(getTeam());
		return toReturn.toString();
	}
}
