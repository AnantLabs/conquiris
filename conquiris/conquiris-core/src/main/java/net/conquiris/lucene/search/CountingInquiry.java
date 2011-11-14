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

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

/**
 * Basic counting inquiry.
 * @author Andres Rodriguez
 */
public final class CountingInquiry extends AbstractInquiry<CountingResult> {
	/**
	 * Factory method.
	 * @param query Lucene query.
	 */
	public static CountingInquiry of(Query query) {
		return new CountingInquiry(query, null);
	}

	/**
	 * Factory method.
	 * @param query Lucene query.
	 * @param filter Lucene filter.
	 */
	public static CountingInquiry of(Query query, Filter filter) {
		return new CountingInquiry(query, filter);
	}
	
	/**
	 * Builder factory.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Constructor.
	 * @param query Lucene query.
	 * @param filter Lucene filter.
	 */
	private CountingInquiry(Query query, Filter filter) {
		super(query, filter);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquiry#perform(net.conquiris.lucene.search.Inquirable)
	 */
	@Override
	public ResultBuilder<CountingResult> perform(Inquirable inquirable) throws IOException {
		final CountingCollector collector = new CountingCollector();
		// Perform the query
		inquirable.search(getQuery(), getFilter(), collector);
		// Return the builder
		return new ResultBuilder<CountingResult>() {
			@Override
			public CountingResult build(long time) {
				return new CountingResult(time, collector.getTotalHits());
			}
		};
	}

	public static final class Builder extends AbstractBuilder<CountingResult, CountingInquiry, Builder> {
		Builder() {
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * net.conquiris.lucene.search.AbstractInquiry.AbstractBuilder#build(org.apache.lucene.search
		 * .Query, org.apache.lucene.search.Filter)
		 */
		@Override
		protected CountingInquiry build(Query query, Filter filter) {
			return new CountingInquiry(query, filter);
		}

	}

}
