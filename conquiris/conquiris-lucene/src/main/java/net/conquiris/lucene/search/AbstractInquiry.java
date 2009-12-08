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
import static com.google.common.base.Preconditions.checkState;
import net.conquiris.api.Result;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

/**
 * Abstract query and filter-based inquiry.
 * @author Andres Rodriguez
 * @param <T> Result type.
 */
public abstract class AbstractInquiry<T extends Result> implements Inquiry<T> {
	static final String NO_QUERY = "A Lucene query must be provided";
	/** Lucene query. */
	private final Query query;
	/** Lucene filter. */
	private final Filter filter;

	/**
	 * Constructor.
	 * @param query Lucene query.
	 * @param filter Lucene filter.
	 */
	protected AbstractInquiry(Query query, Filter filter) {
		this.query = checkNotNull(query, NO_QUERY);
		this.filter = filter;
	}

	/**
	 * Returns the query.
	 * @return The query.
	 */
	protected final Query getQuery() {
		return query;
	}

	/**
	 * Returns the filter.
	 * @return The filter.
	 */
	protected final Filter getFilter() {
		return filter;
	}

	public abstract static class AbstractBuilder<T extends Result, I extends AbstractInquiry<T>, B extends AbstractBuilder<T, I, B>> {
		/** Lucene query. */
		private Query query;
		/** Lucene filter. */
		private Filter filter;

		protected AbstractBuilder() {
		}

		/**
		 * Sets the Lucene query
		 * @param query Lucene query.
		 * @return This builder for method chaining.
		 */
		@SuppressWarnings("unchecked")
		public final B setQuery(Query query) {
			this.query = checkNotNull(query, NO_QUERY);
			return (B) this;
		}

		/**
		 * Sets the Lucene filter
		 * @param filter Lucene filter.
		 * @return This builder for method chaining.
		 */
		@SuppressWarnings("unchecked")
		public final B setFilter(Filter filter) {
			this.filter = filter;
			return (B) this;
		}

		/**
		 * Builds the inquiry.
		 * @return The requested inquiry.
		 */
		public final I build() {
			checkState(query != null, NO_QUERY);
			return build(query, filter);
		}

		/**
		 * Builds the inquiry (internal).
		 * @param query Lucene query.
		 * @param filter Lucene filter.
		 * @return The requested inquiry.
		 */
		protected abstract I build(Query query, Filter filter);

	}
}
