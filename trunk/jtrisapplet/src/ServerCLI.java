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

public class ServerCLI {
	
	public static void main(String[] args) {
		
		Logging log;
		Configuration config;
		Server server;
		
		/* Greet the user */
		System.out.println("JTrisApplet Server Version 4 Release 0");
		
		/* Process command line argument for logging destination */
		if(args.length == 0) {
			System.out.println("Copyright (C) 2010 Brian Nezvadovitz");
			System.out.println("This program comes with ABSOLUTELY NO WARRANTY. This is free software, and you are welcome to redistribute it under certain conditions. See COPYING for details.");
			log = new Logging(System.out);
		} else if(args.length == 1) {
			System.out.println("Logging to file: " + args[0]);
			try {
				PrintStream logIO = new PrintStream(new File(args[0]));
				log = new Logging(logIO);
			} catch(FileNotFoundException e) {
				System.out.println("Unable to open log file");
				return;
			}
		} else {
			System.out.println("Too many command line arguments");
			return;
		}
		
		/* Create the configuration object */
		log.write("Reading configuration data: jtaserv.cfg");
		try {
			config = new Configuration(new BufferedReader(new FileReader("jtaserv.cfg")));
		} catch(IOException e) {
			log.write("I/O Exception while reading configuration file");
			return;
		} catch(IllegalArgumentException e) {
			log.write("Error or invalid line in configuration file");
			if(e.getMessage() != null) {
				log.write("Exception: " + e.getMessage());
			}
			return;
		}
		
		/* Create and start the server */
		log.write("Bringing up the server...");
		server = new Server(log, config);
		server.start();
		
		/* Execution has finished */
		log.close();
		
	}
	
}
