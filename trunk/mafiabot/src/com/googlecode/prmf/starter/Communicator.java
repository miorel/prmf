package com.googlecode.prmf.starter;
import java.io.*;
public class Communicator {
	private static Communicator instance;
	private static PrintStream ps;
	public Communicator() 
	{
	}
	
	public static Communicator getInstance() 
	{
		if(instance == null)
			instance = new Communicator();
		return instance;
	}
	public static void setPrintStream(PrintStream psin)
	{
		ps = psin;
	}
	
	public void sendMessage(String destination, String message) 
	{
		ps.println("PRIVMSG "+destination+" :"+message);
	}
}
