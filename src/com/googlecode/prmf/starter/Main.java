package com.googlecode.prmf.starter;

public class Main {
	static String channel = "#ufpt";
	static String server = "irc.freenode.net";
	static int port = 6667;
	
	public static void main(String[] arg) {
		IOThread inputOutputThread = new IOThread(server, port, "#ufpt");
		Listener mafiaListener = new MafiaListener(); 
		PingListener pingListener = new PingListener();
		inputOutputThread.addListener(mafiaListener);
		inputOutputThread.addListener(pingListener);
		inputOutputThread.start();
	}
}
