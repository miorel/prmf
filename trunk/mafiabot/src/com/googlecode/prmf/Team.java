package com.googlecode.prmf;

interface Team {
	// TODO why is an array of players passed as an argument?
	public boolean hasWon(Player[] players); 
	public boolean contains(Player player);
}
