/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package com.googlecode.prmf.merapi.thread;

/**
 * A thread that runs tasks from a queue, in order.
 */
public class WorkerThread extends QueueThread<Runnable> {	
	/**
	 * Allocates a new worker thread.
	 */
	public WorkerThread() {
		super();
	}

	/**
	 * Allocates a new worker thread that will have the given name.
	 * 
	 * @param name
	 *            the name of the new thread
	 */
	public WorkerThread(String name) {
		super(name);
	}

	/**
	 * Allocates a new worker thread that will belong to the specified thread
	 * group.
	 * 
	 * @param group
	 *            the thread group
	 */
	public WorkerThread(ThreadGroup group) {
		super(group);
	}

	/**
	 * Allocates a new worker thread that will belong to the specified thread
	 * group and have the given name.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 */
	public WorkerThread(ThreadGroup group, String name) {
		super(group, name);
	}
	
	/**
	 * Allocates a new worker thread that will belong to the specified thread
	 * group and have the given name and stack size.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @param stackSize
	 *            the desired stack size, or zero to ignore this parameter
	 */
	public WorkerThread(ThreadGroup group, String name, long stackSize) {
		super(group, name, stackSize);
	}

	/**
	 * Runs the task.
	 * 
	 * @param task the task to run
	 */
	@Override
	protected void process(Runnable task) {
		try {
			task.run();
		}
		catch(Throwable t) {
			report(t);
		}
	}

	/**
	 * Adds a task to this thread's queue.
	 * 
	 * @param task
	 *            the task to add
	 */
	@Override
	public void enqueue(Runnable task) {
		if(task == null)
			throw new NullPointerException("Can't queue null task.");
		super.enqueue(task);
	}
}
