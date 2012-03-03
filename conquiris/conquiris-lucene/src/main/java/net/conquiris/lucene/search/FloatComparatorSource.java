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
 * Int comparator source with an explicit comparator.
 * @author Andres Rodriguez
 */
final class FloatComparatorSource extends GenericComparatorSource<Float> {
	/** Serial UID. */
	private static final long serialVersionUID = 6482544719528657501L;

	/**
	 * Constructor.
	 * @param comparator Comparator to use.
	 */
	FloatComparatorSource(Comparator<Float> comparator) {
		super(comparator);
	}

	@Override
	public FieldComparator<Float> newComparator(String fieldname, int numHits, int sortPos, boolean reversed)
			throws IOException {
		return new FloatComparator(fieldname, reversed, numHits);
	}

	final class FloatComparator extends GenericComparator {
		FloatComparator(String field, boolean reverse, int numHits) {
			super(Float.class, field, reverse, numHits);
		}

		@Override
		Float[] load(IndexReader reader, int docBase) throws IOException {
			float[] p = FieldCache.DEFAULT.getFloats(reader, getField(), FieldCache.NUMERIC_UTILS_FLOAT_PARSER, true);
			Bits docsWithField = FieldCache.DEFAULT.getDocsWithField(reader, getField());
			// optimization to remove unneeded checks on the bit interface:
			if (docsWithField instanceof Bits.MatchAllBits) {
				docsWithField = null;
			}
			int n = p.length;
			Float[] values = new Float[n];
			for (int i = 0; i < n; i++) {
				float v = p[i];
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
