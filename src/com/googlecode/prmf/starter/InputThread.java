package com.googlecode.prmf.starter;

import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class InputThread extends Thread
{
	private Socket soc;
	private InputStream inputstream;
	
	public InputThread(String server, int port)
	{
		try {
			soc = new Socket(server, port);
			inputstream = soc.getInputStream();
		} catch (Exception e) {
			System.out.println("oops");
		} 
	}
	
	public void run()
	{
		Scanner in = new Scanner(inputstream);
		while(in.hasNextLine())
		{
			System.out.println(in.nextLine());
		}
	}
	
}
