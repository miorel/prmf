package com.googlecode.prmf;

import java.util.*;
import java.io.*; 
import com.googlecode.prmf.starter.*;

public class Pregame {
	
	InputStream is;
	String startName;
	
	ArrayList<Player> players;
	ArrayList<Role> townRoles;
	ArrayList<Role> mafiaRoles;
	ArrayList<Role> roles; 
	
	public Pregame(String startName)
	{
		this.is = Connection.is;
		this.startName = startName;
	}

	public void startGame()
	{
		Scanner in = new Scanner(is);
		players = new ArrayList<Player>();
		townRoles = new ArrayList<Role>();
		mafiaRoles = new ArrayList<Role>();
		roles = new ArrayList<Role>();
		
		
		//joining the game	
		
		while(true) //break when the game starts
		{
			String line = in.nextLine();
			String[] msg = line.split(" ", 4);
			String user = "#UFPT";
			
			if(msg[0].indexOf("!")>1)
				user = msg[0].substring(1,msg[0].indexOf("!"));
			
			String destination = msg[2];
			if(msg[3].toLowerCase().equals(":!start") && user.equals(startName))		
				break;
				
			//TODO add an !end command that will cancel the current game
			if(msg[3].toLowerCase().equals(":!join"))
			{
				Player temp = new Player(user);
				int index = players.indexOf(temp);
				System.out.println(index);
				if(index == -1)
				{
					players.add(new Player(user));
					Communicator.getInstance().sendMessage(destination, user + " has joined the game!");
				}
				else
					Communicator.getInstance().sendMessage(destination, user + " has already joined the game!");
			}
			
			if(msg[3].toLowerCase().equals(":!quit"))
			{
				Player temp = new Player(user);
				int index = players.indexOf(temp);
				System.out.println(index);
				if(index == -1)
					Communicator.getInstance().sendMessage(destination, user + " is not part of the game!");
				else
				{
					players.remove(index);
					Communicator.getInstance().sendMessage(destination, user + " has quit the game!");
				}
				
			}	
		}
		
		//assigning roles
		int numMafia = players.size()/3;
		//create the Mafia team
		for(int a = 0; a < numMafia; ++a)
		{
			mafiaRoles.add(new Mafia(new MafiaTeam()));
		}
		
		//create the Town team
		for(int a = 0; a < (players.size() - numMafia); ++a)
		{
			townRoles.add(new Citizen(new Town()));
		}
		Collections.shuffle(mafiaRoles);
		Collections.shuffle(townRoles);
		for(int a = 0; a < numMafia; ++a)
		{
			roles.add(mafiaRoles.get(a));
		}
		for(int a = 0; a < players.size() - numMafia; ++a)
		{
			roles.add(townRoles.get(a));
		}
		for(int a = 0; a < players.size(); ++a)
		{
			players.get(a).role = roles.get(a);
		}
		
	}

	public Player[] getPlayerList()
	{
		return players.toArray(new Player[0]);
	}
}