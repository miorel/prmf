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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * @author Preston Mueller
 */
public class Racer extends JComponent {
	private static final long serialVersionUID = 4391042457384928947L;

	private BufferedImage currentImg;
	private BufferedImage img1;
	private BufferedImage img2;
	private int ci;
	int lane;
	private int currentHeight;

	public Racer(String imgPath, int l) {
		this.currentHeight = 475;
		this.lane = l;
		try {
			this.img1 = ImageIO.read(this.getClass().getResource("gfx/" + imgPath + ".png"));
			this.img2 = ImageIO.read(this.getClass().getResource("gfx/" + imgPath + "2.png"));
			this.currentImg = this.img1;
			this.ci = 1;
		}
		catch(IOException e) {
			e.printStackTrace(System.out);
			System.out.println(e);
		}
	}

	public void dance() throws InterruptedException {
		for(int i = 1; i < 25; ++i) {
			Thread.sleep(100);
			changePic();
			update(this.getGraphics());
		}
	}

	public void reset() {
		this.currentImg = this.img1;
		this.currentHeight = 475;
		this.setBounds(75 * this.lane, this.currentHeight, 27, 90);
		this.update(this.getGraphics());
	}

	public void die() {
		try {
			this.currentImg = ImageIO.read(this.getClass().getResource("gfx/hit.png"));
		}
		catch(IOException e) {
			System.out.println(e);
		}
		setBounds(getX() - 20, getY(), 66, 61);
		update(this.getGraphics());
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(this.currentImg, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize() {
		if(this.currentImg == null) {
			return new Dimension(100, 100);
		}
		else {
			return new Dimension(currentImg.getWidth(null), currentImg.getHeight(null));
		}
	}

	public boolean move() {
		boolean ret;
		this.currentHeight -= 6;
		if(this.currentHeight < 30) {
			setBounds(75 * this.lane, 30, 27, 90);
			ret = false;
		}
		else {
			setBounds(75 * this.lane, this.currentHeight, 27, 90);
			ret = true;
		}
		changePic();
		update(this.getGraphics());
		return ret;
	}

	public void changePic() {
		if(this.ci == 1) {
			this.ci = 2;
			this.currentImg = this.img2;
		}
		else {
			this.ci = 1;
			this.currentImg = this.img1;
		}
	}
}
