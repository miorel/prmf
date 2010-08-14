package com.googlecode.prmf.merapi.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PairTest {
	private String first = "blue";
	private Integer second = Integer.valueOf(42);

	@Test
	public void testConstructor() {
		assertNotNull(buildNullNull());
		assertNotNull(buildNullValid());
		assertNotNull(buildValidNull());
		assertNotNull(buildValidValid());
	}

	@Test
	public void testGetFirst() {
		assertEquals(buildNullNull().getFirst(), null);
		assertEquals(buildNullValid().getFirst(), null);
		assertEquals(buildValidNull().getFirst(), this.first);
		assertEquals(buildValidValid().getFirst(), this.first);
	}

	@Test
	public void testGetSecond() {
		assertEquals(buildNullNull().getSecond(), null);
		assertEquals(buildNullValid().getSecond(), this.second);
		assertEquals(buildValidNull().getSecond(), null);
		assertEquals(buildValidValid().getSecond(), this.second);
	}

	@Test
	public void testEquals() {
		assertEquals(buildNullNull(), buildNullNull());
		assertEquals(buildNullValid(), buildNullValid());
		assertEquals(buildValidNull(), buildValidNull());
		assertEquals(buildValidValid(), buildValidValid());
	}

	private Pair<String,Integer> buildNullNull() {
		return new Pair<String,Integer>(null, null);
	}

	private Pair<String,Integer> buildNullValid() {
		return new Pair<String,Integer>(null, this.second);
	}

	private Pair<String,Integer> buildValidNull() {
		return new Pair<String,Integer>(this.first, null);
	}

	private Pair<String,Integer> buildValidValid() {
		return new Pair<String,Integer>(this.first, this.second);
	}
}
