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

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.testng.annotations.Test;

/**
 * Counting queries tests.
 * @author Andres Rodriguez
 */
public class CountTest {
	private final TestIndex index = new TestIndex();

	public CountTest() {
	}

	private int count(Query q) throws IOException {
		return index.perform(CountingInquiry.of(q)).getTotalHits();
	}

	private int count() throws IOException {
		return count(new MatchAllDocsQuery());
	}

	@Test
	public void load() throws IOException {
		index.add(1, 1);
	}

	@Test(dependsOnMethods = "load")
	public void count1() throws IOException {
		assertEquals(count(), 1);
	}

	@Test(dependsOnMethods = "count1")
	public void count2() throws IOException {
		index.add(1, 1);
		assertEquals(count(), 2);
	}

	@Test(dependsOnMethods = "count2")
	public void count3() throws IOException {
		index.add(1, 2);
		assertEquals(count(), 3);
	}

	@Test(dependsOnMethods = "count3")
	public void countQ() throws IOException {
		assertEquals(count(index.term(1, 1)), 2);
	}

}
