package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;


public class Night implements MafiaGameState
{
	private Player[] players;
	private IOThread inputOutputThread;
	
	public Night(Player[] players, IOThread inputOutputThread)
	{
		this.players = players;
		this.inputOutputThread = inputOutputThread;
	}
	
//	TODO add a timer to night
	public boolean receiveMessage(Game game, String line)
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
			inputOutputThread.sendMessage(speaking.toString(),
					(result?"You successfully targetted " + target:"Your night action did not resolve"));
			
		}
		boolean isOver = isNightOver();
		if (isOver)
		{
			resolveNightActions();
			cleanUp();
			for(Player p : game.getPlayerList())
			{
				if(p.isAlive())
					inputOutputThread.sendMessage("MODE",inputOutputThread.getChannel(), "+v "+p.getName());
			}
			game.setState(new Day(players, inputOutputThread));
		}
		return isOver;
	}
	public void introduction()
	{
		for (Player player : players)
		{
			if (player.isAlive())
			{
				inputOutputThread.sendMessage(player.getName(), player.getRole().description());
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
			inputOutputThread.sendMessage(p.toString(), result);
		}
	}
	
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
		inputOutputThread.sendMessage(inputOutputThread.getChannel(), "It is now night!");
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
