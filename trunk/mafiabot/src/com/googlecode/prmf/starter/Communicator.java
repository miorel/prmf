package com.googlecode.prmf.starter;
import java.io.*;
public class Communicator {
	private static Communicator instance;
	private PrintStream ps;
	
	public Communicator() 
	{
	}
	
	public static Communicator getInstance() 
	{
		if(instance == null)
			instance = new Communicator();
		return instance;
	}
	
	public void setPrintStream(PrintStream psin)
	{
		ps = psin;
	}
	

}
