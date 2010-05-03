/* JTrisApplet - Play a popular tetromino game inside a Java applet
   Version 4
   Copyright (C) 2010 Brian Nezvadovitz
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;

public class BoardGUI extends Canvas {
	
	private final Color Color_bg = new Color(  0,   0,   0);
	private final Color Color_i = new Color(  0, 240, 240);
	private final Color Color_j = new Color(  0,   0, 240);
	private final Color Color_l = new Color(240, 160,   0);
	private final Color Piece_o = new Color(240, 240,   0);
	private final Color Color_s = new Color(  0, 240,   0);
	private final Color Color_t = new Color(160, 240,   0);
	private final Color Color_z = new Color(240,   0,   0);
	
	public BoardGUI() {
		setSize(100, 210);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color_bg);
		g.fillRect(0, 0, 100, 210);
	}
	
}
