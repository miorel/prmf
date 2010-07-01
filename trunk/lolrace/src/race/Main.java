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

package race;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JFrame;

/**
 * 
 * @author Preston Mueller
 *
 */

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		final JFrame frame = new JFrame("Race to the finish!!!");
		
		if(System.getProperty("os.name").equals("Mac OS X"))
				frame.setSize(600, 600);
		else
			frame.setSize(610, 600);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				frame.dispose();
				System.exit(0);
			}
		});
		Boolean again = new Boolean(true);
		HashMap<String, Integer> scoreboard = new HashMap<String, Integer>();
		scoreboard.put("gator", 0);
		scoreboard.put("penguin", 0);
		scoreboard.put("cat", 0);
		scoreboard.put("human", 0);
		while(again)
		{
			Game newContentPane = new Game(frame, again);
			newContentPane.setOpaque(true);
			frame.setContentPane(newContentPane);
			((Game)frame.getContentPane()).gatorScore.setText(scoreboard.get("gator").toString());
			((Game)frame.getContentPane()).penguinScore.setText(scoreboard.get("penguin").toString());
			((Game)frame.getContentPane()).catScore.setText(scoreboard.get("cat").toString());
			((Game)frame.getContentPane()).humanScore.setText(scoreboard.get("human").toString());
			frame.setVisible(true);
			
			
			Random randGen = new Random();
			boolean ret = true;
			String c = "";
			int toMove = 0;
			while(true)
			{
				toMove = randGen.nextInt(4);
				switch(toMove){
				case 0:
					ret = ((Game)frame.getContentPane()).racers.get("gator").move();
					c = "gator";
					break;
				case 1:
					ret = ((Game)frame.getContentPane()).racers.get("penguin").move();
					c = "penguin";
					break;
				case 2:
					ret = ((Game)frame.getContentPane()).racers.get("cat").move();
					c = "cat";
					break;
				case 3:
					ret = ((Game)frame.getContentPane()).racers.get("human").move();
					c = "human";
					break;
				
				}
				int timeToNext = randGen.nextInt(75) + 30;
				Thread.sleep(timeToNext);
	
				if(!ret)
				{
					((Game)frame.getContentPane()).winrar.setText(c + " is a winrar!!!!");
					scoreboard.put(c, scoreboard.get(c)+1);
					switch(toMove){
					case 0:
						
						((Game)frame.getContentPane()).racers.get("penguin").die();
						((Game)frame.getContentPane()).racers.get("cat").die();
						((Game)frame.getContentPane()).racers.get("human").die();
						
						((Game)frame.getContentPane()).gatorScore.setText(scoreboard.get(c).toString());
						((Game)frame.getContentPane()).gatorScore.update(((Game)frame.getContentPane()).gatorScore.getGraphics());
						((Game)frame.getContentPane()).racers.get("gator").dance();
						break;
					case 1:
						((Game)frame.getContentPane()).racers.get("gator").die();
						((Game)frame.getContentPane()).racers.get("cat").die();
						((Game)frame.getContentPane()).racers.get("human").die();
						
						((Game)frame.getContentPane()).penguinScore.setText(scoreboard.get(c).toString());
						((Game)frame.getContentPane()).penguinScore.update(((Game)frame.getContentPane()).penguinScore.getGraphics());
						((Game)frame.getContentPane()).racers.get("penguin").dance();
						break;
					case 2:
						((Game)frame.getContentPane()).racers.get("gator").die();
						((Game)frame.getContentPane()).racers.get("penguin").die();
						((Game)frame.getContentPane()).racers.get("human").die();
						
						((Game)frame.getContentPane()).catScore.setText(scoreboard.get(c).toString());
						((Game)frame.getContentPane()).catScore.update(((Game)frame.getContentPane()).catScore.getGraphics());
						((Game)frame.getContentPane()).racers.get("cat").dance();
						break;
					case 3:
						((Game)frame.getContentPane()).racers.get("gator").die();
						((Game)frame.getContentPane()).racers.get("penguin").die();
						((Game)frame.getContentPane()).racers.get("cat").die();
						
						((Game)frame.getContentPane()).humanScore.setText(scoreboard.get(c).toString());
						((Game)frame.getContentPane()).humanScore.update(((Game)frame.getContentPane()).humanScore.getGraphics());
						((Game)frame.getContentPane()).racers.get("human").dance();
						break;
					}
					((Game)frame.getContentPane()).again.setEnabled(true);
					((Game)frame.getContentPane()).again.update(((Game)frame.getContentPane()).again.getGraphics());
					synchronized(frame)
					{
						frame.wait();
					}
					break;
				}
			}
		}
		frame.setVisible(false);
		frame.dispose();
		System.exit(0);
	}
}
