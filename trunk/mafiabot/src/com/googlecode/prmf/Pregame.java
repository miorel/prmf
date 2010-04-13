package com.googlecode.prmf;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.prmf.starter.IOThread;

public class Pregame implements MafiaGameState {
	static private MafiaTeam mafiaTeam = new MafiaTeam();
	static private Town town = new Town();
	private String startName;
	private boolean profileLoaded;
	private List<Player> players;
	private List<Role> townRoles;
	private List<Role> mafiaRoles;
	private List<Role> roles;
	IOThread inputThread; // TODO why is this not private?
	private boolean dayStart;

	public Pregame(String startName, IOThread inputThread) {
		this.startName = startName;
		players = new ArrayList<Player>();
		townRoles = new ArrayList<Role>();
		mafiaRoles = new ArrayList<Role>();
		roles = new ArrayList<Role>();
		dayStart = true;
		this.inputThread = inputThread;
		profileLoaded = false;
	}
	
	public Pregame()
	{
		
	}
    //TODO: why is this receiving an IO thread? one was given in the constructor
	public boolean receiveMessage(Game game, String line, IOThread inputThread)
	{
			boolean ret = false;
			String[] msg = line.split(" ", 4);
			String user = "";

			//this is kind of a nasty solution...

			if(msg[0].indexOf("!")>1)
				user = msg[0].substring(1,msg[0].indexOf("!"));
			
			Player temp = new Player(user);
			int index = players.indexOf(temp);
			
			String destination = msg[2];
			String command = msg[3].toLowerCase();
			if(command.equalsIgnoreCase(":~start"))
			{
				if(user.equals(startName))		
				{
					inputThread.sendMessage(destination, "The game has begun!");
					startGame(game, inputThread);
					ret = true;
				}
				else
					inputThread.sendMessage(destination,  "Only " + startName + " can start the game!");
			}	
			if(command.equalsIgnoreCase(":~join"))
			{
				System.out.println(index);
				if(index == -1)
				{
					players.add(new Player(user));
					inputThread.sendMessage(destination,  user + " has joined the game!");
				}
				else
					inputThread.sendMessage(destination,  user + " has already joined the game!");
			}
			
			if(command.equalsIgnoreCase(":~quit"))
			{
				System.out.println(index);
				if(index == -1)
					inputThread.sendMessage(destination, user + " is not part of the game!");
				else
				{
					players.remove(index);
					inputThread.sendMessage(destination,  user + " has quit the game!");
				}
				
			}	
			if (ret)
			{
				if (dayStart)
					game.setState(new Day(getPlayerArray(), inputThread));
				else
					game.setState(new Night(getPlayerArray(), inputThread));
			}
			return ret;
	}	
    //TODO: why is this receiving an IO thread? one was given in the constructor
	private void startGame(Game game, IOThread inputThread)
	{	
		if(!profileLoaded)
		{
			defaultStart(game,inputThread);
			return;
		}
		
		Collections.shuffle(roles);
		for(int a = 0; a < players.size(); ++a)
		{
			Player p = players.get(a);
			
			p.setRole(roles.get(a));
			p.getRole().getTeam().addPlayer(p); //this seems kinda sloppy, any better way of doing this?
												//yes, do it from within setRole()
			inputThread.sendMessage(players.get(a).getName(), p.getRole().description());
		}
	}
	
	private void defaultStart(Game game,IOThread inputThread)
	{
		try {
			Class tempTeam = Class.forName("com.googlecode.prmf.Pregame");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Exception caught forName testing.");
		}
		
		int numMafia = (int)Math.ceil(players.size()/4.0);

		for(int a = 0; a < numMafia; ++a)
		{
			mafiaRoles.add(new Mafia(mafiaTeam));
		}
		
		townRoles.add(new Cop(town));
		townRoles.add(new Doctor(town));
		//create the Town team
		for(int a = 0; a < (players.size() - numMafia-2); ++a)
		{
			townRoles.add(new Citizen(town));
		}
		roles.addAll(mafiaRoles);
		roles.addAll(townRoles);
		
		Collections.shuffle(roles);
		
		for(int a = 0; a < players.size(); ++a)
		{
			Player p = players.get(a);
			
			p.setRole(roles.get(a));
			p.getRole().getTeam().addPlayer(p); //this seems kinda sloppy, any better way of doing this?
												//yes, do it from within setRole()
			inputThread.sendMessage(players.get(a).getName(), p.getRole().description());
		}
	}
	
	public void loadRoleProfile(String[] roleMsg)
	{
		//Method not working yet
		roles.clear();
		for(int i=0;i<roleMsg.length;++i)
		{
			roleMsg[i] = roleMsg[i].trim();
			String[] roleSplit = roleMsg[i].split(":");
			Pregame pregame = null;
			try
			{
				Class clsBook = Pregame.class.getClassLoader().loadClass(
                "com.googlecode.prmf.Pregame");
				pregame = (Pregame)clsBook.newInstance();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.err.println("dont add new shit nooby");
			}
			try
			{
				//TODO there be warnings here, fix them
				Class tempAssigner = Class.forName("com.googlecode.prmf.Pregame$"+roleSplit[1]+"Assigner");
				Method[] allMethods = tempAssigner.getMethods();
				Method getTeam = allMethods[0];
				Object obj = tempAssigner.getDeclaredConstructor(new Class[]{Pregame.class}).newInstance(new Object[]{pregame});
				Team specificTeam = (Team)(getTeam.invoke(obj));
				roles.add( (Role)Class.forName("com.googlecode.prmf."+roleSplit[0]).getConstructor(specificTeam.getClass()).newInstance(specificTeam) );
				profileLoaded = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.err.println("OOP is hard"); // reflection is not very OOPy
			}
		}
	}
	public Player[] getPlayerArray() // it's a player array now, hope you're happy ;p
	{
		return players.toArray(new Player[0]);
	}
	
	/*
	public void swapState(Game game, MafiaGameState newState)
	{
		game.setState(newState);
		if(dayStart)
		{
			if (game.getDay() == null)
				game.makeDay();
			game.setState(game.getDay());
		}
		else
		{
			if (game.getNight() == null)
				game.makeNight();
			game.setState(game.getNight());
		}
		game.getState().status();
	}
	*/
	
	public boolean getDayStart()
	{
		return dayStart;
	}
	
	public void setDayStart(boolean day)
	{
		dayStart = day;
	}
	
	public void status()
	{
		inputThread.sendMessage(inputThread.getChannel(), "The following players are currently in:");
		StringBuilder playersIn = new StringBuilder();
		for (Player p : players)
		{
			if(playersIn.length() > 0)
				playersIn.append(", ");
			playersIn.append(p);
		}
		inputThread.sendMessage(inputThread.getChannel(), playersIn.toString());
		
		//TODO transform the following to biconditional
		if(dayStart)
			inputThread.sendMessage(inputThread.getChannel(), "This game is currently set to day start");
		else
			inputThread.sendMessage(inputThread.getChannel(), "This game is currently set to night start");
	}
	
	class MafiaTeamAssigner implements Assigner
	{
		public MafiaTeam getTeam()
		{
			return mafiaTeam;
		}
	}
	class TownAssigner implements Assigner
	{
		public Town getTeam()
		{
			return town;
		}
	}
}