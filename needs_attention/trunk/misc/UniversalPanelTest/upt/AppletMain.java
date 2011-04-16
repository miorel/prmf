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

import java.applet.Applet;

/**
 * @author Miorel-Lucian Palii
 */
public class AppletMain extends Applet {
	private static final long serialVersionUID = -5765366938568648287L;

	private Animation animation;
	
	@Override
	public void destroy() {
		this.animation.destroy();
		this.animation = null;
	}

	@Override
	public void init() {
		this.animation = new Animation(this);
		new Thread(this.animation).start();
	}

	@Override
	public void start() {
		this.animation.start();
	}

	@Override
	public void stop() {
		this.animation.stop();
	}
}
