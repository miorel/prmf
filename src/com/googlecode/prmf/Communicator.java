package com.googlecode.prmf;

public class Communicator {
	private static Communicator instance;
	
	public static Communicator getInstance() {
		if(instance == null)
			instance = new Communicator();
		return instance;
	}
	
	public void sendMessage(String destination, String message) {
		
	}
}
