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

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.conquiris.schema.AbstractWithFieldNameProperty;
import net.conquiris.schema.BinarySchemaItem;
import net.conquiris.schema.DoubleSchemaItem;
import net.conquiris.schema.FloatSchemaItem;
import net.conquiris.schema.IntegerSchemaItem;
import net.conquiris.schema.LongSchemaItem;
import net.conquiris.schema.SchemaItem;
import net.conquiris.schema.TextSchemaItem;
import net.conquiris.schema.UUIDSchemaItem;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;

/**
 * Object representing a search hit. Public methods never return {@code null}.
 * @author Andres Rodriguez
 */
public final class Hit implements Supplier<Document> {
	/** Doc Id. */
	private final int docId;
	/** Hit score. */
	private final float score;
	/** Document. */
	private final Document document;
	/** Highlight fragments. */
	private final ImmutableMultimap<String, String> fragments;
	/** Non-binary fields. */
	private final ImmutableListMultimap<String, Fieldable> fields;
	/** Numeric fields. */
	private final ImmutableListMultimap<String, Number> numeric;
	/** Binary fields. */
	private final ImmutableListMultimap<String, Fieldable> binary;

	/**
	 * Creates a new hit.
	 * @param docId Document id.
	 * @param score Score.
	 * @param document Document. Use only documents loaded with an IndexReader or IndexSearcher.
	 * @param fragments Highlight fragments.
	 * @return
	 */
	public static Hit of(int docId, float score, Document document, @Nullable Multimap<String, String> fragments) {
		return new Hit(docId, score, document, fragments);
	}

	/**
	 * Constructor.
	 */
	private Hit(int docId, float score, Document document, @Nullable Multimap<String, String> fragments) {
		this.docId = docId;
		this.score = score;
		this.document = checkNotNull(document, "The document must be provided");
		if (fragments == null) {
			this.fragments = ImmutableMultimap.of();
		} else {
			this.fragments = ImmutableMultimap.copyOf(fragments);
		}
		ImmutableListMultimap.Builder<String, Fieldable> fieldsBuilder = ImmutableListMultimap.builder();
		ImmutableListMultimap.Builder<String, Number> numericBuilder = ImmutableListMultimap.builder();
		ImmutableListMultimap.Builder<String, Fieldable> binaryBuilder = ImmutableListMultimap.builder();
		for (Fieldable f : document.getFields()) {
			final String name = f.name();
			if (f.isBinary()) {
				binaryBuilder.put(name, f);
			} else {
				fieldsBuilder.put(name, f);
				if (f instanceof NumericField) {
					numericBuilder.put(name, ((NumericField) f).getNumericValue());
				}
			}
		}
		this.fields = fieldsBuilder.build();
		this.numeric = numericBuilder.build();
		this.binary = binaryBuilder.build();
	}

	/** Returns the document id. */
	public int getDocId() {
		return docId;
	}

	/** Returns the score. */
	public float getScore() {
		return score;
	}

	/** Returns the document. If modified results are undefined. */
	@Override
	public Document get() {
		return document;
	}

	/** Returns the highlight fragments. */
	public Multimap<String, String> getFragments() {
		return fragments;
	}

	/* Field value getters. */

	private static String checkName(SchemaItem item) {
		return checkNotNull(item, "The schema item must be provided").getName();
	}

	/** Returns the integer values of the numeric fields with the given name. */
	public FieldValues<Integer> integerValues(String name) {
		return new IntegerValues(name);
	}

	/** Returns the long values of the numeric fields with the given name. */
	public FieldValues<Long> longValues(String name) {
		return new LongValues(name);
	}

	/** Returns the float values of the numeric fields with the given name. */
	public FieldValues<Float> floatValues(String name) {
		return new FloatValues(name);
	}

	/** Returns the double values of the numeric fields with the given name. */
	public FieldValues<Double> doubleValues(String name) {
		return new DoubleValues(name);
	}

	/** Returns the values of the binary fields with the given name. */
	public FieldValues<InputSupplier<ByteArrayInputStream>> binary(String name) {
		return new BinaryValues(name);
	}

	/** Returns the values of the UUID fields with the given name. */
	public FieldValues<UUID> uuid(String name) {
		return new UUIDValues(name);
	}

	/**
	 * Returns the string values of the fields with the given name. Includes all non-binary fields
	 * with the given name.
	 */
	public FieldValues<String> strings(String name) {
		return new StringValues(name);
	}

	/**
	 * Returns the values of the fields corresponding to the given schema item. Includes all
	 * non-binary fields with the given name.
	 */
	public FieldValues<String> item(TextSchemaItem item) {
		return strings(checkName(item));
	}

	/** Returns the values of the fields corresponding to the given schema item. */
	public FieldValues<Integer> item(IntegerSchemaItem item) {
		return integerValues(checkName(item));
	}

	/** Returns the values of the fields corresponding to the given schema item. */
	public FieldValues<Long> item(LongSchemaItem item) {
		return longValues(checkName(item));
	}

	/** Returns the values of the fields corresponding to the given schema item. */
	public FieldValues<Float> item(FloatSchemaItem item) {
		return floatValues(checkName(item));
	}

	/** Returns the values of the fields corresponding to the given schema item. */
	public FieldValues<Double> item(DoubleSchemaItem item) {
		return doubleValues(checkName(item));
	}

	/** Returns the values of the fields corresponding to the given schema item. */
	public FieldValues<InputSupplier<ByteArrayInputStream>> item(BinarySchemaItem item) {
		return binary(checkName(item));
	}

	/** Returns the values of the fields corresponding to the given schema item. */
	public FieldValues<UUID> item(UUIDSchemaItem item) {
		return uuid(checkName(item));
	}

	/** Field values implementation. */
	private abstract class AbstractFieldValues<F, T> extends AbstractWithFieldNameProperty implements FieldValues<T>,
			Function<F, T> {
		private List<T> all = null;

		AbstractFieldValues(String name) {
			super(name);
		}

		@Override
		public final List<T> getAll() {
			if (all == null) {
				all = ImmutableList.copyOf(Lists.transform(map().get(getName()), this));
			}
			return all;
		}

		@Override
		public final Optional<T> getOptional() {
			List<T> list = getAll();
			if (list.isEmpty()) {
				return Optional.absent();
			} else {
				return Optional.of(list.get(0));
			}
		}

		@Override
		public final T get() {
			List<T> list = getAll();
			if (list.isEmpty()) {
				throw new IllegalArgumentException(String.format("No value for field [%s]", getName()));
			} else {
				return list.get(0);
			}
		}

		/** Returns the backing map. */
		abstract ImmutableListMultimap<String, F> map();
	}

	/** String-based values. */
	private abstract class StringBasedValues<T> extends AbstractFieldValues<Fieldable, T> {
		StringBasedValues(String name) {
			super(name);
		}

		@Override
		final ImmutableListMultimap<String, Fieldable> map() {
			return fields;
		}
	}

	/** String values. */
	private final class StringValues extends StringBasedValues<String> {
		StringValues(String name) {
			super(name);
		}

		@Override
		public String apply(Fieldable input) {
			return input.stringValue();
		}
	}

	/** Numeric values. */
	private abstract class NumericValues<T extends Number> extends AbstractFieldValues<Number, T> {
		NumericValues(String name) {
			super(name);
		}

		@Override
		final ImmutableListMultimap<String, Number> map() {
			return numeric;
		}
	}

	/** Integer values. */
	private final class IntegerValues extends NumericValues<Integer> {
		IntegerValues(String name) {
			super(name);
		}

		@Override
		public Integer apply(Number input) {
			if (input instanceof Integer) {
				return (Integer) input;
			}
			return input.intValue();
		}
	}

	/** Long values. */
	private final class LongValues extends NumericValues<Long> {
		LongValues(String name) {
			super(name);
		}

		@Override
		public Long apply(Number input) {
			if (input instanceof Long) {
				return (Long) input;
			}
			return input.longValue();
		}
	}

	/** Float values. */
	private final class FloatValues extends NumericValues<Float> {
		FloatValues(String name) {
			super(name);
		}

		@Override
		public Float apply(Number input) {
			if (input instanceof Float) {
				return (Float) input;
			}
			return input.floatValue();
		}
	}

	/** Double values. */
	private final class DoubleValues extends NumericValues<Double> {
		DoubleValues(String name) {
			super(name);
		}

		@Override
		public Double apply(Number input) {
			if (input instanceof Float) {
				return (Double) input;
			}
			return input.doubleValue();
		}
	}

	/** Binary values. */
	private final class BinaryValues extends AbstractFieldValues<Fieldable, InputSupplier<ByteArrayInputStream>> {
		BinaryValues(String name) {
			super(name);
		}

		@Override
		final ImmutableListMultimap<String, Fieldable> map() {
			return binary;
		}

		@Override
		public InputSupplier<ByteArrayInputStream> apply(Fieldable input) {
			checkArgument(input.isBinary());
			return ByteStreams.newInputStreamSupplier(input.getBinaryValue(), input.getBinaryOffset(),
					input.getBinaryLength());
		}
	}

	/** UUID value function. */
	private final class UUIDValues extends StringBasedValues<UUID> {
		UUIDValues(String name) {
			super(name);
		}

		@Override
		public UUID apply(Fieldable input) {
			return UUID.fromString(input.stringValue());
		}
	}

}
