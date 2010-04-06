package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;


public class Night implements MafiaGameState
{
	private Player[] players;
	private IOThread inputThread;
	private boolean[] actionComplete;
	
	public Night(Player[] players, IOThread inputThread)
	{
		this.players = players;
		this.inputThread = inputThread;
		actionComplete = new boolean[players.length];
	}
	
//	TODO add a timer to night
	
	public boolean receiveMessage(String line, IOThread inputThread)
	{
		// TODO why is the channel name hardcoded?
		inputThread.sendMessage("#UFPT" , " WE ARE IN NIGHT");
		
		//temporary bad solution until we get around to overhauling the command system
		String[] splitLine = line.split(" ");
		if (splitLine.length <= 4)
			return false;
		String speaker = splitLine[0].substring(1,line.indexOf("!"));
		String action = splitLine[3];
		String target = splitLine[4];
		
		
		Player speaking = null;
		for (int i=0;i<players.length;i++)
		{
			if (players[i].equals(new Player(speaker)))
			{
				speaking = players[i];
				break;
			}
			if (i == players.length-1)
				return false;
		}
		
		if (speaking.role.hasNightAction())
		{
			speaking.role.nightAction(action + " " + target, players);
			
			inputThread.sendMessage("#ufpt", "oh god you did a night action~");
		}
		
		for (int i=0;i<players.length;i++)
		{
			if (players[i].equals(speaking))
			{
				actionComplete[i] = true;
			}
		}
		
		
		
		
		
		return isNightOver(); //is this right? maybe should resolve actions after isNightOver returns true,
	}
	
	public void introduction(IOThread inputThread)
	{
		for (Player player : players)
		{
			if (player.isAlive)
			{
				inputThread.sendMessage(player.name, player.role.description());
			}
		}
	}
	
	public boolean isNightOver()
	{
		for (int i=0;i<players.length;i++)
			if(!actionComplete[i] && players[i].role.hasNightAction())
				return false;
		return true;
	}
	
	public void resolveNightActions()
	{
		//TODO resolve night actions, ldo
	}
	
	
}
