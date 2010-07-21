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

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * Makes the task of implementing the {@link Registrar} interface marginally
 * easier.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class AbstractRegistrar implements Registrar {
	/**
	 * Default constructor, does nothing.
	 */
	public AbstractRegistrar() {
	}
	
	@Override
	public SelectionKey register(SelectableChannel channel, int ops) throws ClosedChannelException {
		return register(channel, ops, null);
	}
}
