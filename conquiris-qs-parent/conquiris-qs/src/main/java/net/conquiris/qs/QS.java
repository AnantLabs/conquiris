/*
 * Copyright 2008-2011 the original author or authors.
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
package net.conquiris.qs;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Iterables;

/**
 * QS query dictionary. Used for converting QueryTokens to strings and parsing strings to QueryTokens.
 * @author Andres Rodriguez
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class QS {
	private static final QS EMPTY = new QS(ImmutableBiMap.<Class<? extends QueryToken>, String> of());
	private static final QS DEFAULT = new Builder(EMPTY).build();

	/** Query tokens map. */
	private final ImmutableBiMap<Class<? extends QueryToken>, String> queries;

	public static QS get() {
		return DEFAULT;
	}

	/**
	 * Creates a new builder starting with an existing dictionary.
	 * @param qs Dictionary to use.
	 * @return A new builder.
	 */
	public static Builder builder(QS qs) {
		return new Builder(qs);
	}

	/**
	 * Creates a new builder starting with the default dictionary.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder(DEFAULT);
	}

	private QueryToken checkQuery(QueryToken query) {
		return checkNotNull(query, "Null query");
	}

	/**
	 * Constructor.
	 * @param queries Query dictionary.
	 */
	private QS(Map<Class<? extends QueryToken>, String> queries) {
		this.queries = ImmutableBiMap.copyOf(queries);
	}

	/**
	 * Returns the query key.
	 * @param query Query.
	 * @return The requested key.
	 * @throws IllegalArgumentException if the query type is unknown.
	 */
	String getQueryKey(QueryToken query) {
		String key = queries.get(query.getClass());
		checkArgument(key != null, "Unknown query type [%s]", query.getClass().getName());
		return key;
	}

	public void write(QueryToken query, Appendable a) throws IOException {
		doWrite(checkQuery(query), a);
	}

	private void doWrite(QueryToken query, Appendable a) throws IOException {
		query.write(true, this, a);
	}

	public String write(QueryToken query) {
		checkQuery(query);
		StringBuilder b = new StringBuilder();
		try {
			write(query, b);
		} catch (IOException e) {
			// Should not happen
		}
		return b.toString();
	}

	public QueryToken parse(String string) {
		// TODO;
		return MatchAll.get();
	}

	/**
	 * Builder for QS objects.
	 * @author Andres Rodriguez
	 */
	@NotThreadSafe
	public static class Builder {
		private final QS previous;
		private BiMap<Class<? extends QueryToken>, String> current = null;

		private Builder(QS previous) {
			this.previous = checkNotNull(previous);
		}

		private BiMap<Class<? extends QueryToken>, String> checkMap() {
			return (current != null) ? current : previous.queries;
		}

		private BiMap<Class<? extends QueryToken>, String> workMap() {
			return HashBiMap.create(checkMap());
		}

		private void checkEntry(BiMap<Class<? extends QueryToken>, String> map, Class<? extends QueryToken> queryType,
				String key) {
			checkNotNull(queryType);
			checkNotNull(key);
			checkArgument(!map.containsKey(queryType), "Duplicate query type [%s]", queryType.getName());
			checkArgument(!map.containsValue(key), "Duplicate query key [%s]", key);
		}

		private String getQueryKey(Class<? extends QueryToken> queryType) {
			checkNotNull(queryType);
			QueryKey key = queryType.getAnnotation(QueryKey.class);
			checkArgument(key != null, "Query type [%s] has no QueryKey annotation", queryType.getName());
			return key.value();
		}

		/**
		 * Adds a query type to the QS dictionary.
		 * @param queryType Query type.
		 * @param key Key to use.
		 * @return This builder.
		 * @throws IllegalArgumentException if the key is invalid or the key and/or query type are
		 *           already being used.
		 */
		public Builder put(Class<? extends QueryToken> queryType, String key) {
			checkEntry(checkMap(), queryType, key);
			if (current == null) {
				current = HashBiMap.create(previous.queries);
			}
			current.put(queryType, key);
			return this;
		}

		/**
		 * Adds a query type to the QS dictionary. The key is obtained from the QueryKey annotation.
		 * @param queryType Query type.
		 * @return This builder.
		 * @throws IllegalArgumentException if the key is invalid or the key and/or query type are
		 *           already being used.
		 */
		public Builder put(Class<? extends QueryToken> queryType) {
			return put(queryType, getQueryKey(queryType));
		}

		/**
		 * Adds a collection of query types to the QS dictionary. If an exception is thrown no
		 * modification is made to the dictionary.
		 * @param queries Query types to add. If {@code null} or empty no action is performed.
		 * @return This builder.
		 * @throws IllegalArgumentException if any of keys is invalid or any of the keys and/or querys
		 *           type are already being used.
		 */
		public Builder putAll(@Nullable Map<Class<? extends QueryToken>, String> queries) {
			if (queries == null || queries.isEmpty()) {
				return this;
			}
			BiMap<Class<? extends QueryToken>, String> map = workMap();
			for (Entry<Class<? extends QueryToken>, String> e : queries.entrySet()) {
				checkEntry(map, e.getKey(), e.getValue());
				map.put(e.getKey(), e.getValue());
			}
			current = map;
			return this;
		}

		/**
		 * Adds a collection of query types to the QS dictionary. The keys are obtained from the
		 * QueryKey annotation in each class. If an exception is thrown no modification is made to the
		 * dictionary.
		 * @param queries Query types to add. If {@code null} or empty no action is performed.
		 * @return This builder.
		 * @throws IllegalArgumentException if any of keys is invalid or any of the keys and/or querys
		 *           type are already being used.
		 */
		public Builder putAll(@Nullable Iterable<Class<? extends QueryToken>> queries) {
			if (queries == null || Iterables.isEmpty(queries)) {
				return this;
			}
			BiMap<Class<? extends QueryToken>, String> map = workMap();
			for (Class<? extends QueryToken> type : queries) {
				String k = getQueryKey(type);
				checkEntry(map, type, k);
				map.put(type, k);
			}
			current = map;
			return this;
		}

		/**
		 * Builds the query dictionary.
		 * @return The requested dictionay.
		 */
		public QS build() {
			if (current == null) {
				return previous;
			}
			return new QS(current);
		}

	}

}
