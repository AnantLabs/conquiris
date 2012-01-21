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

import net.conquiris.api.search.AbstractHitMapper;
import net.conquiris.api.search.HitMapper;
import net.conquiris.api.search.ItemResult;
import net.conquiris.api.search.PageResult;
import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.Searcher;
import net.conquiris.api.search.SearcherService;
import net.conquiris.lucene.Conquiris;
import net.conquiris.lucene.document.DocumentBuilder;
import net.conquiris.lucene.search.Hit;
import net.conquiris.schema.IntegerSchemaItem;
import net.conquiris.schema.SchemaItems;
import net.conquiris.schema.TextSchemaItem;
import net.conquiris.search.ReaderSuppliers;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.NumericUtils;

import com.google.common.base.Function;

/**
 * Document support methods for tests.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class TestSupport {
	private TestSupport() {
		throw new AssertionError();
	}

	public static final IntegerSchemaItem ID = SchemaItems.intValue("CQ-ID", true, true, true);
	public static final TextSchemaItem BASE = SchemaItems.id("BASE", true, true, true);
	public static final TextSchemaItem ANALYZED = SchemaItems.tokenized("ANALYZED", true);

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

	public static final HitMapper<Node> MAPPER = new AbstractHitMapper<Node>() {
		public Node apply(Hit hit) {
			final Node node = new Node();
			node.id = hit.item(ID).get();
			;
			node.analyzed = hit.item(ANALYZED).get();
			return node;
		}
	};

	public static Document document(int value, String base) {
		final DocumentBuilder builder = DocumentBuilder.create();
		builder.add(ID, value);
		final String text = value(value);
		builder.add(BASE, text);
		builder.add(ANALYZED, text);
		return builder.build();
	}

	public static Document document(int value) {
		return document(value, BASE.getName());
	}

	public static Term termId(int value) {
		return new Term(ID.getName(), NumericUtils.intToPrefixCoded(value));
	}

	public static ItemResult<Node> getFirst(Searcher searcher, Query query, Filter filter) {
		return searcher.getFirst(MAPPER, query, filter, null, null);
	}

	public static ItemResult<Node> getFirst(Searcher searcher, Query query) {
		return getFirst(searcher, query, null);
	}

	public static ItemResult<Node> getFirst(Searcher searcher, int value, Filter filter) {
		return getFirst(searcher, new TermQuery(termId(value)), filter);
	}

	public static ItemResult<Node> getFirst(Searcher searcher, int value) {
		return getFirst(searcher, value, null);
	}

	public static PageResult<Node> getPage(Searcher searcher, int from, int to, int first, int max, Filter filter) {
		return searcher.getPage(MAPPER, NumericRangeQuery.newIntRange(ID.getName(), from, to, true, true), first, max,
				filter, null, null);
	}

	public static PageResult<Node> getPage(Searcher searcher, int from, int to, int first, int max) {
		return getPage(searcher, from, to, first, max, null);
	}

	public static int getCount(Searcher searcher, Filter filter) {
		return searcher.getCount(new MatchAllDocsQuery(), filter, false).getTotalHits();
	}

	public static int getCount(Searcher searcher) {
		return getCount(searcher, null);
	}

	public static void found(Searcher searcher, int value, Filter filter) {
		final ItemResult<Node> item = getFirst(searcher, value, filter);
		assertNotNull(item);
		assertTrue(item.isFound());
		final Node node = item.getItem();
		assertNotNull(node);
		node.test(value);
	}

	public static void found(Searcher searcher, int value) {
		found(searcher, value, null);
	}

	public static void notFound(Searcher searcher, int value, Filter filter) {
		final ItemResult<Node> item = getFirst(searcher, value, filter);
		assertNotNull(item);
		assertFalse(item.isFound());
	}

	public static void notFound(Searcher searcher, int value) {
		notFound(searcher, value, null);
	}

	public static void write(Directory directory, String base, int from, int to) throws IOException {
		IndexWriter w = new IndexWriter(directory, Conquiris.writerConfig());
		int min = Math.min(from, to);
		int max = Math.max(from, to);
		for (int i = min; i <= max; i++) {
			w.addDocument(document(i, base));
		}
		w.close();
	}

	public static void write(Directory directory, int from, int to) throws IOException {
		write(directory, null, from, to);
	}

	public static Directory createRAMDirectory(String base, int from, int to) throws IOException {
		Directory directory = new RAMDirectory();
		write(directory, base, from, to);
		return directory;
	}

	public static Directory createRAMDirectory(int from, int to) throws IOException {
		return createRAMDirectory(null, from, to);
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
			checkPage(getPage(searcher, f, f + 10, 0, 20), 0, f + 10 - from + 1);
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
}
