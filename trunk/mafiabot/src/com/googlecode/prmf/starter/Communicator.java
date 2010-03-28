package com.googlecode.prmf.starter;
import java.io.*;
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
	
	public void sendMessage(PrintStream os, String destination, String message) 
	{
		os.println("PRIVMSG "+destination+" :"+message);
	}
}
