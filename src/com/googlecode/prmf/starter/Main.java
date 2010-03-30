package com.googlecode.prmf.starter;

import java.util.Scanner;

public class Main {
	static Scanner in;
	static String channel = "#UFPT";
	static String server = "irc.freenode.net";
	static int port = 6667;
	
	public static void main(String[] arg) 
	{
		InputThread inputThread = new InputThread(server, port);
		inputThread.start();
		MafiaListener mafiaListener = new MafiaListener();
		inputThread.addListener(mafiaListener);
	}
}
