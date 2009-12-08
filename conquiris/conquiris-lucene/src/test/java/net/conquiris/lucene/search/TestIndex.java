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

import java.io.IOException;

import net.conquiris.api.Result;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;

/**
 * Index support for tests.
 * @author Andres Rodriguez
 */
final class TestIndex {
	private static final String FIELD = "field";
	private static final String VALUE = "value";

	static String field(int i) {
		return FIELD + i;
	}

	static String value(int i) {
		return VALUE + i;
	}

	/** Directory. */
	private final RAMDirectory directory = new RAMDirectory();

	TestIndex() {
	}

	void add(boolean store, int... values) throws IOException {
		if (values == null || values.length < 2) {
			return;
		}
		final Document d = new Document();
		for (int i = 0; i < values.length - 1; i += 2) {
			d.add(new Field(field(values[i]), value(values[i + 1]), store ? Store.YES : Store.NO, Index.NOT_ANALYZED));
		}
		final IndexWriter w = new IndexWriter(directory, new SimpleAnalyzer(), MaxFieldLength.UNLIMITED);
		w.addDocument(d);
		w.optimize();
		w.commit();
		w.close();
	}

	void add(int... values) throws IOException {
		add(true, values);
	}

	<T extends Result> T perform(Inquiry<T> inquiry) throws IOException {
		final IndexSearcher searcher = new IndexSearcher(directory);
		try {
			return Inquirer.perform(searcher, inquiry);
		} finally {
			searcher.close();
		}
	}
}
