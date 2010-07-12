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

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a set of event listeners and distributes events to them.
 * 
 * @author Miorel-Lucian Palii
 * @param <L>
 *            type of listeners managed
 */
public class EventManager<L extends EventListener> {
	private final List<L> listeners;

	/**
	 * Prepares a new event manager, initialized with an empty listener set.
	 */
	public EventManager() {
		this.listeners = new ArrayList<L>();
	}

	/**
	 * Adds the specified listener to this manager's listener set.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addListener(L listener) {
		synchronized(this.listeners) {
			if(!this.listeners.contains(listener))
				this.listeners.add(listener);
		}
	}

	/**
	 * Removes the specified listener from this manager's listener set.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeListener(L listener) {
		synchronized(this.listeners) {
			this.listeners.remove(listener);
		}
	}

	/**
	 * Distributes the given event by triggering it on all managed listeners in
	 * the order in which they were added to this manager.
	 * 
	 * @param event
	 *            the event to trigger
	 */
	public void distribute(Event<L> event) {
		synchronized(this.listeners) {
			for(L listener: this.listeners)
				event.trigger(listener);
		}
	}
}
