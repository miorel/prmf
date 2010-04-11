package com.googlecode.prmf.starter;

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
}
