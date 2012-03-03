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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Comparator;

import javax.annotation.Nullable;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;

/**
 * Source for field comparators based on Java comparators. The comparators must support
 * {@code null} values.
 * @author Andres Rodriguez
 */
abstract class GenericComparatorSource<T> extends FieldComparatorSource {
	/** Serial UID. */
	private static final long serialVersionUID = -5332664958641451859L;
	/** Comparator. */
	private final Comparator<T> comparator;

	/**
	 * Constructor.
	 * @param comparator Comparator to use.
	 */
	GenericComparatorSource(Comparator<T> comparator) {
		this.comparator = checkNotNull(comparator, "The comparator to use must be provided");
	}

	abstract class GenericComparator extends FieldComparator<T> {
		/** Field name. */
		private final String field;
		/** Whether the comparator is reversed. */
		private final boolean reverse;
		/** Values. */
		private final T[] values;
		private T[] currentReaderValues;
		private T bottom;

		GenericComparator(Class<T> type, String field, boolean reverse, int numHits) {
			this.field = checkNotNull(field);
			this.reverse = reverse;
			@SuppressWarnings("unchecked")
			T[] array = (T[]) Array.newInstance(type, numHits);
			this.values = array;
		}

		final String getField() {
			return field;
		}

		@Override
		public final int compareValues(@Nullable T o1, @Nullable T o2) {
			int c = GenericComparatorSource.this.comparator.compare(o1, o2);
			if (reverse) {
				return -c;
			}
			return c;
		}

		@Override
		public final void setNextReader(IndexReader reader, int docBase) throws IOException {
			currentReaderValues = load(reader, docBase);
		}

		abstract T[] load(IndexReader reader, int docBase) throws IOException;

		@Override
		public final int compare(int slot1, int slot2) {
			return compareValues(values[slot1], values[slot2]);
		}

		@Override
		public final int compareBottom(int doc) {
			return compareValues(bottom, currentReaderValues[doc]);
		}

		@Override
		public final void copy(int slot, int doc) {
			values[slot] = currentReaderValues[doc];
		}

		@Override
		public final void setBottom(final int bottom) {
			this.bottom = values[bottom];
		}

		@Override
		public T value(int slot) {
			return values[slot];
		}

	}

}
