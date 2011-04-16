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

import java.io.*;
import java.net.*;

public class Connection {
	
	private String hostname;
	private int port;
	private boolean connected;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public Connection(final String hostname, final int port) {
		this.hostname = hostname;
		this.port = port;
		this.connected = false;
	}
	
	/* Connect to remote server */
	public boolean connect() {
		if(connected)
			return connected;
		try {
			socket = new Socket(hostname, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			connected = true;
		} catch(UnknownHostException e) {
			System.out.println("debug: unknown host exception");
			connected = false;
		} catch(IOException e) {
			System.out.println("debug: io exception");
			connected = false;
		}
		return connected;
	}
	
	/* Adopt a connection to a remote server */
	public boolean connect(Socket sock) {
		if(connected)
			return connected;
		try {
			socket = sock;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(IOException e) {
			System.out.println("debug: io exception");
			connected = false;
		}
		return connected;
	}
	
	/* Disconnect from the remote server */
	public void disconnect() {
		out.close();
		try {
			in.close();
			socket.close();
		} catch(IOException e) {
			;; // We are disconnecting, no need to care if socket I/O was broken
		}
		connected = false;
	}
	
	/* Login to the remote server */
	public boolean login(String username, String password) {
		send(new String[] {Protocol.C_LOGIN, username});
		String challenge = read(Protocol.S_CHAL);
		String response = Sha1.Encode(challenge + username + password);
		send(new String[] {Protocol.C_AUTH, response});
		String confirm = read(Protocol.S_CONFIRM);
		if(confirm.equals(Protocol.TRUE))
			return true;
		else
			return false;
	}
	
	/* Send method for raw data */
	private void send(String str) {
		str = Netstrings.encode(str);
		out.println(str);
	}
	
	/* Send method for command and arguments */
	private void send(String[] strs) {
		String cmd = "";
		for(String str : strs) {
			cmd = cmd + " " + str;
		}
		send(cmd);
	}
	
	/* Read method for raw data */
	private String read() {
		String str = null;
		try {
			str = in.readLine();
			str = Netstrings.decode(str);
		} catch(IOException e) {
			disconnect();
		}
		return str;
	}
	
	/* Read method for expected command followed by one argument */
	private String read(String cmd) {
		String str = read();
		String[] strs = str.split(" ", 2);
		if(cmd.equals(strs[0]))
			return strs[1];
		else
			return null;
	}
	
	/* Read method for expected command followed by multiple arguments */
	private String[] readMulti(String cmd) {
		String str = read(cmd);
		String[] strs = str.split(" ");
		if(cmd.equals(strs[0]))
			return strs;
		else
			return null;
	}
	
	/* Read method for expected command followed by a specific number of arguments */
	private String[] readMulti(String cmd, int num) {
		String[] strs = readMulti(cmd);
		if(strs == null || strs.length != num)
			return null;
		else
			return strs;
	}
	
}
