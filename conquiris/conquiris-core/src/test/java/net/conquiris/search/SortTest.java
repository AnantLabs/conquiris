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

import net.conquiris.api.search.PageResult;
import net.conquiris.api.search.Searcher;
import net.conquiris.lucene.search.SortBuilder;
import net.conquiris.support.TestSupport;
import net.conquiris.support.TestSupport.Node;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.Directory;
import org.testng.annotations.Test;

/**
 * Simple tests for a searcher.
 * @author Andres Rodriguez
 */
public class SortTest {

	@Test
	public void test() throws Exception {
		Directory d = TestSupport.createRAMDirectory(100, 1000);
		Searcher s = Searchers.service(ReaderSuppliers.managed(d));
		PageResult<Node> p = s.getPage(TestSupport.MAPPER, new MatchAllDocsQuery(), 0, 7, null, SortBuilder.newBuilder()
				.add(TestSupport.ID, -8, 14, 200, 1500, 300, 400, 500, 600, 700, 800).build(), null);
		p.get(0).test(200);
		p.get(1).test(300);
		p.get(2).test(400);
		p.get(3).test(500);
		p.get(4).test(600);
		p.get(5).test(700);
		p.get(6).test(800);
	}

}
