package com.googlecode.prmf.starter;

public class PingListener implements Listener{
	String channel = "#UFPT"; // TODO why is the channel name here?
	public void receiveLine(String in, IOThread inputThread) 
	{
		String msg[] = in.split(" ");
		if(msg[0].equalsIgnoreCase("PING"))
		{
			inputThread.sendPONG(msg);
		}
			
	}
}
