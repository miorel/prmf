package com.googlecode.prmf.starter;

import com.googlecode.prmf.Game;

public class MafiaListener implements Listener {
	String channel = "#UFPT"; //TODO why is the channel listed in so many places? and why is it uppercase?
	private Game game;
	public void receiveLine(String in, InputThread inputThread) 
	{
		String[] msg = in.split(" ",4);
		String user = channel;
		
		if(msg[0].indexOf("!") > 1)
			user = msg[0].substring(1,msg[0].indexOf("!"));

		if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!mafia")) //TODO toLowerCase() and equalsIgnoreCase() are redundant
		{
			//TODO: make it so you can't start 2 games.
			inputThread.sendMessage(channel, "Mafia game started!");
			game = new Game(user, inputThread);
		}
		else if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!join")) //TODO toLowerCase() and equalsIgnoreCase() are redundant
		{
			game.receiveMessage(msg[3]);
		}
		else if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!end")) //TODO toLowerCase() and equalsIgnoreCase() are redundant
		{
			game.receiveMessage(msg[3]);
		}
		else if(msg.length >= 4 && msg[3].toLowerCase().equalsIgnoreCase(":!quit")) //TODO toLowerCase() and equalsIgnoreCase() are redundant
		{
			game.receiveMessage(msg[3]);
		}
	}
}
