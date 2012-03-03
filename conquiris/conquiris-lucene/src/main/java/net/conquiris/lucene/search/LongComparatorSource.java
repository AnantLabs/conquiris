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

import java.io.IOException;
import java.util.Comparator;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.util.Bits;

/**
 * Long comparator source with an explicit comparator.
 * @author Andres Rodriguez
 */
final class LongComparatorSource extends GenericComparatorSource<Long> {
	/** Serial UID. */
	private static final long serialVersionUID = 6482544719528657501L;

	/**
	 * Constructor.
	 * @param comparator Comparator to use.
	 */
	LongComparatorSource(Comparator<Long> comparator) {
		super(comparator);
	}

	@Override
	public FieldComparator<Long> newComparator(String fieldname, int numHits, int sortPos, boolean reversed)
			throws IOException {
		return new LongComparator(fieldname, reversed, numHits);
	}

	final class LongComparator extends GenericComparator {
		LongComparator(String field, boolean reverse, int numHits) {
			super(Long.class, field, reverse, numHits);
		}

		@Override
		Long[] load(IndexReader reader, int docBase) throws IOException {
			long[] p = FieldCache.DEFAULT.getLongs(reader, getField(), FieldCache.NUMERIC_UTILS_LONG_PARSER, true);
			Bits docsWithField = FieldCache.DEFAULT.getDocsWithField(reader, getField());
			// optimization to remove unneeded checks on the bit interface:
			if (docsWithField instanceof Bits.MatchAllBits) {
				docsWithField = null;
			}
			int n = p.length;
			Long[] values = new Long[n];
			for (int i = 0; i < n; i++) {
				long v = p[i];
				if (docsWithField != null && v == 0 && !docsWithField.get(i)) {
					values[i] = null;
				} else {
					values[i] = v;
				}
			}
			return values;
		}
	}

}
