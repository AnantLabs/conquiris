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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import net.conquiris.api.search.AbstractHitMapper;
import net.conquiris.api.search.HitMapper;
import net.conquiris.api.search.Searcher;
import net.conquiris.lucene.search.Hit;
import net.conquiris.support.TestSupport;

import org.apache.lucene.document.NumericField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Simple tests for a searcher.
 * @author Andres Rodriguez
 */
public class FieldSelectorTest {
	private static final HitMapper<Integer> MAPPER = new AbstractHitMapper<Integer>(ImmutableSet.of(TestSupport.ID)) {
		public Integer apply(Hit hit) {
			return ((NumericField) hit.get().getFieldable(TestSupport.ID)).getNumericValue().intValue();
		}
	};

	private boolean found(Searcher s, int value) {
		return s.getFirst(MAPPER, new TermQuery(TestSupport.termId(value)), null, null, null).isFound();
	}

	private void checkFound(Searcher s, int found, int notFound) {
		assertTrue(found(s, found));
		assertFalse(found(s, notFound));
	}

	@Test
	public void test() throws Exception {
		Directory d = TestSupport.createRAMDirectory(1, 10);
		Searcher s = Searchers.service(ReaderSuppliers.managed(d));
		checkFound(s, 4, 15);
		TestSupport.write(d, 11, 15);
		checkFound(s, 4, 25);
		checkFound(s, 14, 35);
	}

}
