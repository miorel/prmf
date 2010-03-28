package com.googlecode.prmf.starter;

public class Communicator {
	private static Communicator instance;
	
	public Communicator() 
	{
	}
	
	public static Communicator getInstance() 
	{
		if(instance == null)
			instance = new Communicator();
		return instance;
	}
	
	public void sendMessage(String destination, String message) 
	{
		
	}
}
