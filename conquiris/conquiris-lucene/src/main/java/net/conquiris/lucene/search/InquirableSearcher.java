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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;

/**
 * Inquirable implementation based on a Lucene searcher.
 * @author Andres Rodriguez
 */
public final class InquirableSearcher implements Inquirable {
	/** Lucene searcher. */
	private final Searcher searcher;

	/**
	 * Returns a new Inquirable based on a Lucene searcher.
	 * @param searcher Lucene searcher.
	 * @return The requested inquirable.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public static InquirableSearcher of(Searcher searcher) {
		return new InquirableSearcher(searcher);
	}

	/**
	 * Constructor.
	 * @param searcher Lucene searcher.
	 */
	private InquirableSearcher(Searcher searcher) {
		this.searcher = checkNotNull(searcher, "A Lucene searcher must be provided.");
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#docFreq(org.apache.lucene.index.Term)
	 */
	public int docFreq(Term term) throws IOException {
		return searcher.docFreq(term);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#docFreqs(org.apache.lucene.index.Term[])
	 */
	public int[] docFreqs(Term[] terms) throws IOException {
		return searcher.docFreqs(terms);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#maxDoc()
	 */
	public int maxDoc() throws IOException {
		return searcher.maxDoc();
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#doc(int)
	 */
	public Document doc(int i) throws CorruptIndexException, IOException {
		return doc(i);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#doc(int, org.apache.lucene.document.FieldSelector)
	 */
	public Document doc(int n, FieldSelector fieldSelector) throws CorruptIndexException, IOException {
		return searcher.doc(n, fieldSelector);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#rewrite(org.apache.lucene.search.Query)
	 */
	public Query rewrite(Query query) throws IOException {
		return searcher.rewrite(query);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#search(org.apache.lucene.search.Query,
	 * org.apache.lucene.search.Filter, int, org.apache.lucene.search.Sort)
	 */
	public TopFieldDocs search(Query query, Filter filter, int n, Sort sort) throws IOException {
		return searcher.search(query, filter, n, sort);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#search(org.apache.lucene.search.Query,
	 * org.apache.lucene.search.Filter, org.apache.lucene.search.Collector)
	 */
	public void search(Query query, Filter filter, Collector results) throws IOException {
		searcher.search(query, filter, results);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquirable#search(org.apache.lucene.search.Query,
	 * org.apache.lucene.search.Filter, int)
	 */
	public TopDocs search(Query query, Filter filter, int n) {
		return search(query, filter, n);
	}

}
