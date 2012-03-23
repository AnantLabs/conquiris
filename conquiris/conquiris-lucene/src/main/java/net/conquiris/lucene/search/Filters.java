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

import java.text.Collator;
import java.util.Arrays;

import javax.annotation.Nullable;

import net.conquiris.schema.DoubleSchemaItem;
import net.conquiris.schema.FloatSchemaItem;
import net.conquiris.schema.InstantSchemaItem;
import net.conquiris.schema.IntegerSchemaItem;
import net.conquiris.schema.LongSchemaItem;
import net.conquiris.schema.TextSchemaItem;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TermsFilter;
import org.joda.time.ReadableInstant;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;

/**
 * Filter building support class.
 * @author Andres Rodriguez
 */
public final class Filters extends SearchSupport {
	private Filters() {
	}
	
	/** Returns a filter that negates the provided one. */
	public static Filter negate(Filter filter) {
		return new NegatingFilter(filter);
	}

	/** Constructs a filter for docs matching any of the terms added to it. */
	public static TermsFilter terms(Iterable<Term> terms) {
		checkNotNull(terms);
		TermsFilter f = new TermsFilter();
		for (Term t : terms) {
			f.addTerm(t);
		}
		return f;
	}

	/** Constructs a filter for docs matching any of the terms added to it. */
	public static TermsFilter terms(Term... terms) {
		return terms(Arrays.asList(terms));
	}

	/** Constructs a filter for docs matching any of the terms added to it. */
	public static <T> TermsFilter terms(Function<? super T, Term> termBuilder, Iterable<T> values) {
		return terms(Iterables.transform(values, termBuilder));
	}

	/** Constructs a filter for docs matching any of the terms added to it. */
	public static <T> TermsFilter terms(Function<? super T, Term> termBuilder, T... values) {
		return terms(termBuilder, Arrays.asList(values));
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static TermRangeFilter termRange(String field, @Nullable String lowerTerm, @Nullable String upperTerm,
			boolean includeLower, boolean includeUpper, Collator collator) {
		checkField(field);
		checkArgument(lowerTerm != null || upperTerm != null, "At least one limit must be provided");
		return new TermRangeFilter(field, lowerTerm, upperTerm, includeLower && lowerTerm != null, includeUpper
				&& upperTerm != null, collator);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static TermRangeFilter termRange(String field, @Nullable String lowerTerm, @Nullable String upperTerm,
			boolean includeLower, boolean includeUpper) {
		return termRange(field, lowerTerm, upperTerm, includeLower, includeUpper, null);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static TermRangeFilter termRange(TextSchemaItem field, @Nullable String lowerTerm, @Nullable String upperTerm,
			boolean includeLower, boolean includeUpper, Collator collator) {
		return termRange(checkItem(field), lowerTerm, upperTerm, includeLower, includeUpper, collator);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static TermRangeFilter termRange(TextSchemaItem field, @Nullable String lowerTerm, @Nullable String upperTerm,
			boolean includeLower, boolean includeUpper) {
		return termRange(checkItem(field), lowerTerm, upperTerm, includeLower, includeUpper);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static TermRangeFilter termRange(String field, Range<String> range, Collator collator) {
		checkField(field);
		checkRange(range);
		return termRange(field, min(range), max(range), minIncluded(range), maxIncluded(range), collator);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static TermRangeFilter termRange(String field, Range<String> range) {
		checkField(field);
		checkRange(range);
		return termRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static TermRangeFilter termRange(TextSchemaItem field, Range<String> range, Collator collator) {
		return termRange(checkItem(field), range, collator);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static TermRangeFilter termRange(TextSchemaItem field, Range<String> range) {
		return termRange(checkItem(field), range);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Integer> intRange(String field, @Nullable Integer min, @Nullable Integer max,
			boolean minInclusive, boolean maxInclusive) {
		checkField(field);
		checkArgument(min != null || max != null, "At least one limit must be provided");
		return NumericRangeFilter.newIntRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Integer> intRange(IntegerSchemaItem field, @Nullable Integer min,
			@Nullable Integer max, boolean minInclusive, boolean maxInclusive) {
		return intRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Integer> intRange(String field, Range<Integer> range) {
		checkField(field);
		checkRange(range);
		return intRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Integer> intRange(IntegerSchemaItem field, Range<Integer> range) {
		return intRange(checkItem(field), range);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Long> longRange(String field, @Nullable Long min, @Nullable Long max,
			boolean minInclusive, boolean maxInclusive) {
		checkField(field);
		checkArgument(min != null || max != null, "At least one limit must be provided");
		return NumericRangeFilter.newLongRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Long> longRange(LongSchemaItem field, @Nullable Long min, @Nullable Long max,
			boolean minInclusive, boolean maxInclusive) {
		return longRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Long> longRange(String field, Range<Long> range) {
		checkField(field);
		checkRange(range);
		return longRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Long> longRange(LongSchemaItem field, Range<Long> range) {
		return longRange(checkItem(field), range);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Float> floatRange(String field, @Nullable Float min, @Nullable Float max,
			boolean minInclusive, boolean maxInclusive) {
		checkField(field);
		checkArgument(min != null || max != null, "At least one limit must be provided");
		return NumericRangeFilter.newFloatRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Float> floatRange(FloatSchemaItem field, @Nullable Float min, @Nullable Float max,
			boolean minInclusive, boolean maxInclusive) {
		return floatRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Float> floatRange(String field, Range<Float> range) {
		checkField(field);
		checkRange(range);
		return floatRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Float> floatRange(FloatSchemaItem field, Range<Float> range) {
		return floatRange(checkItem(field), range);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Double> doubleRange(String field, @Nullable Double min, @Nullable Double max,
			boolean minInclusive, boolean maxInclusive) {
		checkField(field);
		checkArgument(min != null || max != null, "At least one limit must be provided");
		return NumericRangeFilter.newDoubleRange(field, min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Double> doubleRange(DoubleSchemaItem field, @Nullable Double min,
			@Nullable Double max, boolean minInclusive, boolean maxInclusive) {
		return doubleRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Double> doubleRange(String field, Range<Double> range) {
		checkField(field);
		checkRange(range);
		return doubleRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Double> doubleRange(DoubleSchemaItem field, Range<Double> range) {
		return doubleRange(checkItem(field), range);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Long> instantRange(String field, @Nullable ReadableInstant min,
			@Nullable ReadableInstant max, boolean minInclusive, boolean maxInclusive) {
		checkField(field);
		checkArgument(min != null || max != null, "At least one limit must be provided");
		return NumericRangeFilter.newLongRange(field, min != null ? min.getMillis() : null, max != null ? max.getMillis()
				: null, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if both limits are null.
	 */
	public static NumericRangeFilter<Long> instantRange(InstantSchemaItem field, @Nullable ReadableInstant min,
			@Nullable ReadableInstant max, boolean minInclusive, boolean maxInclusive) {
		return instantRange(checkItem(field), min, max, minInclusive, maxInclusive);
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Long> instantRange(String field, Range<? extends ReadableInstant> range) {
		checkField(field);
		checkRange(range);
		return instantRange(field, min(range), max(range), minIncluded(range), maxIncluded(range));
	}

	/**
	 * Creates a new range filter.
	 * @throws IllegalArgumentException if the range has no bounds.
	 */
	public static NumericRangeFilter<Long> instantRange(InstantSchemaItem field, Range<? extends ReadableInstant> range) {
		return instantRange(checkItem(field), range);
	}

	/**
	 * Adds clauses to a boolean filter.
	 * @param query Boolean filter to which the clauses are added.
	 * @param occur Specifies how clauses are to occur in matching documents.
	 * @param filters Filters to use to build the clauses.
	 * @return The provided boolean filter.
	 * @throws IllegalArgumentException if the filters argument is empty.
	 */
	public static BooleanFilter addClauses(BooleanFilter filter, Occur occur, Iterable<? extends Filter> filters) {
		checkNotNull(filter, "The destination boolean filter must be provided");
		checkNotNull(occur, "The occurrence specification must be provided");
		ImmutableList<Filter> list = ImmutableList.copyOf(filters);
		checkArgument(!list.isEmpty(), "At least one filter must be provided");
		for (Filter f : filters) {
			filter.add(f, occur);
		}
		return filter;
	}

	/**
	 * Adds clauses to a boolean filter.
	 * @param query Boolean filter to which the clauses are added.
	 * @param occur Specifies how clauses are to occur in matching documents.
	 * @param filters Filters to use to build the clauses.
	 * @return The provided boolean filter.
	 * @throws IllegalArgumentException if the filters argument is empty.
	 */
	public static BooleanFilter addClauses(BooleanFilter filter, Occur occur, Filter... filters) {
		return addClauses(filter, occur, asList(filters));
	}

	/**
	 * Creates a boolean filter.
	 * @param occur Specifies how clauses are to occur in matching documents.
	 * @param filters Filters to use to build the clauses.
	 * @return The boolean filter.
	 * @throws IllegalArgumentException if the filters argument is empty.
	 */
	public static BooleanFilter booleanFilter(Occur occur, Iterable<? extends Filter> filters) {
		return addClauses(new BooleanFilter(), occur, filters);
	}

	/**
	 * Creates a boolean filter.
	 * @param occur Specifies how clauses are to occur in matching documents.
	 * @param filters Filters to use to build the clauses.
	 * @return The boolean filter.
	 * @throws IllegalArgumentException if the filters argument is empty.
	 */
	public static BooleanFilter booleanFilter(Occur occur, Filter... filters) {
		return addClauses(new BooleanFilter(), occur, filters);
	}

}
