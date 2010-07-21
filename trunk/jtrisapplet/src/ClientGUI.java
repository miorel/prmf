/* JTrisApplet - Play a popular tetromino game inside a Java applet
   Version 4
   Copyright (C) 2010 Brian Nezvadovitz
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientGUI extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 957174843990132008L;

	private Client client;
	
	private TextField tf_hostname;
	private TextField tf_username;
	private TextField tf_password;
	
	public ClientGUI(String title) {
		super(title);
	}
	
	public static void main(String[] args) {
		final ClientGUI s = new ClientGUI("JTrisApplet");
		s.setSize(new Dimension(200, 200));
		s.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				s.setVisible(false);
				s.dispose();
			}
			
		} );
		
		s.setResizable(false);
		s.setLayout(new GridLayout(1, 1));

		Panel p = s.generateLoginPanel();
		s.add(p);
		
		s.setVisible(true);
		
	}
	
	public void attemptConnectAndLogin(Button b, ActionListener a) {
		client = new Client(tf_hostname.getText());
		if(client.attemptConnectAndLogin(tf_username.getText(), tf_password.getText())) {
			System.out.println("debug: to be implemented");
		}
	}
	
	private Panel generateLoginPanel() {
		// Create object to return
		Panel pan_login_main = new Panel();
		
		// Read applet configuration into strings
		String DEFAULT_HOSTNAME = "localhost";
		String DEFAULT_USERNAME = "root";
		String DEFAULT_PASSWORD = "password";
		
		// Top-level panel widgets
		Label lbl_welcome = new Label("Welcome to JTrisApplet", Label.CENTER);
		Panel pan_login_fields = new Panel(new GridLayout(0, 2));
		Button btn_connect = new Button("Connect");
		btn_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Button b = (Button)event.getSource();
				((ClientGUI)b.getParent().getParent()).attemptConnectAndLogin(b, this);
			}
		});
		
		// Create the sub-panel for the login fields
		Label lbl_hostname = new Label("Hostname:");
		Label lbl_username = new Label("Username:");
		Label lbl_password = new Label("Password:");
		tf_hostname = new TextField(DEFAULT_HOSTNAME, 11);
		tf_username = new TextField(DEFAULT_USERNAME, 11);
		tf_password = new TextField(DEFAULT_PASSWORD, 11);
		tf_password.setEchoChar('*');
		pan_login_fields.add(lbl_hostname);
		pan_login_fields.add(tf_hostname);
		pan_login_fields.add(lbl_username);
		pan_login_fields.add(tf_username);
		pan_login_fields.add(lbl_password);
		pan_login_fields.add(tf_password);
		
		// Add these widgets to the main login panel
		pan_login_main.add(lbl_welcome);
		pan_login_main.add(pan_login_fields);
		pan_login_main.add(btn_connect);
		
		// Return the fresh new login panel
		return(pan_login_main);
	}

}
