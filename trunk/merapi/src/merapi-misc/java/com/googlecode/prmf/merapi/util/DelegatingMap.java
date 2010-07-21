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
package com.googlecode.prmf.merapi.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * A map that delegates its map-related methods to another map. The goal is to
 * make it easier to use composition to build maps.
 * 
 * @author Miorel-Lucian Palii
 * @param <K>
 *            the type of keys maintained by this map
 * @param <V>
 *            the type of mapped values
 */
public abstract class DelegatingMap<K,V> implements Map<K,V>, Iterable<Entry<K,V>> {
	/**
	 * Default constructor, does nothing.
	 */
	public DelegatingMap() {
	}

	/**
	 * Retrieves this map's delegate.
	 * 
	 * @return the delegate
	 */
	protected abstract Map<K,V> getDelegate();

	@Override
	public void clear() {
		getDelegate().clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return getDelegate().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return getDelegate().containsValue(value);
	}

	@Override
	public Set<Map.Entry<K,V>> entrySet() {
		return getDelegate().entrySet();
	}

	@Override
	public V get(Object key) {
		return getDelegate().get(key);
	}

	@Override
	public boolean isEmpty() {
		return getDelegate().isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return getDelegate().keySet();
	}

	@Override
	public V put(K key, V value) {
		return getDelegate().put(key, value);
	}

	@Override
	public void putAll(Map<? extends K,? extends V> m) {
		getDelegate().putAll(m);
	}

	@Override
	public V remove(Object key) {
		return getDelegate().remove(key);
	}

	@Override
	public int size() {
		return getDelegate().size();
	}

	@Override
	public Collection<V> values() {
		return getDelegate().values();
	}
	
	@Override
	public boolean equals(Object obj) {
		return getDelegate().equals(obj);
	}
	
	@Override
	public int hashCode() {
		return getDelegate().hashCode();
	}

	/**
	 * Returns an iterator over the entries in this map.
	 * 
	 * @return an iterator over the entries in this map
	 */
	@Override
	public UniversalIterator<Entry<K,V>> iterator() {
		return Iterators.adapt(entrySet());
	}
}
