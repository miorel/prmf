package com.googlecode.prmf;

import com.googlecode.prmf.starter.InputThread;

public class Game{
	private Player[] players;
	private String gameStarter;
	private int numMafia =1;
	private boolean dayStart = true;
	private InputThread inputThread;
	Pregame pregame;
	Day day;
	Night night;
	
	public Game(String gameStarter, InputThread inputThread)	{
		this.gameStarter = gameStarter;
		this.inputThread = inputThread;
		pregame = new Pregame(gameStarter , inputThread);
		// TODO: add day and night constructors
	}

	public void pregameMessage(String line) 
	{
		receiveMessage(line);
	}
	
	public void dayMessage(String line)
	{
		
	}
	
	public void nightMessage(String line)
	{
		
	}
	public void receiveMessage(String line)
	{
		//TODO: send to proper object depending on 'state', instead of sending to pregame (temporary)
		pregameMessage(line);
	}
	
	public Game(String gameStarter, InputThread inputThread, boolean dayStart, int numMafia)
	{
		this(gameStarter, inputThread);
		this.dayStart = dayStart;
		this.numMafia = numMafia;
	}
	
	
}
