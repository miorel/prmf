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
package rpg;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Miorel-Lucian Palii
 */
public class Controller implements KeyListener {
	private final Model model;
	
	public Controller(Model model, Component component) {
		this.model = model;
		component.addKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			this.model.getEntity().move(-1, 0);
			break;
		case KeyEvent.VK_RIGHT:
			this.model.getEntity().move(1, 0);
			break;
		case KeyEvent.VK_UP:
			this.model.getEntity().move(0, -1);
			break;
		case KeyEvent.VK_DOWN:
			this.model.getEntity().move(0, 1);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
