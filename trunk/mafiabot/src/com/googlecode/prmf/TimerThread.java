package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

/**
 * This class 'should' create a thread that will sleep for some time,
 * then before ending, it will interrupt the inputThread!!
 */

public class TimerThread  implements Runnable{
	Thread timer;
	IOThread inputThread;
	final int daytime;

	public TimerThread(IOThread inputThread, int daytime)
	{
		this.daytime = daytime;
		this.inputThread = inputThread;
		timer = new Thread(this);
	 
	}
	
	public TimerThread(IOThread inputThread)
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
		if(Thread.interrupted())
			return;
		inputThread.sendMessage("#UFPT", "The town was not able to reach a consensus.");
		inputThread.ceaseTimer();

		
		// TODO why don't you change the type of inputThread to InputThread so you don't have to cast?
	}
}
