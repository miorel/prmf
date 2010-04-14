package com.googlecode.prmf.gamePlay;

import com.googlecode.prmf.connection.IOThread;

/**
 * This class 'should' create a thread that will sleep for some time,
 * then before ending, it will interrupt the inputThread!!
 */

public class TimerThread  implements Runnable{
	private Thread timer;
	private IOThread inputThread;
	private final int daytime;

	public TimerThread(IOThread inputThread, int daytime) {
		this.daytime = daytime;
		this.inputThread = inputThread;
		timer = new Thread(this, "Timer");
	}
	
	public TimerThread(IOThread inputThread) {
		this(inputThread, 6 * 60000);
	}
	
	public void run()
	{
		try {
			Thread.sleep(daytime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// TODO instead of returning if the thread is interrupted why not just
		// say to send the message and cease the timer if the thread is NOT
		// interrupted
		if(Thread.interrupted())
			return;

		inputThread.sendMessage(inputThread.getChannel(), "The town was not able to reach a consensus.");
		inputThread.ceaseTimer();
	}
	
	public Thread getTimer()
	{
		return timer;
	}
}
