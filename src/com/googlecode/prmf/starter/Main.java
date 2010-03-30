package com.googlecode.prmf.starter;

import java.util.Scanner;

import com.googlecode.prmf.Game;

public class Main {
	static Connection conn;
	static Scanner in;
	static String channel = "#UFPT";
	static String server = "irc.freenode.net";
	static int port = 6667;
	
	public static void main(String[] arg) 
	{
		try
		{
			Connection.makeConnection(server, port);
		}
		catch(Exception e)
		{
			System.out.println("oops");
		}
		
		Communicator.getInstance().setPrintStream(Connection.ps);
		
		try
		{
			in = new Scanner(Connection.is);
		}
		catch(Exception e)
		{
			System.out.println("oops2");
		}
		
		Connection.ps.println("NICK MAFIABOT22");
		//System.out.println("**ENTERED NICK");
		Connection.ps.println("USER MAFIABOT22 12 * MAFIABOT22");
		//System.out.println("**ENTERED USER LOGIN");
		Connection.ps.println("JOIN " + channel);
		//System.out.println("**JOINED CHANNEL");
		Communicator.getInstance().sendMessage(channel, "Hello, I am MafiaBot.");
		
		InputThread inputThread = new InputThread(server, port);
		inputThread.start();
		
		while(in.hasNextLine()) 
		{
			String line = in.nextLine();
			
			String[] msg = line.split(" ",4);
			String user = channel;
			
			if(msg[0].indexOf("!") > 1)
				user = msg[0].substring(1,msg[0].indexOf("!"));
			
			if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!mafia"))
			{
				Communicator.getInstance().sendMessage(channel,"Mafia game started!");
				Game game = new Game(user);
				game.startGame();
			}
		}
	}
}
