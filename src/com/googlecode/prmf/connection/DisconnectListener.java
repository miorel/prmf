package com.googlecode.prmf.connection;

public class DisconnectListener implements Listener{

	public void receiveLine(String line, IOThread inputOutputThread) {
		String msg[] = line.split(" ");
		if(msg[0].equals("ERROR"))
			try
			{
				Main.main(new String[0]);
			}
			catch(Exception e)
			{
				System.out.println(e);
				System.out.println("Oops. (DisconnectListener)");
			}
	}
	
	public void timerMessage()
	{
		
	}
}
