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

public class Protocol {
	
	/* Commands sent by client */
	public static final String C_LOGIN = "login";
	public static final String C_AUTH = "auth";
	
	/* Commands sent by server */
	public static final String S_CHAL = "chal";
	public static final String S_CONFIRM = "confirm";
	
	/* Valid by both client and server */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
}
