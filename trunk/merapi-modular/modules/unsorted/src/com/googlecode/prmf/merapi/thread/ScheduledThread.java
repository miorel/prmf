/*
 * merapi - Multi-purpose Java library
 * Copyright (C) 2009-2010 Miorel-Lucian Palii <mlpalii@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.googlecode.prmf.merapi.thread;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import com.googlecode.prmf.merapi.util.Pair;

/**
 * <p>
 * A thread that runs tasks at scheduled times.
 * </p>
 * <p>
 * The tasks will be run on this thread, sequentially, so the assumption is that
 * they are simple enough that any delay between the execution of two tasks
 * scheduled close together will be negligible. If you anticipate running an
 * expensive task, consider spinning a new thread for it. Replace:
 * 
 * <pre>
 * thread.enqueue(task, time);
 * </pre>
 * 
 * with:
 * 
 * <pre>
 * thread.enqueue(new Runnable() {
 * 	&#064;Override
 * 	public void run() {
 * 		new Thread(task).start();
 * 	}
 * }, time);
 * </pre>
 * 
 * </p>
 * 
 * @author Miorel-Lucian Palii
 */
public class ScheduledThread extends QueueThread<Pair<Long,Runnable>> {
	/**
	 * Allocates a new scheduled thread.
	 */
	public ScheduledThread() {
		super();
	}

	/**
	 * Allocates a new scheduled thread that will have the given name.
	 * 
	 * @param name
	 *            the name of the new thread
	 */
	public ScheduledThread(String name) {
		super(name);
	}

	/**
	 * Allocates a new scheduled thread that will belong to the specified thread
	 * group.
	 * 
	 * @param group
	 *            the thread group
	 */
	public ScheduledThread(ThreadGroup group) {
		super(group);
	}

	/**
	 * Allocates a new scheduled thread that will belong to the specified thread
	 * group and have the given name.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 */
	public ScheduledThread(ThreadGroup group, String name) {
		super(group, name);
	}
	
	/**
	 * Allocates a new scheduled thread that will belong to the specified thread
	 * group and have the given name and stack size.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @param stackSize
	 *            the desired stack size, or zero to ignore this parameter
	 */
	public ScheduledThread(ThreadGroup group, String name, long stackSize) {
		super(group, name, stackSize);
	}
	
	@Override
	protected void process(Pair<Long,Runnable> pair) {
		try {
			pair.getSecond().run();
		}
		catch(Throwable t) {
			report(t);
		}
	}

	/**
	 * Adds a task to this thread's queue.
	 * 
	 * @param pair
	 *            the time when the task should be run and the task to run, as a
	 *            pair
	 */
	@Override
	public void enqueue(Pair<Long,Runnable> pair) {
		if(pair == null || pair.getSecond() == null)
			throw new NullPointerException("Can't queue null task.");
		if(pair.getFirst() == null)
			throw new NullPointerException("The time can't be null.");
		super.enqueue(pair);
	}

	/**
	 * Adds a task to this thread's queue.
	 * 
	 * @param task
	 *            the task to run
	 * @param time
	 *            the time (in milliseconds from the epoch) when the task should
	 *            be run
	 */
	public void enqueue(Runnable task, long time) {
		if(task == null)
			throw new NullPointerException("Can't queue null task.");
		super.enqueue(new Pair<Long,Runnable>(Long.valueOf(time), task));
	}

	/**
	 * Adds a task to this thread's queue.
	 * 
	 * @param time
	 *            the time (in milliseconds from the epoch) when the task should
	 *            be run
	 * @param task
	 *            the task to run
	 */
	public void enqueue(long time, Runnable task) {
		enqueue(task, time);
	}
	
	@Override
	protected long getTimeBeforeProcessing(Pair<Long,Runnable> task) {
		return task.getFirst().longValue() - System.currentTimeMillis();
	}

	@Override
	protected Queue<Pair<Long,Runnable>> prepareQueue() {
		return new PriorityQueue<Pair<Long,Runnable>>(1, new Comparator<Pair<Long,Runnable>>() {
			@Override
			public int compare(Pair<Long,Runnable> a, Pair<Long,Runnable> b) {
				return a.getFirst().compareTo(b.getFirst());
			}
		});
	}
}
