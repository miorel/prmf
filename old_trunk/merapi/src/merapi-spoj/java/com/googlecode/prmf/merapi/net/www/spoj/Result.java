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
package com.googlecode.prmf.merapi.net.www.spoj;

/**
 * Possible results of a submission to the Sphere Online Judge (SPOJ).
 *
 * @author Miorel-Lucian Palii
 * @see <a href="http://www.spoj.pl/">Sphere Online Judge (SPOJ)</a>
 */
public enum Result {
	/**
	 * Accepted: the submission was accepted by the judge.
	 */
	AC("accepted"),

	/**
	 * Compilation Error: the submission was not executed because it did not
	 * compile.
	 */
	CE("compilation error"),

	/**
	 * Runtime Error: the submission crashed while running.
	 */
	RE("runtime error"),

	/**
	 * System Error: there was a system error while the submission was running.
	 */
	SE("system error"),

	/**
	 * Time Limit Exceeded: the submission did not finish running in the allowed
	 * time.
	 */
	TLE("time limit exceeded"),

	/**
	 * Wrong Answer: the submission's output did not match the criteria expected
	 * by the judge.
	 */
	WA("wrong answer"),

	/**
	 * Pending: the submission has not yet been judged.
	 */
	PENDING("??", "pending");

	private final String abbreviation;
	private final String title;

	private Result(String title) {
		this.abbreviation = toString();
		this.title = title;
	}

	private Result(String abbreviation, String title) {
		this.abbreviation = abbreviation;
		this.title = title;
	}

	/**
	 * Returns the abbreviation of this result, e.g.&nbsp;<code>"TLE"</code>.
	 *
	 * @return an abbreviation
	 */
	public String getAbbreviation() {
		return this.abbreviation;
	}

	/**
	 * Returns a longer description of this result, e.g.&nbsp;
	 * <code>"time limit exceeded"</code>.
	 *
	 * @return a title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Obtains the result corresponding to the given abbreviation.
	 *
	 * @param abbreviation
	 *            the desired abbreviation
	 * @return a result object (or <code>null</code> if there isn't one with the
	 *         specified abbreviation)
	 */
	public static Result forAbbreviation(String abbreviation) {
		Result ret = null;
		for(Result res: Result.values())
			if(res.abbreviation.equals(abbreviation)) {
				ret = res;
				break;
			}
		return ret;
	}
}
