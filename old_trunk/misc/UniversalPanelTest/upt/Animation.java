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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author Miorel-Lucian Palii
 */
public class Animation implements Runnable {
	private final Component component;
	private final Object lock;
	
	private boolean paused;
	private boolean running;
	
	private int dx;
	
	public Animation(Component component) {
		this.component = component;
		this.lock = new Object();
		setRunning(true);
		setPaused(true);
	}

	private void setPaused(boolean value) {
		synchronized(this.lock) {
			if(this.paused != value) {
				this.paused = value;
				this.lock.notifyAll();
			}
		}
	}

	private boolean isPaused() {
		synchronized(this.lock) {
			return this.paused;
		}
	}

	private void setRunning(boolean value) {
		synchronized(this.lock) {
			this.running = value;
		}
	}

	private boolean isRunning() {
		synchronized(this.lock) {
			return this.running;
		}
	}
	
	public void start() {
		setPaused(false);
	}

	public void stop() {
		setPaused(true);
	}
	
	public void destroy() {
		setRunning(false);
	}

	@Override
	public void run() {
		System.out.println("Animation init");
		while(isRunning() && !Thread.interrupted()) {
			synchronized(this.lock) {
				if(isPaused()) {
					System.out.println("Animation stop");
					try {
						this.lock.wait();
					}
					catch(InterruptedException e) {
						Thread.currentThread().interrupt();
						continue;
					}
					System.out.println("Animation start");
				}
			}
			
			Dimension dim = this.component.getSize();
			Image img = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
			Graphics g = img.getGraphics();
			g.clearRect(0, 0, dim.width, dim.height);
			g.setColor(Color.RED);
			g.drawString("Hello World!", 100 + dx, 100);
			this.component.getGraphics().drawImage(img, 0, 0, this.component);
			
			dx = (dx + 1) % 200;
			
			try {
				Thread.sleep(10);
			}
			catch(InterruptedException e) {
			}
		}
		System.out.println("Animation destroy");
	}
}
