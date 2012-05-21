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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.FixedBitSet;

/**
 * A wrapping filter that negates the provided one.
 * @author Andres Rodriguez
 */
final class NegatingFilter extends Filter {
	/** Serial UID. */
	private static final long serialVersionUID = 3218934851787738661L;

	/** Wrapped filter. */
	private final Filter filter;

	/**
	 * Constructor.
	 * @param filter Filter to cache results of.
	 */
	NegatingFilter(Filter filter) {
		this.filter = checkNotNull(filter, "The filter to negate must be provided");
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		final int n = reader.maxDoc();
		final FixedBitSet bits = new FixedBitSet(reader.maxDoc());
		final DocIdSet set = filter.getDocIdSet(reader);
		if (set == null || set == DocIdSet.EMPTY_DOCIDSET) {
			bits.set(0, n);
		} else {
			DocIdSetIterator i = set.iterator();
			if (i == null) {
				bits.set(0, n);
			} else {
				bits.or(i);
				bits.flip(0, n);
			}
		}
		return bits;
	}
}
