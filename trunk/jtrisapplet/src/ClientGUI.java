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

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends Applet {
	
	private Client client;
	
	private TextField tf_hostname;
	private TextField tf_username;
	private TextField tf_password;
	
	public String getAppletInfo() {
		String str = "JTrisApplet Client\n";
		str += "Version 4 Release 0";
		return str;
	}
	
	public String[][] getParameterInfo() {
		String[][] pinfo = {
			{"hostname", "String", "Remote hostname[:port] to connect to"},
			{"username", "String", "Default username to login under"},
			{"password", "String", "Default password to authenticate with"}
		};
		return pinfo;
	}
	
	public void attemptConnectAndLogin(Button b, ActionListener a) {
		b.removeActionListener(a);
		b.setLabel("Connecting... please wait");
		b.getParent().validate();
		client = new Client(tf_hostname.getText());
		if(client.attemptConnectAndLogin(tf_username.getText(), tf_password.getText())) {
			System.out.println("debug: to be implemented");
		} else {
			b.addActionListener(a);
			b.setLabel("Connect");
			b.getParent().validate();
		}
	}
	
	public void init() {
		System.out.println(getAppletInfo());
		setSize(300, 150);
		setLayout(new GridLayout(1, 1));
	}
	
	public void start() {
		Panel p = generateLoginPanel();
		add(p);
	}
	
	private Panel generateLoginPanel() {
		// Create object to return
		Panel pan_login_main = new Panel();
		
		// Read applet configuration into strings
		String DEFAULT_HOSTNAME = getParameter("hostname");
		String DEFAULT_USERNAME = getParameter("username");
		String DEFAULT_PASSWORD = getParameter("password");
		if(DEFAULT_HOSTNAME == null) DEFAULT_HOSTNAME = "localhost";
		if(DEFAULT_USERNAME == null) DEFAULT_USERNAME = "root";
		if(DEFAULT_PASSWORD == null) DEFAULT_PASSWORD = "password";
		
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