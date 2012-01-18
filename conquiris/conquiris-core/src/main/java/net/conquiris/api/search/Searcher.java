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
package net.conquiris.api.search;

import javax.annotation.Nullable;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;

/**
 * Interface for conquiris searchers. Implementations specify the mapping between conquiris and
 * lucene index searchers.
 * @author Andres Rodriguez
 */
public interface Searcher {
	/**
	 * Low level Lucene method.
	 * @see IndexSearcher#search(Query, Filter, Collector)
	 */
	void search(Query query, Filter filter, Collector results);

	/**
	 * Low level Lucene method.
	 * @see IndexSearcher#search(Query, Filter, int)
	 */
	TopDocs search(Query query, Filter filter, int n);

	/**
	 * Low level Lucene method.
	 * @see IndexSearcher#search(Query, Filter, int, Sort)
	 */
	TopFieldDocs search(Query query, Filter filter, int n, Sort sort);

	/**
	 * Low level Lucene method.
	 * @see IndexSearcher#rewrite(Query)
	 */
	Query rewrite(Query query);

	/**
	 * Low level Lucene method.
	 * @see IndexSearcher#doc(int)
	 */
	Document doc(int i);

	/**
	 * Returns the first result of a query
	 * @param mapper Mapper to use.
	 * @param query Query to perform.
	 * @param filter Filter to apply
	 * @param sort Sort order.
	 * @param highlight Highlight configuration.
	 * @return The item result.
	 */
	<T> ItemResult<T> getFirst(HitMapper<T> mapper, Query query, @Nullable Filter filter, @Nullable Sort sort,
			@Nullable Highlight highlight);

	/**
	 * Returns a page of results of a query.
	 * @param mapper Mapper to use.
	 * @param query Query to perform.
	 * @param firstRecord First requedted record.
	 * @param maxRecords Maximum number of records to return.
	 * @param filter Filter to apply
	 * @param sort Sort order.
	 * @param highlight Highlight configuration.
	 * @return The page result.
	 */
	<T> PageResult<T> getPage(HitMapper<T> mapper, Query query, int firstRecord, int maxRecords, @Nullable Filter filter,
			@Nullable Sort sort, @Nullable Highlight highlight);

	/**
	 * Returns the number of results of a query.
	 * @param query Query to perform.
	 * @param filter Filter to apply
	 * @param score Whether the scores must be calculated.
	 * @return The count result.
	 */
	CountResult getCount(Query query, @Nullable Filter filter, boolean score);
}
