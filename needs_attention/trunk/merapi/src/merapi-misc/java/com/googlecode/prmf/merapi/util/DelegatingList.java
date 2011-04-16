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
import java.util.List;
import java.util.ListIterator;

import com.googlecode.prmf.merapi.util.iterators.ReversibleIterator;

/**
 * A list that delegates its list-related methods to another list. The goal is
 * to make it easier to use composition to build lists.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            the type of elements in the list
 */
public abstract class DelegatingList<T> implements List<T> {
	/**
	 * Default constructor, does nothing.
	 */
	public DelegatingList() {
	}
	
	/**
	 * Retrieves the list delegate.
	 * 
	 * @return the list delegate
	 */
	protected abstract List<T> getDelegate();
	
	@Override
	public boolean add(T e) {
		return getDelegate().add(e);
	}

	@Override
	public void add(int index, T element) {
		getDelegate().add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return getDelegate().addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return getDelegate().addAll(index, c);
	}

	@Override
	public void clear() {
		getDelegate().clear();
	}

	@Override
	public boolean contains(Object o) {
		return getDelegate().contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return getDelegate().containsAll(c);
	}

	@Override
	public T get(int index) {
		return getDelegate().get(index);
	}

	@Override
	public int indexOf(Object o) {
		return getDelegate().indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return getDelegate().isEmpty();
	}

	@Override
	public ReversibleIterator<T> iterator() {
		return Iterators.iterator(getDelegate());
	}

	@Override
	public int lastIndexOf(Object o) {
		return getDelegate().lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return getDelegate().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return getDelegate().listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return getDelegate().remove(o);
	}

	@Override
	public T remove(int index) {
		return getDelegate().remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return getDelegate().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return getDelegate().retainAll(c);
	}

	@Override
	public T set(int index, T element) {
		return getDelegate().set(index, element);
	}

	@Override
	public int size() {
		return getDelegate().size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return getDelegate().subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return getDelegate().toArray();
	}

	@Override
	public <U> U[] toArray(U[] a) {
		return getDelegate().toArray(a);
	}

	@Override
	public boolean equals(Object obj) {
		return getDelegate().equals(obj);
	}
	
	@Override
	public int hashCode() {
		return getDelegate().hashCode();
	}
}
