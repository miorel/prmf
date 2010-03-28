package com.googlecode.prmf.starter;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connection 
	{
	public static Socket soc;
	public static InputStream is;
	public static PrintStream ps;
	
	public Connection(String socket, int port) throws UnknownHostException, IOException 
	{
		soc = new Socket(socket, port);
		ps = new PrintStream(soc.getOutputStream());
	}
	
}