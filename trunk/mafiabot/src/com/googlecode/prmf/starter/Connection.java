package com.googlecode.prmf.starter;

//TODO organize imports; Eclipse can do this automatically for you with Ctrl+Shift+O
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connection {
	// TODO why are these static and public?
	public static Socket soc;
	public static InputStream is;
	public static PrintStream ps; // TODO variable names are allowed to be longer than three letters
	
	//TODO String socket should probably be renamed to String hostname, it's more descriptive
	public static void makeConnection(String socket, int port) throws UnknownHostException, IOException 
	{
		soc = new Socket(socket, port);
		ps = new PrintStream(soc.getOutputStream());
		is = soc.getInputStream();
	}
	
}
