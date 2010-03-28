package com.googlecode.prmf.starter;

// TODO this class seems useless
public class Main {
	static Connection conn;

	public static void main(String[] arg) 
	{
		try
		{
			conn = new Connection("irc.freenode.net", 6667);
		}
		catch(Exception e)
		{
			System.out.println("opps");
		}
		
		
	}
}
