package com.googlecode.prmf.connection;

public class PingListener implements Listener {

	public void receiveLine(String in, IOThread inputThread) {
		String msg[] = in.split(" ");
		if(msg[0].equalsIgnoreCase("PING")) {
			inputThread.sendPONG(msg);
		}

	}
}
