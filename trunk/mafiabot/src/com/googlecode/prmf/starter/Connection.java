package com.googlecode.prmf.starter;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connection 
	{
	// TODO it's better practice to actually catch exceptions, and print a useful message
	public Connection(String socket, int port) throws UnknownHostException, IOException 
	{
		
		Socket s = new Socket(socket, port);
		Scanner in = new Scanner(s.getInputStream());
		PrintStream os = new PrintStream(s.getOutputStream());
		os.println("NICK MAFIABOT22");
		//System.out.println("**ENTERED NICK");
		os.println("USER MAFIABOT22 12 * MAFIABOT22");
		//System.out.println("**ENTERED USER LOGIN");
		os.println("JOIN #UFPT");
		//System.out.println("**JOINED CHANNEL");
		os.println("PRIVMSG #UFPT :HELLO I AM MAFAIA BOTT");
		
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			
			String[] msg = line.split(" ",4);
			String user = "#UFPT";
			
			//if(msg[0].indexOf("!")>1)
				//user = msg[0].substring(1,msg[0].indexOf("!"));
			
			
			if(msg.length >= 4 && msg[3].toLowerCase().equals(":!start"))
			{
				os.println("PRIVMSG "+ user +" :MAFAI GAME STARTED!");
			}
			System.out.println(line);
			
		}
	}
	
	
}
