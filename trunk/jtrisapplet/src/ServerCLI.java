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
		System.out.println("JTrisApplet Server Version 4");
		
		/* Process command line argument for logging destination */
		if(args.length == 0) {
			System.out.println("Logging to stdout");
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
		log.Write("Building configuration data: jtaserv.cfg");
		try {
			config = new Configuration(new BufferedReader(new FileReader(new File("jtaserv.cfg"))));
		} catch(IOException e) {
			log.Write("I/O Exception while reading configuration file");
			return;
		} catch(IllegalArgumentException e) {
			log.Write("Error or invalid line in configuration file");
			return;
		}
		
		/* Create and start the server */
		log.Write("Bringing up the server");
		server = new Server(log, config);
		server.Start();
		
		/* Execution has finished */
		log.Close();
		
	}
	
}
