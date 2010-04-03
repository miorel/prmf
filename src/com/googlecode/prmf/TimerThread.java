package com.googlecode.prmf;

import com.googlecode.prmf.starter.InputThread;

/**
 * This class 'should' create a thread that will sleep for some time,
 * then before ending, it will interrupt the inputThread!!
 */

public class TimerThread  implements Runnable{
	Thread timer, inputThread;
	final int daytime;

	public TimerThread(Thread inputThread, int daytime)
	{
		this.daytime = daytime;
		this.inputThread = inputThread;
		
		// TODO NO NO NO DO NOT START A THREAD FROM THE CONSTRUCTOR!!!
		timer = new Thread(this);
		timer.start(); 
	}
	
	public TimerThread(Thread inputThread)
	{
		this(inputThread, 6*60000);
	}
	
	public void run()
	{
		try {
			Thread.sleep(daytime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// TODO why is the channel name hardcoded?
		
		//This sends a message "TIMEUP" to mafiaListener to end SWAPSTATES.
		((InputThread)inputThread).sendMessage("#UFPT", "TIME IS UP!");
		((InputThread)inputThread).ceaseTimer();
		
		// TODO why don't you change the type of inputThread to InputThread so you don't have to cast?
	}
}
