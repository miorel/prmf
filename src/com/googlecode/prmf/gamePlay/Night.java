package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;


public class Night implements MafiaGameState
{
	private Player[] players;
	private IOThread inputThread;
	
	public Night(Player[] players, IOThread inputThread)
	{
		this.players = players;
		this.inputThread = inputThread;
	}
	
//	TODO add a timer to night
    //TODO: why is this receiving an IO thread? one was given in the constructor
	public boolean receiveMessage(Game game, String line, IOThread inputThread)
	{
		if (isNightOver())
			return true;
		
		//temporary bad solution until we get around to overhauling the command system
		String[] splitLine = line.split(" ");
		if (splitLine.length <= 4)
			return false;
		String speaker = splitLine[0].substring(1,line.indexOf("!"));
		String action = splitLine[3];
		String target = splitLine[4];
		
		if(splitLine[1].startsWith("NICK") )
		{
			changeNick(speaker,splitLine[2]);
			return false;
		}
		
		Player speaking = null;
		for (Player p : players)
		{
			if (p.equals(new Player(speaker)))
			{
				speaking = p;
				break;
			}
		}
		if (speaking == null)
			return false;
		
		if (speaking.getRole().hasNightAction())
		{
			boolean result = speaking.getRole().nightAction(action + " " + target, players);
			if (result)
				inputThread.sendMessage(speaking.toString(), "You successfully targetted " + target);
			else
				inputThread.sendMessage(speaking.toString(), "Your night action did not resolve");
		}
		boolean isOver = isNightOver();
		if (isOver)
		{
			resolveNightActions();
			cleanUp();
			game.setState(new Day(players, inputThread));
		}
		return isOver;
	}
    //TODO: why is this receiving an IO thread? one was given in the constructor
	public void introduction(IOThread inputThread)
	{
		for (Player player : players)
		{
			if (player.isAlive())
			{
				inputThread.sendMessage(player.getName(), player.getRole().description());
			}
		}
	}
	
	public boolean isNightOver() {
		boolean result = true;
		for(Player p: players)
			if(p.isAlive() && !p.getRole().didNightAction())
			{
				result = false;
				break;
			}
		return result;
	}
	
	public void resolveNightActions()
	{
		for (Player p : players)
		{
			String result = p.getRole().resolveNightAction();
			inputThread.sendMessage(p.toString(), result);
		}
	}
	
	/*
	public void swapState(Game game, MafiaGameState newState)
	{
		game.setState(newState);
		game.isOver();
		game.getState().status();	
	}
	*/
	
	//TODO: turn this into something Player does in endNight perhaps
	public void resetActions()
	{
		for (Player p : players)
			p.getRole().resetNightAction();
	}
	
	public void resetLives()
	{
		for (Player p : players)
		{
			p.endNight();
		}
	}
	
	public void cleanUp()
	{
		resetActions();
		resetLives();
	}
	
	public void status()
	{
		inputThread.sendMessage(inputThread.getChannel(), "It is now night!");
	}
	
	private void changeNick(String oldNick , String newNick)
	{
		for(int i=0;i<players.length;++i)
		{
			if(players[i].getName().equals(oldNick))
			{
				players[i].setName(newNick.substring(1));
				return;
			}
		}
	}
}
