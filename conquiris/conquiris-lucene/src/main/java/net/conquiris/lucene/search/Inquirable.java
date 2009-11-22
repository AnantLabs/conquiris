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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;

/**
 * Interface for inquirable objects. Inquirable objects are the bridge to lucene searchers.
 * @author Andres Rodriguez
 */
public interface Inquirable {
	/**
	 * Returns the number of documents containing <code>term</code>.
	 * @see org.apache.lucene.index.IndexReader#docFreq(Term)
	 */
	int docFreq(Term term) throws IOException;

	/**
	 * For each term in the terms array, calculates the number of documents containing
	 * <code>term</code>.
	 * @return An array with these document frequencies.
	 */
	int[] docFreqs(Term[] terms) throws IOException;

	/**
	 * Returns one greater than the largest possible document number.
	 * @see org.apache.lucene.index.IndexReader#maxDoc()
	 */
	int maxDoc() throws IOException;

	/**
	 * Returns the stored fields of document <code>i</code>.
	 * @see org.apache.lucene.index.IndexReader#document(int)
	 * @throws CorruptIndexException if the index is corrupt
	 * @throws IOException if there is a low-level IO error
	 */
	Document doc(int i) throws CorruptIndexException, IOException;

	/**
	 * Get the {@link org.apache.lucene.document.Document} at the <code>n</code><sup>th</sup>
	 * position. The {@link org.apache.lucene.document.FieldSelector} may be used to determine what
	 * {@link org.apache.lucene.document.Field}s to load and how they should be loaded. <b>NOTE:</b>
	 * If the underlying Reader (more specifically, the underlying <code>FieldsReader</code>) is
	 * closed before the lazy {@link org.apache.lucene.document.Field} is loaded an exception may be
	 * thrown. If you want the value of a lazy {@link org.apache.lucene.document.Field} to be
	 * available after closing you must explicitly load it or fetch the Document again with a new
	 * loader.
	 * @param n Get the document at the <code>n</code><sup>th</sup> position
	 * @param fieldSelector The {@link org.apache.lucene.document.FieldSelector} to use to determine
	 *          what Fields should be loaded on the Document. May be null, in which case all Fields
	 *          will be loaded.
	 * @return The stored fields of the {@link org.apache.lucene.document.Document} at the nth
	 *         position
	 * @throws CorruptIndexException if the index is corrupt
	 * @throws IOException if there is a low-level IO error
	 * @see org.apache.lucene.index.IndexReader#document(int, FieldSelector)
	 * @see org.apache.lucene.document.Fieldable
	 * @see org.apache.lucene.document.FieldSelector
	 * @see org.apache.lucene.document.SetBasedFieldSelector
	 * @see org.apache.lucene.document.LoadFirstFieldSelector
	 */
	Document doc(int n, FieldSelector fieldSelector) throws CorruptIndexException, IOException;

	/**
	 * Called to re-write queries into primitive queries.
	 * @throws BooleanQuery.TooManyClauses
	 */
	Query rewrite(Query query) throws IOException;

	/**
	 * Search implementation with arbitrary sorting. Finds the top <code>n</code> hits for
	 * <code>query</code>, applying <code>filter</code> if non-null, and sorting the hits by the
	 * criteria in <code>sort</code>.
	 * <p>
	 * NOTE: this does not compute scores by default; use
	 * {@link IndexSearcher#setDefaultFieldSortScoring} to enable scoring.
	 * @throws BooleanQuery.TooManyClauses
	 */
	TopFieldDocs search(Query query, Filter filter, int n, Sort sort) throws IOException;

	/**
	 * Lower-level search API.
	 * <p>
	 * {@link Collector#collect(int)} is called for every matching document. <br>
	 * Collector-based access to remote indexes is discouraged.
	 * <p>
	 * Applications should only use this if they need <i>all</i> of the matching documents. The
	 * high-level search API ({@link Searcher#search(Query, Filter, int)}) is usually more efficient,
	 * as it skips non-high-scoring hits.
	 * @param query to match documents
	 * @param filter if non-null, used to permit documents to be collected.
	 * @param results to receive hits
	 * @throws BooleanQuery.TooManyClauses
	 */
	void search(Query query, Filter filter, Collector results) throws IOException;

	/**
	 * Finds the top <code>n</code> hits for <code>query</code>, applying <code>filter</code> if
	 * non-null.
	 * @throws BooleanQuery.TooManyClauses
	 */
	TopDocs search(Query query, Filter filter, int n);

}
