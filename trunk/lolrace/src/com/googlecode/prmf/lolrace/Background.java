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
package com.googlecode.prmf.lolrace;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * @author Preston Mueller
 */
public class Background extends JComponent {
	private static final long serialVersionUID = -4831891858524142765L;

	@Override
	public void paintComponent(Graphics g) {
		g.drawLine(400, 0, 400, 600);
		g.drawLine(400, 270, 600, 270);
		g.drawLine(400, 45, 600, 45);
		g.drawString("Scoreboard", 466, 30);
		g.drawString("Gator", 420, 74);
		g.drawString("Penguin", 420, 125);
		g.drawString("Cat", 420, 174);
		g.drawString("Human", 420, 230);
	}
}
