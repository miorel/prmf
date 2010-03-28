package com.googlecode.prmf;

/*
 * This class 'should' create a thread that will sleep for some time,
 * then before ending, it will interrupt the inputThread!!
 */

public class TimerThread  implements Runnable{
	
Thread timer, inputThread;
final int daytime = 3*60000; // 3 minutes

	public TimerThread(Thread inputThread)
	{
		this.inputThread = inputThread;
		timer = new Thread(this);
		timer.start(); 
	}
	
	public void run()
	{
		try {
			//I do not understand this eclipse warning. (rodrigo)
			timer.sleep(daytime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//inputThread.stop();
		inputThread.interrupt();
	}
	
}
