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

import java.util.*;

public class Client {
	
	private Connection con;
	private Board board;
	private ArrayList<Player> players;
	
	public Client(final String hostname) {
		String real_hostname;
		int port = Protocol.PROTO_DEFAULT_PORT;
		if(hostname.indexOf(':') == -1)
			real_hostname = hostname;
		else {
			real_hostname = hostname.split(":", 2)[0];
			port = Integer.valueOf(hostname.split(":", 2)[1]);
		}
		con = new Connection(real_hostname, port);
	}
	
	public boolean attemptConnectAndLogin(final String username, final String password) {
		if(con.connect()) {
			System.out.println("debug: connect OK");
			if(con.login(username, password)) {
				System.out.println("debug: login:" + username + " pass:" + password);
				return true;
			}
		}
		System.out.println("debug: failed attemptConnectAndLogin");
		return false;
	}

}
