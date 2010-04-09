package com.googlecode.prmf.starter;

public class Main {
	static String channel = "#ufpt";
	static String server = "irc.freenode.net";
	static int port = 6667;
	
	public static void main(String[] arg) {
		IOThread inputOutputThread = new IOThread(server, port, "#ufpt"); // TODO rename the variable in light of the updated name of its class
		MafiaListener mafiaListener = new MafiaListener(); // TODO declare things as what they're used, not as what they are
		PingListener pingListener = new PingListener();
		inputOutputThread.addListener(mafiaListener);
		inputOutputThread.addListener(pingListener);
		inputOutputThread.start();
	}
}
