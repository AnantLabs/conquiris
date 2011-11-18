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
package net.conquiris.support;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import net.conquiris.api.search.DocMapper;
import net.conquiris.api.search.ItemResult;
import net.conquiris.api.search.PageResult;
import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.Searcher;
import net.conquiris.api.search.SearcherService;
import net.conquiris.search.ReaderSuppliers;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import com.google.common.base.Function;
import com.google.common.collect.Multimap;

/**
 * Document support methods for tests.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class TestSupport {
	private TestSupport() {
		throw new AssertionError();
	}

	static final String ID = "CQ-ID";
	static final String ANALYZED = "ANALYZED";

	private static final String value(int value) {
		return "value_" + value;
	}

	public static class Node {
		private int id;
		private String analyzed;

		public void test(int value) {
			assertEquals(id, value);
			assertEquals(analyzed, value(value));
		}

	}

	public static final DocMapper<Node> MAPPER = new DocMapper<Node>() {
		public Node map(int id, float score, Document doc, Multimap<String, String> fragments) {
			final Node node = new Node();
			node.id = ((NumericField) doc.getFieldable(ID)).getNumericValue().intValue();
			node.analyzed = doc.get(ANALYZED);
			return node;
		}
	};

	public static Document document(int value) {
		final Document document = new Document();
		final String text = value(value);
		document.add(new NumericField(ID, Field.Store.YES, true).setIntValue(value));
		document.add(new Field(ANALYZED, text, Field.Store.YES, Field.Index.ANALYZED));
		return document;
	}

	public static Term termId(int value) {
		return new Term(ID, NumericUtils.intToPrefixCoded(value));
	}

	public static ItemResult<Node> getFirst(Searcher searcher, Query query) {
		return searcher.getFirst(MAPPER, query, null, null, null);
	}

	public static ItemResult<Node> getFirst(Searcher searcher, int value) {
		return getFirst(searcher, new TermQuery(termId(value)));
	}

	public static PageResult<Node> getPage(Searcher searcher, int from, int to, int first, int max) {
		return searcher.getPage(MAPPER, NumericRangeQuery.newIntRange(ID, from, to, true, true), first, max, null, null,
				null);
	}

	public static int getCount(Searcher searcher) {
		return searcher.getCount(new MatchAllDocsQuery(), null, false).getTotalHits();
	}

	public static void found(Searcher searcher, int value) {
		final ItemResult<Node> item = getFirst(searcher, value);
		assertNotNull(item);
		assertTrue(item.isFound());
		final Node node = item.getItem();
		assertNotNull(node);
		node.test(value);
	}

	public static void notFound(Searcher searcher, int value) {
		final ItemResult<Node> item = getFirst(searcher, value);
		assertNotNull(item);
		assertFalse(item.isFound());
	}

	public static void write(Directory directory, int from, int to) throws IOException {
		IndexWriter w = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(
				Version.LUCENE_34)));
		int min = Math.min(from, to);
		int max = Math.max(from, to);
		for (int i = min; i <= max; i++) {
			w.addDocument(document(i));
		}
		w.close();
	}

	public static Directory createRAMDirectory(int from, int to) throws IOException {
		Directory directory = new RAMDirectory();
		write(directory, from, to);
		return directory;
	}

	public static ReaderSupplier createRAMSupplier(int from, int to) throws IOException {
		return ReaderSuppliers.directory(createRAMDirectory(from, to));
	}

	private static void checkPage(PageResult<?> page, int first, int size) {
		assertNotNull(page);
		assertEquals(page.size(), size);
		assertEquals(page.isEmpty(), size == 0);
		assertEquals(page.getFirstRequestedResult(), first);
		if (size > 0) {
			assertEquals(page.getFirstResult(), first);
			assertNotNull(page.getItems());
			assertEquals(page.getItems().size(), size);
		}
	}

	/**
	 * Excercise a searcher.
	 * @param searcher Searcher
	 * @param from Expected existing document lower bound.
	 * @param to Expected existing document upper bound.
	 */
	public static void performEmptyQueries(Searcher searcher) {
		for (int i = 0; i < 500; i++) {
			notFound(searcher, i);
		}
		checkPage(getPage(searcher, 3, 67, 0, 10), 0, 0);
		checkPage(getPage(searcher, 3, 67, 100, 10), 100, 0);
	}

	/**
	 * Excercise an empty index.
	 * @param searcher Searcher
	 * @param from Expected existing document lower bound.
	 * @param to Expected existing document upper bound.
	 */
	public static void performQueries(Searcher searcher, int from, int to) {
		final int n = to - from + 1;
		assertTrue(n > 1);
		found(searcher, from);
		found(searcher, to);
		notFound(searcher, from - 1);
		notFound(searcher, to + 1);
		found(searcher, (from + to) / 2);
		final int p = Math.min(5, n);
		checkPage(getPage(searcher, from, from + p, 0, p), 0, p);
		checkPage(getPage(searcher, to - p + 1, to, 0, p), 0, p);
		checkPage(getPage(searcher, to - p + 1, to + 2 * p, 0, 3 * p), 0, p);
		final int f = Math.max(0, from - 5);
		if (f > 0) {
			checkPage(getPage(searcher, f, f+10, 0, 20), 0, f + 10 - from + 1);
		}
	}

	/**
	 * Excercise a searcher service.
	 * @param service Searcher service
	 * @param from Expected existing document lower bound.
	 * @param to Expected existing document upper bound.
	 */
	public static void performQueriesInService(SearcherService service, final int from, final int to) {
		performQueries(service, from, to);
		service.search(new Function<Searcher, Object>() {
			@Override
			public Object apply(Searcher input) {
				performQueries(input, from, to);
				return null;
			}
		});
	}

	/*
	 * static Batch<Long> batch(int from, int to, long cp) throws InterruptedException { final
	 * Batch.Builder<Long> builder = Batch.builder(); for (int i = from; i <= to; i++) {
	 * builder.add(document(i)); } return builder.build(cp); }
	 */

}
