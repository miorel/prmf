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
import java.util.*;

public class Configuration {
	
	private static final int CFG_COMPATMIN = 4000;
	private static final int CFG_COMPATMAX = 4000;
	
	private static final String CFG_VERSION = "Version";
	private static final String CFG_PORT = "Port";
	private static final String CFG_USERFILE = "Userfile";
	private static final String CFG_SCOREFILE = "Scorefile";
	private static final String CFG_INITSCRIPT = "Initscript";
	
	private int version;
	private int port;
	private Hashtable<String, String> users;
	private Hashtable<String, Integer> scores;
	private String[] rc;
	
	public Configuration(BufferedReader br) throws IOException, IllegalArgumentException {
		users = new Hashtable<String, String>();
		scores = new Hashtable<String, Integer>();
		String str;
		while((str = br.readLine()) != null) {
			String[] hashval = str.split("=", 2);
			if(hashval.length == 2)
				setAttribute(hashval[0], hashval[1]);
		}
		br.close();
	}
	
	/* Handle the setting/updating of the internal representation of the master configuration data */
	private boolean setAttribute(String key, String value) throws IOException, IllegalArgumentException {
		if(key.equals(CFG_VERSION)) {
			version = Integer.parseInt(value);
			if(version < CFG_COMPATMIN || version > CFG_COMPATMAX) {
				throw new IllegalArgumentException(CFG_VERSION + ": Not compatible");
			}
		} else if(key.equals(CFG_PORT)) {
			port = Integer.parseInt(value);
			if(port <= 0 || port > 65535) {
				throw new IllegalArgumentException(CFG_PORT + ": Out of range");
			}
		} else if(key.equals(CFG_USERFILE)) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(value));
				String str;
				while((str = br.readLine()) != null) {
					String[] hashval = str.split("=", 2);
					if(hashval.length == 2)
						users.put(hashval[0], hashval[1]);
				}
				br.close();
			} catch(FileNotFoundException e) {
				throw new IllegalArgumentException(CFG_USERFILE + ": File not found");
			}
		} else if(key.equals(CFG_SCOREFILE)) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(value));
				String str;
				while((str = br.readLine()) != null) {
					String[] hashval = str.split("=", 2);
					if(hashval.length == 2)
						scores.put(hashval[0], Integer.valueOf(hashval[1]));
				}
				br.close();
			} catch(FileNotFoundException e) {
				throw new IllegalArgumentException(CFG_USERFILE + ": File not found");
			}
		} else if(key.equals(CFG_INITSCRIPT)) {
			try {
				ArrayList<String> temprc = new ArrayList<String>();
				BufferedReader br = new BufferedReader(new FileReader(value));
				String str;
				while((str = br.readLine()) != null) {
					temprc.add(str);
				}
				br.close();
				rc = temprc.toArray(new String[] {});
			} catch(FileNotFoundException e) {
				throw new IllegalArgumentException(CFG_INITSCRIPT + ": File not found");
			}
		} else {
			return false;
		}
		return true;
	}
	
	/* Add a user and generate an encrypted password for them if the user doesn't already exist */
	public boolean addUser(String username, String password) {
		return false;
	}
	
	/* Delete a user if it exists */
	public boolean removeUser(String username) {
		return false;
	}
	
	/* Validate if a given unencrypted password is valid for a specific username */
	public boolean validateUser(String username, String password) {
		return false;
	}
	
	/* Generates a new encrypted password for an already existing user */
	public boolean setUserPassword(String username, String password) {
		return false;
	}
	
	/* Returns the high score of a specific user if it exists */
	public int getHighScore(String username) throws IllegalArgumentException {
		throw new IllegalArgumentException("Unable to look up high score for nonuser " + username);
	}
	
	/* Sets the high score of a specific user if it exists */
	public boolean setHighScore(String username, int score) {
		return false;
	}
	
	/* Returns an array of strings containing the */
	public String[] getInitScript() {
		return rc;
	}
	
	/* Saves internal user configuration down to a file */
	public boolean syncUsers() {
		return false;
	}
	
	/* Saves internal high scores down to a file */
	public boolean syncScores() {
		return false;
	}
	
	/* Get the port number for the server */
	public int getPort() {
		return port;
	}
	
}
