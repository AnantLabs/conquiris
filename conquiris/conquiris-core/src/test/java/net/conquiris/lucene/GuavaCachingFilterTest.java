/*
 * Copyright (C) the original author or authors.
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
package net.conquiris.lucene;

import static org.testng.Assert.assertEquals;
import net.conquiris.api.search.Searcher;
import net.conquiris.search.ReaderSuppliers;
import net.conquiris.search.Searchers;
import net.conquiris.support.TestSupport;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.TermsFilter;
import org.apache.lucene.store.Directory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for GuavaCachingFilter.
 * @author Andres Rodriguez
 */
public class GuavaCachingFilterTest {
	private static final String B = "FILTERED";

	private Directory directory;
	private Searcher s;
	private GuavaCachingFilter f;

	/** Initialization. */
	@BeforeClass
	public void setUp() throws Exception {
		directory = TestSupport.createRAMDirectory(1, 3);
		s = Searchers.service(ReaderSuppliers.managed(directory));
		TermsFilter tf = new TermsFilter();
		tf.addTerm(new Term(TestSupport.BASE, B));
		f = new GuavaCachingFilter(tf);
	}

	private void count(int n, Filter f) {
		assertEquals(TestSupport.getCount(s, f), n);
	}

	/** Test filter. */
	@Test
	public void filter() throws Exception {
		count(3, null);
		TestSupport.write(directory, 4, 5);
		count(5, null);
		count(0, f);
		int t = 5;
		int b = 0;
		for (int i = 1; i < 100; i++) {
			int start = i*10;
			TestSupport.write(directory, B, start+1, start+5);
			t += 5;
			b += 5;
			count(t, null);
			count(b, f);
			count(b, f);
		}
	}

}
