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

//currently not in use
public class Vigilante extends Role {
	private final Town team;

	public Vigilante(Town nteam) {
		setName("vigilante");
		team = nteam;
	}

	@Override
	public Town getTeam() {
		return this.team;
	}
	
	@Override
	public boolean checkNightAction(String message) {
		boolean result = false;
		message = message.toLowerCase();
		if (message.substring(2).startsWith("attack"))
			result = true;
		return result;
	}
	
	@Override
	public String description() {
		return String.format("You are a %s! As a %s, you have the ability to kill a player at night. Bear in mind you can only do this once, so choose wisely. You win when all threats to the town are eliminated.", getName(), getName());
	}
}
