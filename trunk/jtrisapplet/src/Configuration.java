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

public class Configuration {
	
	private static final String CFG_VERSION = "Version";
	private static final String CFG_PORT = "Port";
	private static final String CFG_USERFILE = "Userfile";
	private static final String CFG_SCOREFILE = "Scorefile";
	
	public Configuration(BufferedReader reader) throws IOException, IllegalArgumentException {
		String line;
		while((line = reader.readLine()) != null) {
			String key = line.split("=", 2)[0];
			String value = line.split("=", 2)[1];
			SetAttribute(key, value);
		}
		reader.close();
		Validate();
	}
	
	private boolean SetAttribute(String key, String value) {
		if(key.equals(CFG_VERSION)) {
			;;
		} else if(key.equals(CFG_PORT)) {
			;;
		} else if(key.equals(CFG_USERFILE)) {
			;;
		} else if(key.equals(CFG_SCOREFILE)) {
			;;
		} else {
			return false;
		}
		return true;
	}
	
	private boolean Validate() throws IllegalArgumentException {
		return true;
	}
	
}
