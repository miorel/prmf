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
import java.io.IOException;

/**
 * @author Miorel-Lucian Palii
 */
public class Game {
	private final Model model;
	private final View view;
	private final Controller controller;
	
	private Thread drawingThread;
	
	public Game(Component component) throws IOException {
		this.model = new Model(new Map(getClass().getResourceAsStream("map.txt")), 3, 6);
		this.view = new View(this.model, component);
		this.controller = new Controller(this.model, component);
	}
	
	public void init() {
		this.drawingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					Game.this.view.paint();
					try {
						Thread.sleep(20);
					}
					catch(InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		});
		this.drawingThread.start();
	}
	
	public void destroy() {
		this.drawingThread.interrupt();
	}
}
