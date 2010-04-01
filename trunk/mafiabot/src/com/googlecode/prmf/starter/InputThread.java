package com.googlecode.prmf.starter;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TODO rename this class since it will probably also be used for output 
public class InputThread extends Thread {
	private Socket soc;
	private InputStream inputstream; //TODO use camel case
	private List<Listener> list;
	private PrintStream printstream; //TODO use camel case

	public InputThread(String server, int port) {
		try {
			soc = new Socket(server, port);
			inputstream = soc.getInputStream();
			printstream = new PrintStream(soc.getOutputStream());
		}
		catch (Exception e) {
			System.out.println("oops");
		} 
		list = new ArrayList<Listener>();
		
		//TODO the following should not be done done in the constructor but when the thread is started
		printstream.println("NICK MAFFAIBOOT77"); //TODO don't hardcode the nick
		printstream.println("USER MAFFAIBOOT77 12 * MAFFAIBOOT77"); //TODO don't hardcode the username and real name
		printstream.println("JOIN #UFPT"); //TODO don't hardcode the channel
	}
	
	public void run()
	{
		Scanner in = new Scanner(inputstream);
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			System.out.println("<<<< " + line);
			for(Listener l : list)
				l.receiveLine(line, this);
		}
	}
	
	public void addListener(Listener listener) {
		list.add(listener);
	}
	
	public void sendMessage(String destination, String message) 
	{
		printstream.println("PRIVMSG "+destination+" :"+message);
		System.out.println(">>>> " + "PRIVMSG "+destination+" :"+message);
	}
	
	public void sendPONG(String[] msg)
	{
		msg[0] = "PONG";
		String concat="";
		for(int i=0;i<msg.length;++i)
		{
			concat+=msg[i];
			if(i<msg.length-1)
				concat+=" ";
		}
		printstream.println(concat);
	}
}
