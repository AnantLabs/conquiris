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
package net.conquiris.lucene.search;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Comparator;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

/**
 * Comparator with an explicit order provided by a collection of values. Values not in the
 * collection are assigned the last position.
 * @author Andres Rodriguez
 */
final class ExplicitComparator<T> implements Comparator<T> {
	/** Rank map. */
	private final ImmutableMap<T, Integer> rankMap;
	/** Rank for values not in the map. */
	private final int otherRank;

	static <T> ExplicitComparator<T> of(Iterable<? extends T> values) {
		return new ExplicitComparator<T>(values);
	}

		/**
	 * Constructor.
	 * @param values Values in sort order. Duplicates and {@code nulls} are filtered out.
	 */
	private ExplicitComparator(Iterable<? extends T> values) {
		checkNotNull(values);
		ImmutableMap.Builder<T, Integer> builder = ImmutableMap.builder();
		Set<T> visited = Sets.newHashSet();
		int rank = 0;
		for (T value : Iterables.filter(values, Predicates.notNull())) {
			if (visited.add(value)) {
				builder.put(value, rank);
				rank++;
			}
		}
		this.rankMap = builder.build();
		this.otherRank = rank + 1;
	}

	private int rank(@Nullable T value) {
		Integer rank = rankMap.get(value);
		if (rank != null) {
			return rank.intValue();
		}
		return otherRank;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T o1, T o2) {
		return Ints.compare(rank(o1), rank(o2));
	}
}
