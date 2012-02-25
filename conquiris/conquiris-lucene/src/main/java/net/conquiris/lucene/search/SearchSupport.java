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
import net.conquiris.schema.SchemaItem;
import net.derquinse.common.base.NotInstantiable;

import org.apache.lucene.index.Term;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

/**
 * Base class for search elements support classes.
 * @author Andres Rodriguez
 */
abstract class SearchSupport extends NotInstantiable {
	SearchSupport() {
	}

	static Term checkTerm(Term term) {
		return checkNotNull(term, "The term must be provided");
	}

	static String checkField(String field) {
		return checkNotNull(field, "The field name must be provided");
	}

	static void checkRange(Range<?> range) {
		checkNotNull(range, "The value range must be provided");
	}

	static <T extends Comparable<T>> T min(Range<? extends T> range) {
		if (range.hasLowerBound()) {
			return range.lowerEndpoint();
		}
		return null;
	}

	static <T extends Comparable<T>> T max(Range<? extends T> range) {
		if (range.hasUpperBound()) {
			return range.upperEndpoint();
		}
		return null;
	}

	static boolean minIncluded(Range<?> range) {
		return range.hasLowerBound() && range.lowerBoundType() == BoundType.CLOSED;
	}

	static boolean maxIncluded(Range<?> range) {
		return range.hasUpperBound() && range.upperBoundType() == BoundType.CLOSED;
	}

	static String checkItem(SchemaItem field) {
		return checkNotNull(field, "The term field schema item must be provided").getName();
	}

}
