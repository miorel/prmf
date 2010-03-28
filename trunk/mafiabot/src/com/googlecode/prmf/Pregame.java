package com.googlecode.prmf;

import java.util.*;

public class Pregame {
	

		public static void main(String[] args)
		{
			Scanner in = new Scanner(System.in);
			ArrayList<Player> players = new ArrayList<Player>();
			ArrayList<Role> townRoles = new ArrayList<Role>();
			ArrayList<Role> mafiaRoles = new ArrayList<Role>();
			ArrayList<Role> roles = new ArrayList<Role>();
			
			
			//joining the game	
			
			while(true) //break when the game starts
			{
				String line = in.nextLine();
				String[] data = line.split(" ");
				String name = data[0];
				if(data[1].equals("!start"))		
					break;
					
				if(data[1].equals("!join"))
				{
					players.add(new Player(data[0]));
					System.out.println(name + " has joined the game!");
				}
				
				if(data[1].equals("!quit"))
				{
					for(int a = 0; a < players.size(); ++a)
					{
						Player temp = players.get(a);
						if(temp.name.equals(name))
							players.remove(a);
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
	}

