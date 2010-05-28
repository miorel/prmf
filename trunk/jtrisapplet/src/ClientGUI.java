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
	
	public void init() {
		// Window setup
		setSize(300, 150);
		
		// Window-level widgets
		Label lbl_welcome = new Label("Welcome to JTrisApplet");
		Panel pan_login = new Panel(new GridLayout(0, 2));
		Button btn_connect = new Button("Connect");
		
		// Create the login panel
		Label lbl_hostname = new Label("Hostname:");
		Label lbl_username = new Label("Username:");
		Label lbl_password = new Label("Password:");
		TextField tf_hostname = new TextField(11);
		TextField tf_username = new TextField(11);
		TextField tf_password = new TextField(11);
		tf_password.setEchoChar('*');
		pan_login.add(lbl_hostname);
		pan_login.add(tf_hostname);
		pan_login.add(lbl_username);
		pan_login.add(tf_username);
		pan_login.add(lbl_password);
		pan_login.add(tf_password);
		
		// Add widgets to window
		add(lbl_welcome);
		add(pan_login);
		add(btn_connect);
	}
	
}
