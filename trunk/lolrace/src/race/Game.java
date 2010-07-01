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
 * 
 * @author Preston Mueller
 *
 */

public class Game extends JLayeredPane {

	HashMap<String, Racer> racers;
	Background bg;
	JLabel winrar;
	JButton again;
	JButton exit;
	JFrame frame;
	Boolean boolagain;
	JLabel gatorScore;
	JLabel penguinScore;
	JLabel catScore;
	JLabel humanScore;
	public Game(JFrame f, Boolean b)
	{
		boolagain = b;
		frame = f;
		racers = new HashMap<String, Racer>();
		setBackground(Color.WHITE);
		this.setLayout(null);
		bg = new Background();
		add(bg, 1);
		bg.setBounds(0,0,600,600);
		bg.setOpaque(true);
		winrar = new JLabel();
		add(winrar,2);
		winrar.setOpaque(true);
		winrar.setBounds(0,15,400,15);
		winrar.setHorizontalAlignment( SwingConstants.CENTER );
		
		gatorScore = new JLabel();
		penguinScore = new JLabel();
		catScore = new JLabel();
		humanScore = new JLabel();
		
		add(gatorScore,2);
		add(penguinScore,2);
		add(catScore,2);
		add(humanScore,2);
		
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
			tmpImg = ImageIO.read(this.getClass().getResource("again.png"));
			again = new JButton(new ImageIcon(tmpImg));
			tmpImg = ImageIO.read(this.getClass().getResource("exit.png"));
			exit = new JButton(new ImageIcon(tmpImg));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		add(again,2);
		again.setBounds(433, 307,137,129);
		again.setOpaque(true);
		again.setBorderPainted(false);
		again.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	boolagain = new Boolean(false);
            	synchronized(frame)
            	{
            		frame.notify();
            	}
            }
        });   
		again.setEnabled(false);
		add(exit,2);
		exit.setOpaque(true);
		exit.setBorderPainted(false);
		exit.addActionListener(new ActionListener() {
               
            public void actionPerformed(ActionEvent e)
            {
            	frame.setVisible(false);
				frame.dispose();
				System.exit(0);
            }
        });      
		
		exit.setBounds(450,522 ,103,31);

		racers.put("gator", new Racer("gator", 1));
		racers.put("penguin", new Racer("penguin", 2));
		racers.put("cat", new Racer("cat", 3));
		racers.put("human", new Racer("human", 4));

		Set<String> s = racers.keySet();
		
		for(String x : s)
		{
			add(racers.get(x),3);
		}
		for(String x : s)
		{
			racers.get(x).setBounds(75 * racers.get(x).lane, 475, 27, 90);
			racers.get(x).setOpaque(true);
		}
		
	}
}
