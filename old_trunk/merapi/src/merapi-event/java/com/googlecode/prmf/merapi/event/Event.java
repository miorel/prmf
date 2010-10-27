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
package com.googlecode.prmf.merapi.event;

/**
 * Object representation of an event.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type of listeners on which this event may be triggered
 */
public interface Event<T extends EventListener> {
	/**
	 * Triggers this event on the specified listener. If this event has been
	 * consumed, does nothing.
	 * 
	 * @param listener
	 *            listener on which to trigger
	 */
	public void trigger(T listener);

	/**
	 * Marks this event as having been handled, so that further attempts to
	 * trigger it on listeners will do nothing.
	 */
	public void consume();

	/**
	 * Checks whether or not this event has been consumed.
	 * 
	 * @return whether or not this event has been consumed
	 */
	public boolean isConsumed();
}
