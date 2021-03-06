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

import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;

/**
 * @author Preston Mueller
 */
public class Game {
	private HashMap<String,Integer> scoreboard;

	private View view;
	private HashMap<String,Racer> racers;

	public Game(JFrame frame) {
		this.scoreboard = new HashMap<String,Integer>();

		this.racers = new HashMap<String,Racer>();

		this.view = new View(frame, this.racers);

		reset();
		frame.setVisible(true);
	}

	public void reset() {
		this.scoreboard.put("gator", 0);
		this.scoreboard.put("penguin", 0);
		this.scoreboard.put("cat", 0);
		this.scoreboard.put("human", 0);

		this.view.gatorScore.setText(this.scoreboard.get("gator").toString());
		this.view.penguinScore.setText(this.scoreboard.get("penguin").toString());
		this.view.catScore.setText(this.scoreboard.get("cat").toString());
		this.view.humanScore.setText(this.scoreboard.get("human").toString());
	}

	public void exit() {
		this.view.frame.setVisible(false);
		this.view.frame.dispose();
		System.exit(0);
	}

	public void play() throws InterruptedException {
		this.view.again.setEnabled(false);
		this.view.again.update(this.view.again.getGraphics());
		this.view.winrar.setText("");
		this.view.winrar.update(this.view.winrar.getGraphics());
		Random randGen = new Random();
		boolean ret = true;
		String c = "";
		int toMove = 0;
		while(true) {
			toMove = randGen.nextInt(4);
			switch(toMove) {
			case 0:
				ret = this.view.racers.get("gator").move();
				c = "gator";
				break;
			case 1:
				ret = this.view.racers.get("penguin").move();
				c = "penguin";
				break;
			case 2:
				ret = this.view.racers.get("cat").move();
				c = "cat";
				break;
			case 3:
				ret = this.view.racers.get("human").move();
				c = "human";
				break;
			}
			int timeToNext = randGen.nextInt(75) + 30;
			Thread.sleep(timeToNext);

			if(!ret) {
				this.view.winrar.setText(c + " wins!!!!");
				this.scoreboard.put(c, Integer.valueOf(this.scoreboard.get(c).intValue() + 1));
				switch(toMove) {
				case 0:
					this.view.racers.get("penguin").die();
					this.view.racers.get("cat").die();
					this.view.racers.get("human").die();

					this.view.gatorScore.setText(this.scoreboard.get(c).toString());
					this.view.gatorScore.update(this.view.gatorScore.getGraphics());
					this.view.racers.get("gator").dance();
					break;
				case 1:
					this.view.racers.get("gator").die();
					this.view.racers.get("cat").die();
					this.view.racers.get("human").die();

					this.view.penguinScore.setText(this.scoreboard.get(c).toString());
					this.view.penguinScore.update(this.view.penguinScore.getGraphics());
					this.view.racers.get("penguin").dance();
					break;
				case 2:
					this.view.racers.get("gator").die();
					this.view.racers.get("penguin").die();
					this.view.racers.get("human").die();

					this.view.catScore.setText(this.scoreboard.get(c).toString());
					this.view.catScore.update(this.view.catScore.getGraphics());
					this.view.racers.get("cat").dance();
					break;
				case 3:
					this.view.racers.get("gator").die();
					this.view.racers.get("penguin").die();
					this.view.racers.get("cat").die();

					this.view.humanScore.setText(this.scoreboard.get(c).toString());
					this.view.humanScore.update(this.view.humanScore.getGraphics());
					this.view.racers.get("human").dance();
					break;
				}
				this.view.again.setEnabled(true);
				this.view.again.update(this.view.again.getGraphics());
				synchronized(this.view.frame) {
					this.view.frame.wait();
				}
				this.view.resetRacers();
				break;
			}
		}
	}
}
