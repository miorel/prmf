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
package upt;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Miorel-Lucian Palii
 */
public class AppMain {
	public static void main(String[] arg) {
		final Frame frame = new Frame();
		final Animation animation = new Animation(frame);
		new Thread(animation).start();
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				animation.start();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				animation.destroy();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				animation.stop();
			}

		});

		frame.setSize(400, 400);
		frame.setVisible(true);
	}
}
