package com.googlecode.prmf.starter;

import java.util.Scanner;

public class Main {
	static Scanner in; //TODO remove this variable
	static String channel = "#UFPT"; //TODO make this lowercase
	static String server = "irc.freenode.net";
	static int port = 6667;
	
	public static void main(String[] arg) {
		IOThread inputThread = new IOThread(server, port); // TODO rename the variable in light of the updated name of its class
		MafiaListener mafiaListener = new MafiaListener(); // TODO declare things as what they're used, not as what they are
		PingListener pingListener = new PingListener();
		inputThread.addListener(mafiaListener);
		inputThread.addListener(pingListener);
		inputThread.start();
	}
}
