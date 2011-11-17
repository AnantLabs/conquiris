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

import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.RAMDirectory;
import org.testng.annotations.Test;

/**
 * Tests for EmptyIndex.
 * @author Andres Rodriguez
 */
public class EmptyIndexTest {

	/** Missing index. */
	@Test(expectedExceptions=IndexNotFoundException.class)
	public void missingRAM() throws Exception {
		IndexSearcher s = new IndexSearcher(new RAMDirectory());
		s.search(new MatchAllDocsQuery(), 5);
	}
	
	/** Search. */
	@Test
	public void search() throws Exception {
		IndexSearcher s = new IndexSearcher(EmptyIndex.getInstance().get().get());
		s.search(new MatchAllDocsQuery(), 5);
	}

}
