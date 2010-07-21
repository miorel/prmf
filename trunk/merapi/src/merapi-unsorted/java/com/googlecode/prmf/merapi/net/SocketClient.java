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
package com.googlecode.prmf.merapi.net;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.LinkedList;
import java.util.Queue;

import com.googlecode.prmf.merapi.event.EventManager;
import com.googlecode.prmf.merapi.io.Registrar;
import com.googlecode.prmf.merapi.net.event.ConnectedEvent;
import com.googlecode.prmf.merapi.net.event.DisconnectedEvent;
import com.googlecode.prmf.merapi.net.event.NetworkEvent;
import com.googlecode.prmf.merapi.net.event.NetworkEventListener;
import com.googlecode.prmf.merapi.net.event.ReadingEvent;
import com.googlecode.prmf.merapi.net.event.WritingEvent;

/**
 * <p>
 * An event-driven, non-blocking client socket manager.
 * </p>
 * <p>
 * The typical use of a client is:
 * <ol>
 * <li>Construct the client.</li>
 * <li>{@linkplain #addNetworkEventListener(NetworkEventListener) Register
 * listeners} with the client.</li>
 * <li>{@linkplain #start() Start} the client.</li>
 * </ol>
 * Note that this is not sufficient for complete functioning because the client
 * is non-blocking: the {@link #connect()}, {@link #read()}, and
 * {@link #write()} need to be called to perform I/O operations. The best way to
 * accomplish this is using a {@link Selector}: the client sets itself as
 * attachment when
 * {@linkplain Registrar#register(java.nio.channels.SelectableChannel,int,Object)
 * registering} with a {@link Registrar}, so the I/O methods can be called when
 * the selection key is {@linkplain SelectionKey#isConnectable() connectable},
 * {@linkplain SelectionKey#isReadable() readable}, or
 * {@linkplain SelectionKey#isWritable() writable}, respectively.
 * </p>
 * <p>
 * This class is abstract to allow for different types of messages. Subclasses
 * must implement {@link #scanBuffer(StringBuilder)} to define message parsing.
 * They should probably also define delegates to the protected
 * {@link #send(CharSequence)} to allow sending messages, as well as custom,
 * protocol-specific events (because the {@linkplain NetworkEvent network ones}
 * are likely too general).
 * </p>
 * 
 * @author Miorel-Lucian Palii
 * @see SocketClientsThread
 */
public abstract class SocketClient {
	/**
	 * The name of the default {@link Charset} to use.
	 */
	public static final String CHARSET_NAME = "ISO-8859-1";
	
	/**
	 * The default buffer size.
	 */
	public static final int BUFFER_SIZE = 1 << 12;
	
	private final Registrar registrar;
	private final SelectorProvider provider;
	private final SocketAddress address;
	private SocketChannel channel;
	
	private final Queue<String> outputQueue;
	private ByteBuffer outgoingBuffer;

	private final ByteBuffer byteBuffer;
	private final CharBuffer charBuffer;
	private final StringBuilder stringBuffer;

	private final CharsetDecoder decoder;
	private final CharsetEncoder encoder;
	
	private final EventManager<NetworkEventListener> networkEventManager;
	
	/**
	 * Builds a client that will connect to the specified address, register with
	 * the specified registrar, and use a default provider to open channels.
	 * 
	 * @param address
	 *            the address to connect to
	 * @param registrar
	 *            the registrar which will dictate network activity
	 */
	public SocketClient(SocketAddress address, Registrar registrar) {
		this(address, registrar, null);
	}

	/**
	 * Builds a client that will connect to the specified address, register with
	 * the specified registrar, and use the specified provider to open channels.
	 * 
	 * @param address
	 *            the address to connect to
	 * @param registrar
	 *            the registrar which will dictate network activity
	 * @param provider
	 *            the provider to use for opening channels, or <code>null</code>
	 *            to use the default provider
	 */
	public SocketClient(SocketAddress address, Registrar registrar, SelectorProvider provider) {
		if(address == null)
			throw new NullPointerException("The address may not be null.");
		if(registrar == null)
			throw new NullPointerException("The registrar may not be null.");
		this.address = address;
		this.registrar = registrar;
		this.provider = provider == null ? SelectorProvider.provider() : provider;
		this.outputQueue = new LinkedList<String>();
		this.byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		this.charBuffer = CharBuffer.allocate(BUFFER_SIZE);
		this.stringBuffer = new StringBuilder();
		Charset charset = Charset.forName(CHARSET_NAME);
		this.decoder = charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
		this.encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
		this.networkEventManager = new EventManager<NetworkEventListener>();
	}
	
	/**
	 * Returns this client's address.
	 * 
	 * @return this client's address
	 */
	public SocketAddress getAddress() {
		return this.address;
	}

	private void setChannelInterest(int ops) throws ClosedChannelException {
		synchronized(this.stringBuffer) {
			this.registrar.register(this.channel, ops, this);
		}
	}

	private void listenForConnect() throws ClosedChannelException {
		setChannelInterest(SelectionKey.OP_CONNECT);
	}

	private void listenForRead() throws ClosedChannelException {
		setChannelInterest(SelectionKey.OP_READ);
	}

	private void listenForReadWrite() throws ClosedChannelException {
		setChannelInterest(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}

	/**
	 * Starts this client's connection process. If the client was already
	 * connected or connecting, it will be {@link #stop()}-ed first.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void start() throws IOException {
		synchronized(this.stringBuffer) {
			stop(); // stop first
			this.channel = this.provider.openSocketChannel();
			this.channel.configureBlocking(false);
			if(this.channel.connect(this.address))
				connect();
			else
				listenForConnect();
		}
	}

	/**
	 * Tells this client to attempt finishing the process of connecting. Users
	 * should not invoke this directly, but let the registrar do so when the
	 * socket is ready for connecting. If you're looking for the method that
	 * starts the client's connection process, it's called {@link #start()}.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void connect() throws IOException {
		synchronized(this.stringBuffer) {
			if(this.channel == null)
				throw new IllegalStateException("This client is stopped or not yet started.");
			try {
				if(this.channel.finishConnect()) {
					listenForReadWrite();
					distributeNetworkEvent(new ConnectedEvent(this));
				}
			}
			catch(ConnectException e) { // timeout
				stop();
			}
		}
	}

	/**
	 * Stops (disconnects) this client. The underlying socket is closed and its
	 * channel
	 * {@linkplain Registrar#deregister(java.nio.channels.SelectableChannel)
	 * deregistered}. Network event listeners will be notified of the
	 * disconnection.
	 */
	public void stop() {
		synchronized(this.stringBuffer) {
			synchronized(this.outputQueue) {
				this.outputQueue.clear();
			}
			SocketChannel tmp = this.channel;
			if(tmp != null) {
				this.channel = null;
				this.registrar.deregister(tmp);
				try {
					tmp.socket().close();
				}
				catch(IOException e) {
					report(e);
				}
				distributeNetworkEvent(new DisconnectedEvent(this));
			}
		}
	}
	
	/**
	 * Defines how this client reports exceptions or other
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
	 * Tells this client to attempt reading a message from its incoming buffer.
	 * Users should not invoke this directly, but let the registrar do so when
	 * the socket is ready for reading.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void read() throws IOException {
		synchronized(this.stringBuffer) {
			if(this.channel == null)
				throw new IllegalStateException("This client is stopped or not yet started.");
			if(this.channel.read(this.byteBuffer) < 0)
				stop();
			else {
				this.byteBuffer.flip();
				this.decoder.decode(this.byteBuffer, this.charBuffer, false);
				this.charBuffer.flip();
				this.stringBuffer.append(this.charBuffer);
				this.byteBuffer.clear();
				this.charBuffer.clear();
				scanBuffer(this.stringBuffer);
			}
		}
	}

	/**
	 * Tells this client to attempt writing a message from its outgoing queue to
	 * the underlying socket. Users should not invoke this directly, but let the
	 * registrar do so when the socket is ready for writing.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void write() throws IOException {
		synchronized(this.stringBuffer) {
			if(this.channel == null)
				throw new IllegalStateException("This client is stopped or not yet started.");
			if(this.outgoingBuffer != null)
				doWrite();
			else {
				String message;
				synchronized(this.outputQueue) {
					message = this.outputQueue.poll();
					if(this.outputQueue.isEmpty())
						try {
							listenForRead();
						}
						catch(ClosedChannelException e) {
							report(e);
						}
				}
				if(message != null) {
					raiseWritingEvent(message);
					this.outgoingBuffer = this.encoder.encode(CharBuffer.wrap(message));
					doWrite();
				}
			}
		}
	}
	
	private void doWrite() throws IOException {
		synchronized(this.stringBuffer) {
			this.channel.write(this.outgoingBuffer);
			if(this.outgoingBuffer.remaining() == 0)
				this.outgoingBuffer = null;
		}
	}

	/**
	 * Defines how this client scans messages from its incoming buffer.
	 * Implementations should discard characters from the beginning of the
	 * buffer when a message is successfully read, but should not otherwise
	 * modify the contents of the buffer. Read messages should be announced by
	 * {@linkplain #raiseReadingEvent(String) raising a reading event}.
	 * 
	 * @param buffer
	 *            a reference to this client's incoming buffer
	 */
	protected abstract void scanBuffer(StringBuilder buffer);

	/**
	 * Queues a literal message in this client's outgoing queue.
	 * 
	 * @param message
	 *            the messages to queue
	 */
	protected void send(CharSequence message) {
		synchronized(this.outputQueue) {
			this.outputQueue.add(message.toString());
			try {
				listenForReadWrite();
			}
			catch(ClosedChannelException e) {
				report(e);
			}
		}
	}

	/**
	 * Adds a network listener to this client's listener set.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addNetworkEventListener(NetworkEventListener listener) {
		this.networkEventManager.addListener(listener);
	}

	/**
	 * Distributes a network event to the network listeners registered with this
	 * client.
	 * 
	 * @param event
	 *            the event to distribute
	 */
	protected void distributeNetworkEvent(NetworkEvent event) {
		this.networkEventManager.distribute(event);
	}

	/**
	 * Removes a network listener from this client's listener set.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeNetworkEventListener(NetworkEventListener listener) {
		this.networkEventManager.removeListener(listener);
	}

	/**
	 * Raises a reading event with the specified message. This should be used by
	 * subclasses in their implementation of {@link #scanBuffer(StringBuilder)}
	 * in order to notify listeners.
	 * 
	 * @param message
	 *            the message of the reading event
	 */
	protected void raiseReadingEvent(String message) {
		distributeNetworkEvent(new ReadingEvent(this, message));
	}

	/**
	 * Raises a writing event with the specified message. This is called to
	 * notify listeners when the write actually happens (since it might not
	 * immediately after the message gets queued).
	 * 
	 * @param message
	 *            the message of the writing event
	 */
	protected void raiseWritingEvent(String message) {
		distributeNetworkEvent(new WritingEvent(this, message));
	}
}
