/*
 * Merapi - Multi-purpose Java library
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
package com.googlecode.prmf.merapi.io;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

import com.googlecode.prmf.merapi.threads.SpecializedThread;

/**
 * A selector-backed thread for managing channels.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class SelectingThread extends SpecializedThread implements Registrar {
	private final Selector selector;

	/**
	 * Allocates a new selecting thread. The thread will use the system-wide
	 * default selector provider.
	 * 
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread() throws IOException {
		this((SelectorProvider) null);
	}

	/**
	 * Allocates a new selecting thread that will have the given name. The
	 * thread will use the system-wide default selector provider.
	 * 
	 * @param name
	 *            the name of the new thread
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(String name) throws IOException {
		this((SelectorProvider) null, name);
	}

	/**
	 * Allocates a new selecting thread that will belong to the specified thread
	 * group. The thread will use the system-wide default selector provider.
	 * 
	 * @param group
	 *            the thread group
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(ThreadGroup group) throws IOException {
		this((SelectorProvider) null, group);
	}

	/**
	 * Allocates a new selecting thread that will belong to the specified thread
	 * group and have the given name. The thread will use the system-wide
	 * default selector provider.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(ThreadGroup group, String name) throws IOException {
		this((SelectorProvider) null, group, name);
	}
	
	/**
	 * Allocates a new selecting thread that will belong to the specified thread
	 * group and have the given name and stack size. The thread will use the
	 * system-wide default selector provider.
	 * 
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @param stackSize
	 *            the desired stack size, or zero to ignore this parameter
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(ThreadGroup group, String name, long stackSize) throws IOException {
		this((SelectorProvider) null, group, name, stackSize);
	}

	/**
	 * Allocates a new selecting thread that will use the specified selector
	 * provider.
	 * 
	 * @param provider
	 *            the selector provider to use, or null to use the system-wide
	 *            default
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(SelectorProvider provider) throws IOException {
		super();
		this.selector = (provider != null ? provider : SelectorProvider.provider()).openSelector();
	}

	/**
	 * Allocates a new selecting thread that will use the specified selector
	 * provider and have the given name.
	 * 
	 * @param provider
	 *            the selector provider to use, or null to use the system-wide
	 *            default
	 * @param name
	 *            the name of the new thread
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(SelectorProvider provider, String name) throws IOException {
		this(provider, null, name);
	}

	/**
	 * Allocates a new selecting thread that will use the specified selector
	 * provider and belong to the specified thread group.
	 * 
	 * @param provider
	 *            the selector provider to use, or null to use the system-wide
	 *            default
	 * @param group
	 *            the thread group
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(SelectorProvider provider, ThreadGroup group) throws IOException {
		super(group);
		this.selector = (provider != null ? provider : SelectorProvider.provider()).openSelector();
	}

	/**
	 * Allocates a new selecting thread that will use the specified selector
	 * provider, belong to the specified thread group, and have the given name.
	 * 
	 * @param provider
	 *            the selector provider to use, or null to use the system-wide
	 *            default
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(SelectorProvider provider, ThreadGroup group, String name) throws IOException {
		this(provider, group, name, 0);
	}

	/**
	 * Allocates a new selecting thread that will use the specified selector
	 * provider, belong to the specified thread group, and have the given name
	 * and stack size.
	 * 
	 * @param provider
	 *            the selector provider to use, or <code>null</code> to use the
	 *            system-wide default
	 * @param group
	 *            the thread group
	 * @param name
	 *            the name of the new thread
	 * @param stackSize
	 *            the desired stack size, or zero to ignore this parameter
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SelectingThread(SelectorProvider provider, ThreadGroup group, String name, long stackSize) throws IOException {
		super(group, name, stackSize);
		this.selector = (provider != null ? provider : SelectorProvider.provider()).openSelector();
	}

	/**
	 * Selects channels that are ready for I/O operations and handles them.
	 * Stops when the thread is interrupted.
	 */
	@Override
	protected void work() {
		int s = 0;
		try {
			s = this.selector.select();
		}
		catch(IOException e) {
			handleSelectException(e);
		}
		if(s > 0) {
			Set<SelectionKey> keys = this.selector.selectedKeys();
			for(SelectionKey key: keys) {
				if(key.isValid() && key.isAcceptable())
					accept(key);
				if(key.isValid() && key.isConnectable())
					connect(key);
				if(key.isValid() && key.isReadable())
					read(key);
				if(key.isValid() && key.isWritable())
					write(key);
			}
			keys.clear();
		}
	}
	
	/**
	 * Closes the selector used by this thread.
	 */
	@Override
	protected void cleanUp() {
		try {
			this.selector.close();
		}
		catch(IOException e) {
			handleCloseException(e);
		}
	}

	/**
	 * Defines how this thread deals with an I/O exception thrown during the
	 * select process. The default behavior is to
	 * {@linkplain #report(Throwable) report} it and interrupt the thread.
	 * Subclasses should override this if needed.
	 * 
	 * @param e
	 *            the thrown exception
	 */
	protected void handleSelectException(IOException e) {
		report(e);
		interrupt();
	}

	/**
	 * Defines how this thread deals with an I/O exception thrown during the
	 * close process. The default behavior is to {@linkplain #report(Throwable)
	 * report} it.
	 * 
	 * @param e the thrown exception
	 */
	protected void handleCloseException(IOException e) {
		report(e);
	}

	/**
	 * Defines how this thread handles a key that's ready for accepting. Default
	 * implementation does nothing.
	 * 
	 * @param key
	 *            the key to handle
	 */
	protected void accept(SelectionKey key) {
	}

	/**
	 * Defines how this thread handles a key that's ready for connection.
	 * Default implementation does nothing.
	 * 
	 * @param key
	 *            the key to handle
	 */
	protected void connect(SelectionKey key) {
	}
	
	/**
	 * Defines how this thread handles a key that's ready for reading. Default
	 * implementation does nothing.
	 * 
	 * @param key
	 *            the key to handle
	 */
	protected void read(SelectionKey key) {
	}
	
	/**
	 * Defines how this thread handles a key that's ready for writing. Default
	 * implementation does nothing.
	 * 
	 * @param key
	 *            the key to handle
	 */
	protected void write(SelectionKey key) {
	}

	/**
	 * Registers a channel with this thread's selector, returning a selection
	 * key with the specified interest set and a <code>null</code> attachment.
	 * This method is equivalent to calling
	 * <code>{@linkplain #register(SelectableChannel,int,Object) register}(channel, ops, null)</code>.
	 */
	@Override
	public SelectionKey register(SelectableChannel channel, int ops) throws ClosedChannelException {
		return register(channel, ops, null);
	}
	
	/**
	 * Registers a channel with this thread's selector, returning a selection
	 * key with the specified interest set and attachment.
	 */
	@Override
	public SelectionKey register(SelectableChannel channel, int ops, Object attachment) throws ClosedChannelException {
		boolean wakeUp = true; // we should wake up if there are changes to the selector
		SelectionKey oldKey = channel.keyFor(this.selector);
		if(oldKey != null && oldKey.interestOps() == ops) // check the old key
			wakeUp = attachment == null ? oldKey.attachment() != null : !oldKey.attachment().equals(attachment);
		SelectionKey newKey = channel.register(this.selector, ops);
		newKey.attach(attachment); // need this separate call to handle null attachment correctly
		if(wakeUp)
			this.selector.wakeup();
		return newKey;
	}

	/**
	 * Cancels the given channel's registration.
	 */
	@Override
	public void deregister(SelectableChannel channel) {
		channel.keyFor(this.selector).cancel();
	}
}
