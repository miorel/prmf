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
import java.net.*;
import java.io.*;

public class Server {
	
	private Logging log;
	private Configuration config;
	private Vector<Player> players;
	
	public Server(Logging log, Configuration config) {
		this.log = log;
		this.config = config;
		players = new Vector<Player>();
	}
	
	public void start() {
		Socket socket;
		ServerSocket servsock;
		try {
			servsock = new ServerSocket(config.getPort());
		} catch(IOException e) {
			log.write("Could not allocate server socket on port " + config.getPort());
			log.write(e.toString());
			return;
		}
		log.write("Server started on port " + config.getPort());
		try {
			while((socket = servsock.accept()) != null) {
				Player p = new Player(socket);
				Thread cur = new Thread(p);
				cur.setDaemon(true);
				players.addElement(p);
				cur.start();
			}
		} catch(IOException e) {
			log.write("I/O exception while trying to accept socket");
			log.write(e.toString());
		}
	}
	
}
