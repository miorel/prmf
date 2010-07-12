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
package com.googlecode.prmf.merapi.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.SelectorProvider;

import com.googlecode.prmf.merapi.nio.SelectingThread;

/**
 * A selector-backed thread for managing socket clients.
 * 
 * @author Miorel-Lucian Palii
 */
public class SocketClientsThread extends SelectingThread {
	/**
	 * Allocates a new socket clients thread. The thread will use the system-wide
	 * default selector provider.
	 * 
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SocketClientsThread() throws IOException {
		super();
	}

	/**
	 * Allocates a new socket clients thread that will have the given name. The
	 * thread will use the system-wide default selector provider.
	 * 
	 * @param name
	 *            the name of the new thread
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SocketClientsThread(String name) throws IOException {
		super(name);
	}

	/**
	 * Allocates a new socket clients thread that will belong to the specified thread
	 * group. The thread will use the system-wide default selector provider.
	 * 
	 * @param group
	 *            the thread group
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SocketClientsThread(ThreadGroup group) throws IOException {
		super(group);
	}

	/**
	 * Allocates a new socket clients thread that will belong to the specified thread
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
	public SocketClientsThread(ThreadGroup group, String name) throws IOException {
		super(group, name);
	}
	
	/**
	 * Allocates a new socket clients thread that will belong to the specified thread
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
	public SocketClientsThread(ThreadGroup group, String name, long stackSize) throws IOException {
		super(group, name, stackSize);
	}

	/**
	 * Allocates a new socket clients thread that will use the specified selector
	 * provider.
	 * 
	 * @param provider
	 *            the selector provider to use, or null to use the system-wide
	 *            default
	 * @throws IOException
	 *             if the selector provider propagates an I/O error during the
	 *             open operation
	 */
	public SocketClientsThread(SelectorProvider provider) throws IOException {
		super(provider);
	}

	/**
	 * Allocates a new socket clients thread that will use the specified selector
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
	public SocketClientsThread(SelectorProvider provider, String name) throws IOException {
		super(provider, name);
	}

	/**
	 * Allocates a new socket clients thread that will use the specified selector
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
	public SocketClientsThread(SelectorProvider provider, ThreadGroup group) throws IOException {
		super(provider, group);
	}

	/**
	 * Allocates a new socket clients thread that will use the specified selector
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
	public SocketClientsThread(SelectorProvider provider, ThreadGroup group, String name) throws IOException {
		super(provider, group, name);
	}

	/**
	 * Allocates a new socket clients thread that will use the specified selector
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
	public SocketClientsThread(SelectorProvider provider, ThreadGroup group, String name, long stackSize) throws IOException {
		super(provider, group, name, stackSize);
	}

	/**
	 * Tells the socket client attached to the given key to finish connecting.
	 */
	@Override
	protected void connect(SelectionKey key) {
		SocketClient client = (SocketClient) key.attachment(); 
		try {
			client.connect();
		}
		catch(IOException e) {
			report(e);
			client.stop();
		}
	}
	
	/**
	 * Tells the socket client attached to the given key to read from its channel.
	 */
	@Override
	protected void read(SelectionKey key) {
		SocketClient client = (SocketClient) key.attachment(); 
		try {
			client.read();
		}
		catch(IOException e) {
			report(e);
			client.stop();
		}
	}
	
	/**
	 * Tells the socket client attached to the given key to write to its channel.
	 */
	@Override
	protected void write(SelectionKey key) {
		SocketClient client = (SocketClient) key.attachment(); 
		try {
			client.write();
		}
		catch(IOException e) {
			report(e);
			client.stop();
		}
	}
}
