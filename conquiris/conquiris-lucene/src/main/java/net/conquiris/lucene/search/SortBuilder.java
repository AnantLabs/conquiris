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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.conquiris.schema.BooleanSchemaItem;
import net.conquiris.schema.DoubleSchemaItem;
import net.conquiris.schema.FloatSchemaItem;
import net.conquiris.schema.InstantSchemaItem;
import net.conquiris.schema.IntegerSchemaItem;
import net.conquiris.schema.LongSchemaItem;
import net.conquiris.schema.SchemaItem;
import net.conquiris.schema.TextSchemaItem;
import net.derquinse.common.base.Builder;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * Sort building class. The comparators rovided as arguments MUST support null values.
 * @author Andres Rodriguez
 */
public final class SortBuilder implements Builder<Sort> {
	/** Sort fields. */
	private final List<SortField> fields = Lists.newLinkedList();

	/** Constructor. */
	private SortBuilder() {
	}

	private static String checkSortable(SchemaItem item) {
		String name = SearchSupport.checkItem(item);
		checkArgument(item.isIndexed(), "The provided field [%s] is not indexed", name);
		return name;
	}

	/** Creates a new builder. */
	public static SortBuilder newBuilder() {
		return new SortBuilder();
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(TextSchemaItem field, boolean reverse) {
		return new SortField(checkSortable(field), SortField.STRING, reverse);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(TextSchemaItem field) {
		return field(field, false);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(IntegerSchemaItem field, boolean reverse) {
		return new SortField(checkSortable(field), SortField.INT, reverse);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(IntegerSchemaItem field) {
		return field(field, false);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(LongSchemaItem field, boolean reverse) {
		return new SortField(checkSortable(field), SortField.LONG, reverse);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(LongSchemaItem field) {
		return field(field, false);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(FloatSchemaItem field, boolean reverse) {
		return new SortField(checkSortable(field), SortField.FLOAT, reverse);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(FloatSchemaItem field) {
		return field(field, false);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(DoubleSchemaItem field, boolean reverse) {
		return new SortField(checkSortable(field), SortField.DOUBLE, reverse);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(DoubleSchemaItem field) {
		return field(field, false);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(BooleanSchemaItem field, boolean reverse) {
		return new SortField(checkSortable(field), SortField.INT, reverse);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(BooleanSchemaItem field) {
		return field(field, false);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(InstantSchemaItem field, boolean reverse) {
		return new SortField(checkSortable(field), SortField.LONG, reverse);
	}

	/**
	 * Creates a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(InstantSchemaItem field) {
		return field(field, false);
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(IntegerSchemaItem field, Comparator<Integer> comparator, boolean reverse) {
		return new SortField(checkSortable(field), new IntComparatorSource(comparator), reverse);
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(IntegerSchemaItem field, Comparator<Integer> comparator) {
		return field(field, comparator, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(IntegerSchemaItem field, Iterable<Integer> values, boolean reverse) {
		return field(field, ExplicitComparator.of(values), false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(IntegerSchemaItem field, Iterable<Integer> values) {
		return field(field, values, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(IntegerSchemaItem field, int... values) {
		return field(field, Ints.asList(values));
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(LongSchemaItem field, Comparator<Long> comparator, boolean reverse) {
		return new SortField(checkSortable(field), new LongComparatorSource(comparator), reverse);
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(LongSchemaItem field, Comparator<Long> comparator) {
		return field(field, comparator, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(LongSchemaItem field, Iterable<Long> values, boolean reverse) {
		return field(field, ExplicitComparator.of(values), false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(LongSchemaItem field, Iterable<Long> values) {
		return field(field, values, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(LongSchemaItem field, long... values) {
		return field(field, Longs.asList(values));
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(FloatSchemaItem field, Comparator<Float> comparator, boolean reverse) {
		return new SortField(checkSortable(field), new FloatComparatorSource(comparator), reverse);
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(FloatSchemaItem field, Comparator<Float> comparator) {
		return field(field, comparator, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(FloatSchemaItem field, Iterable<Float> values, boolean reverse) {
		return field(field, ExplicitComparator.of(values), false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(FloatSchemaItem field, Iterable<Float> values) {
		return field(field, values, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(FloatSchemaItem field, float... values) {
		return field(field, Floats.asList(values));
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(DoubleSchemaItem field, Comparator<Double> comparator, boolean reverse) {
		return new SortField(checkSortable(field), new DoubleComparatorSource(comparator), reverse);
	}

	/**
	 * Creates a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(DoubleSchemaItem field, Comparator<Double> comparator) {
		return field(field, comparator, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(DoubleSchemaItem field, Iterable<Double> values, boolean reverse) {
		return field(field, ExplicitComparator.of(values), false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(DoubleSchemaItem field, Iterable<Double> values) {
		return field(field, values, false);
	}

	/**
	 * Creates a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public static SortField field(DoubleSchemaItem field, double... values) {
		return field(field, Doubles.asList(values));
	}

	/** Creates the sort specification. */
	@Override
	public Sort build() {
		if (fields.isEmpty()) {
			return new Sort();
		} else {
			SortField[] array = new SortField[fields.size()];
			fields.toArray(array);
			return new Sort(array);
		}
	}

	/**
	 * Adds a sort field.
	 * @return This builder.
	 */
	public SortBuilder add(SortField field) {
		fields.add(Preconditions.checkNotNull(field));
		return this;
	}

	/**
	 * Adds some sort fields.
	 * @return This builder.
	 */
	public SortBuilder add(Iterable<? extends SortField> fields) {
		for (SortField field : fields) {
			add(field);
		}
		return this;
	}

	/**
	 * Adds some sort fields.
	 * @return This builder.
	 */
	public SortBuilder add(SortField... fields) {
		return add(Arrays.asList(fields));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(TextSchemaItem field, boolean reverse) {
		return add(field(field, reverse));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(TextSchemaItem field) {
		return add(field, false);
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(IntegerSchemaItem field, boolean reverse) {
		return add(field(field, reverse));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(IntegerSchemaItem field) {
		return add(field, false);
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(LongSchemaItem field, boolean reverse) {
		return add(field(field, reverse));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(LongSchemaItem field) {
		return add(field, false);
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(FloatSchemaItem field, boolean reverse) {
		return add(field(field, reverse));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(FloatSchemaItem field) {
		return add(field, false);
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(DoubleSchemaItem field, boolean reverse) {
		return add(field(field, reverse));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(DoubleSchemaItem field) {
		return add(field, false);
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(BooleanSchemaItem field, boolean reverse) {
		return add(field(field, reverse));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(BooleanSchemaItem field) {
		return add(field, false);
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(InstantSchemaItem field, boolean reverse) {
		return add(field(field, reverse));
	}

	/**
	 * Adds a sort by terms in the field specified by the provided schema item.
	 * @param field Field identified by an schema item.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 * @return This builder.
	 */
	public SortBuilder add(InstantSchemaItem field) {
		return add(field, false);
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(IntegerSchemaItem field, Comparator<Integer> comparator, boolean reverse) {
		return add(field(field, comparator, reverse));
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(IntegerSchemaItem field, Comparator<Integer> comparator) {
		return add(field(field, comparator));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(IntegerSchemaItem field, Iterable<Integer> values, boolean reverse) {
		return add(field(field, values, reverse));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(IntegerSchemaItem field, Iterable<Integer> values) {
		return add(field(field, values));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(IntegerSchemaItem field, int... values) {
		return add(field(field, values));
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(LongSchemaItem field, Comparator<Long> comparator, boolean reverse) {
		return add(field(field, comparator, reverse));
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(LongSchemaItem field, Comparator<Long> comparator) {
		return add(field(field, comparator));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(LongSchemaItem field, Iterable<Long> values, boolean reverse) {
		return add(field(field, values, reverse));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(LongSchemaItem field, Iterable<Long> values) {
		return add(field(field, values));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(LongSchemaItem field, long... values) {
		return add(field(field, values));
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(FloatSchemaItem field, Comparator<Float> comparator, boolean reverse) {
		return add(field(field, comparator, reverse));
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(FloatSchemaItem field, Comparator<Float> comparator) {
		return add(field(field, comparator));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(FloatSchemaItem field, Iterable<Float> values, boolean reverse) {
		return add(field(field, values, reverse));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(FloatSchemaItem field, Iterable<Float> values) {
		return add(field(field, values));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(FloatSchemaItem field, float... values) {
		return add(field(field, values));
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(DoubleSchemaItem field, Comparator<Double> comparator, boolean reverse) {
		return add(field(field, comparator, reverse));
	}

	/**
	 * Adds a sort field specified by the provided comparator.
	 * @param field Field identified by an schema item.
	 * @param comparator Comparator to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(DoubleSchemaItem field, Comparator<Double> comparator) {
		return add(field(field, comparator));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @param reverse True if natural order should be reversed.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(DoubleSchemaItem field, Iterable<Double> values, boolean reverse) {
		return add(field(field, values, reverse));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(DoubleSchemaItem field, Iterable<Double> values) {
		return add(field(field, values));
	}

	/**
	 * Adds a sort field specified by the provided value list.
	 * @param field Field identified by an schema item.
	 * @param values Value list to use.
	 * @throws IllegalArgumentException if the provided schema item is not indexed.
	 */
	public SortBuilder add(DoubleSchemaItem field, double... values) {
		return add(field(field, values));
	}

}
