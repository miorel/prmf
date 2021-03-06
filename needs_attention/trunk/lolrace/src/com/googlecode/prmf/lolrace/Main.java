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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * @author Preston Mueller
 */
public class Main {
	public static void main(String[] arg) throws IOException, InterruptedException {
		final JFrame frame = new JFrame("Race to the finish!!!");

		if(System.getProperty("os.name").equals("Mac OS X"))
			frame.setSize(600, 600);
		else
			frame.setSize(610, 600);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				frame.dispose();
				System.exit(0);
			}
		});

		Game game = new Game(frame);

		for(;;) {
			game.play();
		}
	}
}

