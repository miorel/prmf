package com.googlecode.prfm;

public class Communicator {
	private static Communicator instance;
	
	public Communicator getInstance() {
		if(instance == null)
			instance = new Communicator();
		return instance;
	}
	
	public void sendMessage(String destination, String message) {
		
	}
}
