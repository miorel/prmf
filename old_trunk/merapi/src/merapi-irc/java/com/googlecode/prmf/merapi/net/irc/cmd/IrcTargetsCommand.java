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
package com.googlecode.prmf.merapi.net.irc.cmd;

import static com.googlecode.prmf.merapi.util.Iterators.adapt;
import static com.googlecode.prmf.merapi.util.Iterators.iterator;
import static com.googlecode.prmf.merapi.util.Iterators.list;

import java.util.List;
import java.util.Locale;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.Strings;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * An IRC command taking a list of targets as parameters.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class IrcTargetsCommand extends AbstractIrcCommand {
	private final String[] targets;

	/**
	 * Builds an IRC targets command from the given iterator.
	 * 
	 * @param targets
	 *            the command's targets
	 */
	public IrcTargetsCommand(Iterator<String> targets) {
		List<String> targetsList = list(adapt(targets));
		if(targetsList.isEmpty())
			throw new IllegalArgumentException("Can't " + getVerb() + " without targets.");
		for(String target: targetsList)
			validateString("targets", target, false, false);
		this.targets = targetsList.toArray(new String[targetsList.size()]);
	}
	
	/**
	 * Retrieves a string description of the action this command represents.
	 * 
	 * @return the command's verb
	 */
	protected String getVerb() {
		return getCommand().toLowerCase(Locale.ENGLISH);
	}
	
	/**
	 * Returns an iterator over this command's targets.
	 * 
	 * @return this command's targets
	 */
	public UniversalIterator<String> getTargets() {
		return iterator(this.targets);
	}
	
	@Override
	public UniversalIterator<String> getArguments() {
		return iterator(Strings.join(" ", getTargets()));
	}
}
