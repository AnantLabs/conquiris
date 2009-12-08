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
import java.util.Set;

import net.conquiris.api.GroupResult;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

import com.google.common.collect.ImmutableList;

/**
 * Basic counting inquiry.
 * @author Andres Rodriguez
 * @param <T> Payload type.
 */
public final class GroupingInquiry<T> extends AbstractInquiry<GroupResult<T>> {
	/**
	 * Constructor.
	 * @param query Lucene query.
	 * @param filter Lucene filter.
	 * @param builder Inquiry builder.
	 */
	private GroupingInquiry(Query query, Filter filter, Builder<T> builder) {
		super(query, filter);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.search.Inquiry#perform(net.conquiris.lucene.search.Inquirable)
	 */
	@Override
	public ResultBuilder<GroupResult<T>> perform(Inquirable inquirable) throws IOException {
		final CountingCollector collector = new CountingCollector();
		// Perform the query
		inquirable.search(getQuery(), getFilter(), collector);
		// Return the builder
		return new ResultBuilder<GroupResult<T>>() {
			@Override
			public GroupResult<T> build(long time) {
				return null; // TODO
			}
		};
	}

	public static final class Builder<T> extends AbstractBuilder<GroupResult<T>, GroupingInquiry<T>, Builder<T>> {
		/** Payload calculator, {@code null} for counting queries. */
		private final Group<T> payload;
		/** Fields to group by. */
		private final ImmutableList.Builder<String> groupBy = ImmutableList.builder();
		/** Mapped fields. */
		private Set<String> fields;

		private Builder(Group<T> payload) {
			this.payload = payload;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * net.conquiris.lucene.search.AbstractInquiry.AbstractBuilder#build(org.apache.lucene.search
		 * .Query, org.apache.lucene.search.Filter)
		 */
		@Override
		protected GroupingInquiry<T> build(Query query, Filter filter) {
			return new GroupingInquiry<T>(query, filter, this);
		}

	}

}
