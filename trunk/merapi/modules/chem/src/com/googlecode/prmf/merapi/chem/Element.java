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
package com.googlecode.prmf.merapi.chem;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.googlecode.prmf.merapi.util.Strings;

/**
 * An element of the periodic table.
 *
 * @author Miorel-Lucian Palii
 */
public final class Element {
	private static final ResourceBundle PERIODIC_TABLE = ResourceBundle.getBundle(Element.class.getPackage().getName() + ".PeriodicTableInfo");
	private static final HashMap<String,Element> bySymbol = new HashMap<String, Element>();

	private final String symbol;
	private final String name;
	private final int atomicNumber;
	private final double atomicRadius;
	private final double covalentRadius;

	/**
	 * Constructs an element object with the specified properties. This is
	 * private because users shouldn't directly construct element objects but
	 * instead get them with methods like {@link #forSymbol(String)}.
	 *
	 * @param symbol
	 *            the element symbol, e.g. "H"
	 * @param name
	 *            the element's full name, e.g. "Hydrogen"
	 * @param atomicNumber
	 *            the atomic number
	 * @param atomicRadius
	 *            the atomic radius in picometers
	 * @param covalentRadius
	 *            the covalent radius in picometers
	 */
	private Element(String symbol, String name, int atomicNumber, double atomicRadius, double covalentRadius) {
		this.symbol = symbol;
		this.name = name;
		this.atomicNumber = atomicNumber;
		this.atomicRadius = atomicRadius;
		this.covalentRadius = covalentRadius;
	}

	/**
	 * Retrieves the element with the specified symbol, or <code>null</code> if
	 * there is no known element with the specified symbol.
	 *
	 * @param symbol
	 *            the symbol of the element, e.g. <code>"H"</code> for hydrogen
	 * @return the element with the specified symbol or <code>null</code> if
	 *         there isn't one
	 */
	public static Element forSymbol(String symbol) {
		String sym = Strings.toTitleCase(symbol);
		Element ret = null;
		synchronized(bySymbol) {
			ret = bySymbol.get(sym);
			if(ret == null) {
				try {
					String[] arr = PERIODIC_TABLE.getString(sym).split(",");
					ret = new Element(sym, arr[0], Integer.parseInt(arr[1]), Double.parseDouble(arr[2]), Double.parseDouble(arr[3]));
				}
				catch(MissingResourceException e) {
					// it would be a lot of fun to throw a NoSuchElementException, but let's just return null for now
				}
				bySymbol.put(sym, ret);
			}
		}
		return ret;
	}

	/**
	 * Returns this element's symbol, e.g.&nbsp;<code>"H"</code> for hydrogen.
	 *
	 * @return this element's symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * Returns this element's full name, e.g.&nbsp;<code>"Hydrogen"</code>.
	 *
	 * @return this element's full name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns this element's atomic number.
	 *
	 * @return this element's atomic number
	 */
	public int getAtomicNumber() {
		return this.atomicNumber;
	}

	/**
	 * Returns this element's atomic radius in picometers.
	 *
	 * @return this element's atomic radius
	 */
	public double getAtomicRadius() {
		return this.atomicRadius;
	}

	/**
	 * Returns this element's covalent radius in picometers.
	 *
	 * @return this element's covalent radius
	 */
	public double getCovalentRadius() {
		return this.covalentRadius;
	}

	@Override
	public String toString() {
		return this.symbol;
	}

	@Override
	public int hashCode() {
		return this.atomicNumber;
	}

	@Override
	public boolean equals(Object obj) {
		/*
		 * Technically this is not required: This class is the only one creating
		 * Element objects, and it shouldn't create multiple copies of the same
		 * one, therefore Object's equals() ought to be adequate. But just so we
		 * don't rely on this, here goes...
		 */
		boolean ret = false;
		if(this == obj)
			ret = true;
		else if(obj instanceof Element) {
			Element e = (Element) obj;
			if(this.atomicNumber == e.atomicNumber) {
				ret = true;
				if(!this.symbol.equals(e.symbol)
						|| !this.name.equals(e.name)
						|| !Double.valueOf(this.atomicRadius).equals(Double.valueOf(e.atomicRadius))
						|| !Double.valueOf(this.covalentRadius).equals(Double.valueOf(e.covalentRadius)))
					throw new AssertionError("Two elements with the same atomic number have different fields!");
			}
		}
		return ret;
	}
}
