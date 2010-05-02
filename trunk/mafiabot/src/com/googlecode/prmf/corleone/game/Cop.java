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
package com.googlecode.prmf.corleone.game;

public class Cop extends Role {
	final private Town team;

	public Cop(Town nteam) {
		setName("cop");
		team = nteam;
	}

	@Override
	public Town getTeam() {
		return this.team;
	}
	
	@Override
	public boolean checkNightAction(String message)
	{
		boolean result = false;
		message = message.toLowerCase();
		if (message.substring(2).startsWith("check"))
			result = true;
		return result;
	}
	
	@Override
	public String resolveNightAction() {
		if (getTarget() == null)
			return "You didn't give a target!";
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(getTarget()).append(" is a ").append(getTarget().getRole().getName());
		return toReturn.toString();
	}

	@Override
	public String description() {
		//TODO reimplement using String.format()
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have the ability to investigate players to determine whether or not they are mafia. You win when all threats to the town are eliminated.");
		return toReturn.toString();
	}

}

