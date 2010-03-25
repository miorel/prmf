// TODO there is a compilation error on the next line
+package com.googlecode.prmf;

public class Timer
{
	// TODO having multiple units of time seems like a poor design choice
	long lengthInMinutes;
	long lengthInMillis;
	long startTime;
	Timer(int length)
	{
		lengthInMinutes = length;
		lengthInMillis = lengthInMinutes * 60000;
	}
	
	public void startTimer()
	{
		startTime = System.currentTimeMillis();
	}
	
	public boolean status()
	{
		if (lengthInMinutes == 0)
			return false;
		//false for keep running, true for end
		long curTime = System.currentTimeMillis();
		long elapsed = curTime - startTime;
		if (elapsed > lengthInMillis)
		{
			Communicator.getInstance().sendMessage("channel", "Day is over!");
			return true;
		}
		long secondsLeft = lengthInMinutes*60 - (elapsed/1000);
		Communicator.getInstance().sendMessage("channel", "There are about " + secondsLeft + " seconds left in the day.");
		return false;
	}
}