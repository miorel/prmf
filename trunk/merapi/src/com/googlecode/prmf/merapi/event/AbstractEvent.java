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
package com.googlecode.prmf.merapi.event;

/**
 * Sample {@link Event} implementation.
 * 
 * @param <T>
 *            type of listeners on which this event may be triggered
 */
public abstract class AbstractEvent<T extends EventListener> implements Event<T> {
	private boolean consumed = false;
	
	/**
	 * Default constructor, does nothing.
	 */
	public AbstractEvent() {
	}
	
	@Override
	public void consume() {
		this.consumed = true;
	}

	@Override
	public boolean isConsumed() {
		return this.consumed;
	}

	@Override
	public void trigger(T listener) {
		if(!this.consumed)
			doTrigger(listener);
	}

	/**
	 * Defines the specific trigger of this event. Implementations may assume
	 * this method will not be called on consumed events.
	 * 
	 * @param listener
	 *            listener on which to trigger
	 */
	protected abstract void doTrigger(T listener);
}
