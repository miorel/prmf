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
public class Jester extends Role {
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
	public String description() {
		//TODO reimplement using String.format()
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("You are a ").append(getName()).append("! ");
		toReturn.append("As a ").append(getName()).append(", you have no special powers. You win if and only if you are lynched by the town.");
		return toReturn.toString();
	}
}
