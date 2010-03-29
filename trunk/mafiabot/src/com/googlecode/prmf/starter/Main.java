package com.googlecode.prmf.starter;

import java.util.Scanner;

import com.googlecode.prmf.Game;

public class Main {
	static Connection conn;
	static Scanner in;
	
	public static void main(String[] arg) 
	{
		try
		{
			Connection.makeConnection("irc.freenode.net", 6667);
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
		Connection.ps.println("JOIN #UFPT");
		//System.out.println("**JOINED CHANNEL");
		Connection.ps.println("PRIVMSG #UFPT :HELLO I AM MAFAIA BOTT");
		
		while(in.hasNextLine()) {
			String line = in.nextLine();
			
			String[] msg = line.split(" ",4);
			String user = "#UFPT"; // TODO why is this uppercase?
			
			if(msg[0].indexOf("!") > 1)
				user = msg[0].substring(1,msg[0].indexOf("!"));
			
			if(msg.length >= 4 && msg[3].toLowerCase().equals(":!mafia"))
			{
				Communicator.getInstance().sendMessage(user,"MAFAI GAM STARTERED");
				Game game = new Game(msg[1]);
				game.startGame();
			}
			
			System.out.println(line);
		}
	}
}
