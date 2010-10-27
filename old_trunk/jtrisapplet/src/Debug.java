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

public class Debug {
	
	public static void main(String[] args) {
		
		Client client;
		Scanner s = new Scanner(System.in);
		
		System.out.println("JTrisApplet Debugger\nVersion 4 Release 0");
		System.out.println("** Be careful, I don't try to validate input");
		
		System.out.println("Connecting to localhost port 2000");
		
		System.out.print("Username: ");
		String username = s.nextLine();
		System.out.print("Password: ");
		String password = s.nextLine();
		
		client = new Client("localhost:2000");
		
		if(client.attemptConnectAndLogin(username, password)) {
			System.out.println("Logged in");
		} else {
			System.out.println("Login didn't work");
		}
		
		System.out.println("Done!");
		
	}
	
}
