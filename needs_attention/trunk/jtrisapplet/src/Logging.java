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
import java.io.*;
import java.text.*;

public class Logging {
	
	private PrintStream log;
	private boolean open;
	private Date date;
	private DateFormat dateFormat;
	
	public Logging(PrintStream log) {
		date = new Date();
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		this.log = log;
		open = true;
		write("--- New Session Started ---");
	}
	
	public boolean write(String str) {
		if(!open) return false;
		log.println(dateFormat.format(date) + " | " + str);
		return true;
	}
	
	public void close() {
		write("--- Session Ended Gracefully ---");
		open = false;
		log.close();
	}
	
}
