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
package net.conquiris.search;

import static net.conquiris.support.TestSupport.found;
import static net.conquiris.support.TestSupport.notFound;
import static org.testng.Assert.assertEquals;
import net.conquiris.api.search.Searcher;
import net.conquiris.lucene.index.Terms;
import net.conquiris.lucene.search.Filters;
import net.conquiris.support.TestSupport;

import org.apache.lucene.search.Filter;
import org.apache.lucene.store.Directory;
import org.testng.annotations.Test;

/**
 * Simple tests for a searcher.
 * @author Andres Rodriguez
 */
public class SimpleSearcherTest {

	@Test
	public void test() throws Exception {
		Directory d = TestSupport.createRAMDirectory(1, 10);
		Searcher s = Searchers.service(ReaderSuppliers.managed(d));
		assertEquals(TestSupport.getCount(s), 10);
		TestSupport.write(d, 11, 15);
		assertEquals(TestSupport.getCount(s), 15);
		found(s, 2);
		found(s, 6);
		Filter f1 = Filters.terms(Terms.termBuilder(TestSupport.ID), 2, 3, 4);
		found(s, 2, f1);
		notFound(s, 6, f1);
		Filter f2 = Filters.negate(f1);
		notFound(s, 2, f2);
		found(s, 6, f2);
	}

}
