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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static net.conquiris.lucene.index.Terms.term;

import java.text.Collator;
import java.util.UUID;

import javax.annotation.Nullable;

import net.conquiris.schema.BooleanSchemaItem;
import net.conquiris.schema.DoubleSchemaItem;
import net.conquiris.schema.FloatSchemaItem;
import net.conquiris.schema.InstantSchemaItem;
import net.conquiris.schema.IntegerSchemaItem;
import net.conquiris.schema.LongSchemaItem;
import net.conquiris.schema.SchemaItem;
import net.conquiris.schema.TextSchemaItem;
import net.conquiris.schema.UUIDSchemaItem;
import net.derquinse.common.base.NotInstantiable;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.joda.time.ReadableInstant;

import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;

/**
 * Query building support class.
 * @author Andres Rodriguez
 */
public final class Queries extends NotInstantiable {
	private Queries() {
	}

	private static Term checkTerm(Term term) {
		return checkNotNull(term, "The term must be provided");
	}

	private static String checkField(String field) {
		return checkNotNull(field, "The field name must be provided");
	}

	private static void checkRange(Range<?> range) {
		checkNotNull(range, "The value range must be provided");
	}

	private static <T extends Comparable<T>> T min(Range<? extends T> range) {
		if (range.hasLowerBound()) {
			return range.lowerEndpoint();
		}
		return null;
	}

	private static <T extends Comparable<T>> T max(Range<? extends T> range) {
		if (range.hasUpperBound()) {
			return range.upperEndpoint();
		}
		return null;
	}

	private static boolean minIncluded(Range<?> range) {
		return range.hasLowerBound() && range.lowerBoundType() == BoundType.CLOSED;
	}

	private static boolean maxIncluded(Range<?> range) {
		return range.hasUpperBound() && range.upperBoundType() == BoundType.CLOSED;
	}

	private static <T> T checkValue(T value) {
		return checkNotNull(value, "The term value must be provided");
	}

	private static String checkItem(SchemaItem field) {
		return checkNotNull(field, "The term field schema item must be provided").getName();
	}

	/** Creates a term query. */
	public static TermQuery termQuery(Term term) {
		return new TermQuery(checkTerm(term));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, String value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, int value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, long value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, float value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, double value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, boolean value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, UUID value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(String field, ReadableInstant value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(TextSchemaItem field, String value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(IntegerSchemaItem field, int value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(LongSchemaItem field, long value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(FloatSchemaItem field, float value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(DoubleSchemaItem field, double value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(BooleanSchemaItem field, boolean value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(UUIDSchemaItem field, UUID value) {
		return termQuery(term(field, value));
	}

	/** Creates a term query. */
	public static TermQuery termQuery(InstantSchemaItem field, ReadableInstant value) {
		return termQuery(term(field, value));
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query termRange(String field, @Nullable String lowerTerm, @Nullable String upperTerm,
			boolean includeLower, boolean includeUpper, Collator collator) {
		checkField(field);
		if (lowerTerm == null && upperTerm == null) {
			return new MatchAllDocsQuery();
		}
		return new TermRangeQuery(field, lowerTerm, upperTerm, includeLower, includeUpper, collator);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query termRange(String field, String lowerTerm, String upperTerm, boolean includeLower,
			boolean includeUpper) {
		return termRange(field, lowerTerm, upperTerm, includeLower, includeUpper, null);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query termRange(TextSchemaItem field, String lowerTerm, String upperTerm, boolean includeLower,
			boolean includeUpper, Collator collator) {
		return termRange(checkItem(field), lowerTerm, upperTerm, includeLower, includeUpper, collator);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query termRange(TextSchemaItem field, String lowerTerm, String upperTerm, boolean includeLower,
			boolean includeUpper) {
		return termRange(checkItem(field), lowerTerm, upperTerm, includeLower, includeUpper);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query termRange(String field, Range<String> range, Collator collator) {
		checkField(field);
		checkRange(range);
		return termRange(field, min(range), max(range), minIncluded(range), maxIncluded(range), collator);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query termRange(String field, Range<String> range) {
		checkField(field);
		checkRange(range);
		return termRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query termRange(TextSchemaItem field, Range<String> range, Collator collator) {
		return termRange(checkItem(field), range, collator);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query termRange(TextSchemaItem field, Range<String> range) {
		return termRange(checkItem(field), range);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query intRange(String field, @Nullable Integer min, @Nullable Integer max, boolean minInclusive,
			boolean maxInclusive) {
		checkField(field);
		if (min == null && max == null) {
			return new MatchAllDocsQuery();
		}
		return NumericRangeQuery.newIntRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query intRange(IntegerSchemaItem field, @Nullable Integer min, @Nullable Integer max,
			boolean minInclusive, boolean maxInclusive) {
		return intRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query intRange(String field, Range<Integer> range) {
		checkField(field);
		checkRange(range);
		return intRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query intRange(IntegerSchemaItem field, Range<Integer> range) {
		return intRange(checkItem(field), range);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query longRange(String field, @Nullable Long min, @Nullable Long max, boolean minInclusive,
			boolean maxInclusive) {
		checkField(field);
		if (min == null && max == null) {
			return new MatchAllDocsQuery();
		}
		return NumericRangeQuery.newLongRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query longRange(LongSchemaItem field, @Nullable Long min, @Nullable Long max, boolean minInclusive,
			boolean maxInclusive) {
		return longRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query longRange(String field, Range<Long> range) {
		checkField(field);
		checkRange(range);
		return longRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query longRange(LongSchemaItem field, Range<Long> range) {
		return longRange(checkItem(field), range);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query floatRange(String field, @Nullable Float min, @Nullable Float max, boolean minInclusive,
			boolean maxInclusive) {
		checkField(field);
		if (min == null && max == null) {
			return new MatchAllDocsQuery();
		}
		return NumericRangeQuery.newFloatRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query floatRange(FloatSchemaItem field, @Nullable Float min, @Nullable Float max, boolean minInclusive,
			boolean maxInclusive) {
		return floatRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query floatRange(String field, Range<Float> range) {
		checkField(field);
		checkRange(range);
		return floatRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query floatRange(FloatSchemaItem field, Range<Float> range) {
		return floatRange(checkItem(field), range);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query doubleRange(String field, @Nullable Double min, @Nullable Double max, boolean minInclusive,
			boolean maxInclusive) {
		checkField(field);
		if (min == null && max == null) {
			return new MatchAllDocsQuery();
		}
		return NumericRangeQuery.newDoubleRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query doubleRange(DoubleSchemaItem field, @Nullable Double min, @Nullable Double max,
			boolean minInclusive, boolean maxInclusive) {
		return doubleRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query doubleRange(String field, Range<Double> range) {
		checkField(field);
		checkRange(range);
		return doubleRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query doubleRange(DoubleSchemaItem field, Range<Double> range) {
		return doubleRange(checkItem(field), range);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query instantRange(String field, @Nullable ReadableInstant min, @Nullable ReadableInstant max,
			boolean minInclusive, boolean maxInclusive) {
		checkField(field);
		if (min == null && max == null) {
			return new MatchAllDocsQuery();
		}
		return NumericRangeQuery.newLongRange(field, min != null ? min.getMillis() : null, max != null ? max.getMillis()
				: null, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If both limits are {@code null} a {@link MatchAllDocsQuery} is
	 * returned.
	 */
	public static Query instantRange(InstantSchemaItem field, @Nullable ReadableInstant min,
			@Nullable ReadableInstant max, boolean minInclusive, boolean maxInclusive) {
		return instantRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query instantRange(String field, Range<? extends ReadableInstant> range) {
		checkField(field);
		checkRange(range);
		return instantRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range query. If the range has no bounds a {@link MatchAllDocsQuery} is returned.
	 */
	public static Query instantRange(InstantSchemaItem field, Range<? extends ReadableInstant> range) {
		return instantRange(checkItem(field), range);
	}

	/**
	 * Adds clauses to a boolean query.
	 * @param query Boolean query to which the clauses are added.
	 * @param occur Specifies how clauses are to occur in matching documents.
	 * @param queries Queries to use to build the clauses.
	 * @return The provided boolean query.
	 * @throws IllegalArgumentException if the queries argument is empty.
	 */
	public static BooleanQuery addClauses(BooleanQuery query, Occur occur, Iterable<? extends Query> queries) {
		checkNotNull(query, "The destination boolean query must be provided");
		checkNotNull(occur, "The occurrence specification must be provided");
		ImmutableList<Query> list = ImmutableList.copyOf(queries);
		checkArgument(!list.isEmpty(), "At least one query must be provided");
		for (Query q : queries) {
			query.add(q, occur);
		}
		return query;
	}

	/**
	 * Adds clauses to a boolean query.
	 * @param query Boolean query to which the clauses are added.
	 * @param occur Specifies how clauses are to occur in matching documents.
	 * @param queries Queries to use to build the clauses.
	 * @return The provided boolean query.
	 * @throws IllegalArgumentException if the queries argument is empty.
	 */
	public static BooleanQuery addClauses(BooleanQuery query, Occur occur, Query... queries) {
		return addClauses(query, occur, asList(queries));
	}
}
