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

/**
 * <p>
 * Abstract superclass for threads with a specialized behavior.
 * </p>
 * <p>
 * One of Java's options for asynchronous execution is to subclass
 * {@link Thread}, overriding {@link Thread#run() run()}. A common idiom is to
 * repeat some loop until the thread is interrupted. This class adopts this
 * idiom and defines {@link #prepare()}, {@link #work()}, and {@link #cleanUp()}
 * with reasonable default implementations.
 * </p>
 * <p>
 * The signature of <code>run()</code> does not allow any checked exceptions,
 * which means you've gotta catch 'em all. Naturally you don't plan on just
 * silently trapping exceptions, but in many cases you don't need to do anything
 * more complicated than logging the problem. The {@link #report(Throwable)}
 * method was created for this purpose. It can be overridden of course, to
 * customize reporting behavior. 
 * </p>
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class SpecializedThread extends Thread {
	/**
	 * Allocates a new specialized thread.
	 */
	public SpecializedThread() {
		super();
	}

	/**
	 * Allocates a new specialized thread that will have the given name.
	 * 
	 * @param name
	 *            the name of the new thread
	 */
	public SpecializedThread(String name) {
		this(null, name);
	}

	/**
	 * Allocates a new specialized thread that will belong to the specified
	 * thread group.
	 * 
	 * @param group
	 *            the thread group
	 */
	public SpecializedThread(ThreadGroup group) {
		super(group, (Runnable) null);
	}
	
	/**
	 * Allocates a new specialized thread that will belong to the specified
	 * thread group and have the given name.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 */
	public SpecializedThread(ThreadGroup group, String name) {
		this(group, name, 0);
	}
	
	/**
	 * Allocates a new specialized thread that will belong to the specified
	 * thread group and have the given name and stack size.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @param stackSize
	 *            the desired stack size, or zero to ignore this parameter
	 */
	public SpecializedThread(ThreadGroup group, String name, long stackSize) {
		super(group, null, name, stackSize);
	}

	/**
	 * Defines how this thread reports exceptions or other
	 * <code>Throwable</code>s. The default is to print a stack trace to the
	 * standard error stream.
	 * 
	 * @param t
	 *            the <code>Throwable</code> to report
	 */
	protected void report(Throwable t) {
		t.printStackTrace();
	}
	
	/**
	 * Prepares to do work, does work until interrupted, then cleans up. 
	 */
	@Override
	public void run() {
		prepare();
		while(!interrupted())
			work();
		cleanUp();
	}
	
	/**
	 * Defines any initialization that must be done by this thread at the
	 * beginning of <code>run()</code> before entering the work loop.
	 */
	protected void prepare() {
	}

	/**
	 * Defines any clean up that must be done by this thread at the
	 * end of <code>run()</code> after finishing the work loop.
	 */
	protected void cleanUp() {
	}
	
	/**
	 * Defines the loop which this thread will execute until it's interrupted.
	 * If you only wish to execute the code once, you should probably override
	 * <code>run()</code> instead. Or explicitly interrupt at the end of this
	 * method (but that's lame).
	 */
	protected void work() {
		interrupt(); // yeah, so I guess I indirectly called my own code lame, what of it?
	}
}
