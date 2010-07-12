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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

/**
 * @author Preston Mueller
 */
public class View {
	private Background bg;
	JLabel winrar;
	JButton again;
	private JButton exit;
	JFrame frame;
	private JLayeredPane gamePanel;
	HashMap<String,Racer> racers;
	JLabel gatorScore;
	JLabel penguinScore;
	JLabel catScore;
	JLabel humanScore;

	public View(JFrame f, HashMap<String,Racer> racers) {
		this.racers = racers;
		this.frame = f;
		this.gamePanel = new JLayeredPane();
		this.gamePanel.setOpaque(true);
		this.frame.setContentPane(gamePanel);

		this.frame.setBackground(Color.WHITE);
		this.frame.setLayout(null);
		bg = new Background();
		this.frame.add(bg, 1);
		bg.setBounds(0, 0, 600, 600);
		bg.setOpaque(true);
		winrar = new JLabel();
		this.frame.add(winrar, 2);
		winrar.setOpaque(true);
		winrar.setBounds(0, 15, 400, 15);
		winrar.setHorizontalAlignment(SwingConstants.CENTER);

		gatorScore = new JLabel();
		penguinScore = new JLabel();
		catScore = new JLabel();
		humanScore = new JLabel();

		this.frame.add(gatorScore, 2);
		this.frame.add(penguinScore, 2);
		this.frame.add(catScore, 2);
		this.frame.add(humanScore, 2);

		gatorScore.setBounds(530, 63, 60, 20);
		gatorScore.setHorizontalAlignment(SwingConstants.RIGHT);
		penguinScore.setBounds(530, 113, 60, 20);
		penguinScore.setHorizontalAlignment(SwingConstants.RIGHT);
		catScore.setBounds(530, 162, 60, 20);
		catScore.setHorizontalAlignment(SwingConstants.RIGHT);
		humanScore.setBounds(530, 219, 60, 20);
		humanScore.setHorizontalAlignment(SwingConstants.RIGHT);

		gatorScore.setOpaque(true);
		penguinScore.setOpaque(true);
		catScore.setOpaque(true);
		humanScore.setOpaque(true);

		BufferedImage tmpImg;
		try {
			tmpImg = ImageIO.read(this.getClass().getResource("gfx/again.png"));
			again = new JButton(new ImageIcon(tmpImg));
			tmpImg = ImageIO.read(this.getClass().getResource("gfx/exit.png"));
			exit = new JButton(new ImageIcon(tmpImg));
		}
		catch(IOException e) {
			System.out.println(e);
		}

		this.frame.add(again, 2);
		again.setBounds(433, 307, 137, 129);
		again.setOpaque(true);
		again.setBorderPainted(false);

		this.again.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(View.this.frame) {
					View.this.frame.notify();
				}
			}
		});

		this.again.setEnabled(false);
		this.frame.add(this.exit, 2);
		this.exit.setOpaque(true);
		this.exit.setBorderPainted(false);

		this.exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				View.this.frame.setVisible(false);
				View.this.frame.dispose();
				System.exit(0);
			}
		});

		this.exit.setBounds(450, 522, 103, 31);

		this.racers.put("gator", new Racer("gator", 1));
		this.racers.put("penguin", new Racer("penguin", 2));
		this.racers.put("cat", new Racer("cat", 3));
		this.racers.put("human", new Racer("human", 4));

		Set<String> s = racers.keySet();

		for(String x: s) {
			this.frame.add(this.racers.get(x), 3);
		}
		for(String x: s) {
			this.racers.get(x).setBounds(75 * this.racers.get(x).lane, 475, 27, 90);
			this.racers.get(x).setOpaque(true);
		}
	}

	public void resetRacers() {
		Set<String> s = this.racers.keySet();

		for(String x: s) {
			this.racers.get(x).reset();
		}
	}
}
