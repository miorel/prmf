package com.googlecode.prmf.starter;

import com.googlecode.prmf.Game;
public class MafiaListener implements Listener {
	String channel = "#UFPT";
	private Game game;
	public void receiveLine(String in, InputThread inputThread) 
	{
		String[] msg = in.split(" ",4);
		String user = channel;
		
		if(msg[0].indexOf("!") > 1)
			user = msg[0].substring(1,msg[0].indexOf("!"));

		if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!mafia"))
		{
			//TODO: make it so you cant start 2 games.
			inputThread.sendMessage(channel, "Mafia game started!");
			game = new Game(user, inputThread);
		}
		else if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!join"))
		{
			game.receiveMessage(msg[3]);
		}
		else if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!end"))
		{
			game.receiveMessage(msg[3]);
		}
		else if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!quit"))
		{
			game.receiveMessage(msg[3]);
		}
	}
}
