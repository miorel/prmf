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

import java.util.LinkedList;
import java.util.Queue;

/**
 * A queue-backed thread.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type of elements in the queue
 */
public abstract class QueueThread<T> extends SpecializedThread {
	private final Queue<T> queue = prepareQueue();
	
	/**
	 * Allocates a new queue-backed thread.
	 */
	public QueueThread() {
		super();
	}

	/**
	 * Allocates a new queue-backed thread that will have the given name.
	 * 
	 * @param name
	 *            the name of the new thread
	 */
	public QueueThread(String name) {
		super(name);
	}

	/**
	 * Allocates a new queue-backed thread that will belong to the specified
	 * thread group.
	 * 
	 * @param group
	 *            the thread group
	 */
	public QueueThread(ThreadGroup group) {
		super(group);
	}

	/**
	 * Allocates a new queue-backed thread that will belong to the specified
	 * thread group and have the given name.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 */
	public QueueThread(ThreadGroup group, String name) {
		super(group, name);
	}

	/**
	 * Allocates a new queue-backed thread that will belong to the specified
	 * thread group and have the given name and stack size.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @param stackSize
	 *            the desired stack size, or zero to ignore this parameter
	 */
	public QueueThread(ThreadGroup group, String name, long stackSize) {
		super(group, name, stackSize);
	}

	/**
	 * Process elements from the queue (unless interrupted while waiting for one
	 * to be queued).
	 */
	@Override
	protected void work() {
		T element = null; 
		boolean process = true; // without this variable, the thread would process one more element after being interrupted
		synchronized(this.queue) {
			if(interrupted()) {
				process = false;
				interrupt();
			}
			else 
				while(this.queue.isEmpty())
					try {
						this.queue.wait(); // I'll wake when an element is queued
					}
					catch(InterruptedException e) {
						process = false;
						interrupt();
						break;
					}
			if(process) {
				long nap = getTimeBeforeProcessing(this.queue.peek());
				if(nap <= 0)
					element = this.queue.poll();
				else {
					process = false;
					try {
						this.queue.wait(nap);
					}
					catch(InterruptedException e) {
						interrupt();
					}
				}
			}
		}
		if(process)
			process(element);
	}
	
	/**
	 * Empties this thread's queue so that remaining elements can be garbage
	 * collected.
	 */
	@Override
	protected void cleanUp() {
		clear();	
	}

	/**
	 * Adds an element to this thread's queue.
	 * 
	 * @param element
	 *            the element to add
	 */
	public void enqueue(T element) {
		synchronized(this.queue) {
			this.queue.add(element);
			this.queue.notify(); // this thread is the only one that might be wait()-ing
		}
	}
	
	/**
	 * Empties this thread's queue.
	 */
	public void clear() {
		synchronized(this.queue) {
			this.queue.clear();
		}
	}
	
	@Override
	public void interrupt() {
		synchronized(this.queue) {
			super.interrupt();
		}
	}

	/**
	 * Prepares and returns a backing queue for this thread. Will be called
	 * once. Default implementation gives a plain old {@linkplain LinkedList
	 * linked list}.
	 * 
	 * @return the queue to use for backing this thread
	 */
	protected Queue<T> prepareQueue() {
		return new LinkedList<T>();
	}

	/**
	 * Defines how this thread processes the given element.
	 * 
	 * @param element
	 *            the element to process
	 */
	protected abstract void process(T element);

	/**
	 * Returns the number of milliseconds until the given element is "due" for
	 * processing. Will be called during the thread's work loop with the first
	 * element in the queue. While the result is positive, the thread will wait
	 * for the specified number of milliseconds, then check the (possibly
	 * updated) first element in the queue again. Therefore it's acceptable to
	 * underestimate the time but probably not to overestimate it. Default
	 * implementation returns zero, i.e. always process immediately without
	 * waiting.
	 * 
	 * @param element
	 *            the element whose due time should be checked
	 * @return the number of milliseconds until the element should be processed
	 */
	protected long getTimeBeforeProcessing(T element) {
		return 0;
	}
}
