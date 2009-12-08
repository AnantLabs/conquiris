/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.conquiris.lucene.search;

import static net.conquiris.lucene.search.TestIndex.value;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import net.conquiris.api.Grouping;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * Grouping implementation tests.
 * @author Andres Rodriguez
 */
public class GroupingTest {
	private GroupingBuilder<Integer> builder;

	public GroupingTest() {
	}

	private List<String> list(int... values) {
		final List<String> list = Lists.newArrayListWithCapacity(values.length);
		for (int v : values) {
			list.add(value(v));
		}
		return list;
	}

	private Grouping<Integer> build() {
		Grouping<Integer> g = builder.build();
		assertNotNull(g);
		return g;
	}

	private Grouping<Integer> build(int expected) {
		Grouping<Integer> g = build();
		assertEquals(g.getHits(), expected);
		return g;
	}

	private int[] keys(Grouping<Integer> g, int... values) {
		assertEquals(g.size(), values.length);
		final int[] sizes = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			final String key = value(values[i]);
			assertTrue(g.containsKey(key));
			sizes[i] = g.get(key).getHits();
			assertTrue(sizes[i] >= 1);
		}
		return sizes;
	}

	private void sizes(int[] sizes, int... values) {
		assertEquals(sizes.length, values.length);
		for (int i = 0; i < values.length; i++) {
			assertEquals(sizes[i], values[i]);
		}
	}

	private <T> Grouping<T> get(Grouping<T> g, int value) {
		assertFalse(g.isLast());
		final Grouping<T> next = g.get(value(value));
		assertNotNull(next);
		return next;
	}

	private <T> void leaf(Grouping<T> g, int hits, T payload) {
		assertTrue(g.isLast());
		assertEquals(g.getHits(), hits);
		assertEquals(g.getPayload(), payload);
	}

	private <T> void leaf(Grouping<T> g, int hits) {
		leaf(g, hits, null);
	}
	
	@Test
	public void one() throws IOException {
		builder = new GroupingBuilder<Integer>(list(1, 1), 0, null, null);
		Grouping<Integer> g = build(1);
		sizes(keys(g, 1), 1);
		Grouping<Integer> g1 = get(g, 1);
		sizes(keys(g1, 1), 1);
		leaf(get(g1, 1), 1);
	}

	@Test(dependsOnMethods = "one")
	public void two() {
		builder.add(list(1, 1), null, null);
		Grouping<Integer> g = build(2);
		sizes(keys(g, 1), 2);
		Grouping<Integer> g1 = get(g, 1);
		sizes(keys(g1, 1), 2);
		leaf(get(g1, 1), 2);
	}

	@Test(dependsOnMethods = "two")
	public void three() {
		builder.add(list(1, 2), null, null);
		Grouping<Integer> g = build(3);
		sizes(keys(g, 1), 3);
		Grouping<Integer> g1 = get(g, 1);
		sizes(keys(g1, 1, 2), 2, 1);
		leaf(get(g1, 1), 2);
		leaf(get(g1, 2), 1);
	}

}
