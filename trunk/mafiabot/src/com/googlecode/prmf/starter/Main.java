package com.googlecode.prmf.starter;

import java.io.*;
import java.util.*;

public class Main {
	static Connection conn;
	static Scanner in;
	public static void main(String[] arg) 
	{
		try
		{
			conn = new Connection("irc.freenode.net", 6667);
		}
		catch(Exception e)
		{
			System.out.println("oops");
		}
		
		Communicator.setPrintStream(conn.os);
		
		try
		{
		in = new Scanner(conn.soc.getInputStream());
		}
		catch(Exception e)
		{
			System.out.println("oops");
		}
		
		conn.os.println("NICK MAFIABOT22");
		//System.out.println("**ENTERED NICK");
		conn.os.println("USER MAFIABOT22 12 * MAFIABOT22");
		//System.out.println("**ENTERED USER LOGIN");
		conn.os.println("JOIN #UFPT");
		//System.out.println("**JOINED CHANNEL");
		conn.os.println("PRIVMSG #UFPT :HELLO I AM MAFAIA BOTT");
		
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			
			String[] msg = line.split(" ",4);
			String user = "#UFPT";
			
			if(msg[0].indexOf("!")>1)
				user = msg[0].substring(1,msg[0].indexOf("!"));
			
			
			if(msg.length >= 4 && msg[3].toLowerCase().equals(":!mafia"))
			{
				
			}
			
			System.out.println(line);
		}
	}
}
