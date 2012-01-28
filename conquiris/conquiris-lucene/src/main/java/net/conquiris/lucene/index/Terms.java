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
package net.conquiris.lucene.index;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

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
import org.apache.lucene.util.NumericUtils;
import org.joda.time.ReadableInstant;

import com.google.common.base.Function;

/**
 * Term building support class.
 * @author Andres Rodriguez
 */
public final class Terms extends NotInstantiable {
	private Terms() {
	}

	private static String checkField(String field) {
		return checkNotNull(field, "The term field name must be provided");
	}

	private static <T> T checkValue(T value) {
		return checkNotNull(value, "The term value must be provided");
	}

	private static String checkItem(SchemaItem field) {
		return checkNotNull(field, "The term field schema item must be provided").getName();
	}

	/** Creates a term. */
	public static Term term(String field, String value) {
		return new Term(checkField(field), checkValue(value));
	}

	/** Creates a term. */
	public static Term term(String field, int value) {
		return term(field, NumericUtils.intToPrefixCoded(value));
	}

	/** Creates a term. */
	public static Term term(String field, long value) {
		return term(field, NumericUtils.longToPrefixCoded(value));
	}

	/** Creates a term. */
	public static Term term(String field, float value) {
		return term(field, NumericUtils.floatToPrefixCoded(value));
	}

	/** Creates a term. */
	public static Term term(String field, double value) {
		return term(field, NumericUtils.doubleToPrefixCoded(value));
	}

	/** Creates a term. */
	public static Term term(String field, boolean value) {
		return term(field, value ? 1 : 0);
	}

	/** Creates a term. */
	public static Term term(String field, UUID value) {
		return term(field, checkValue(value).toString());
	}

	/** Creates a term. */
	public static Term term(String field, ReadableInstant value) {
		return term(field, checkValue(value).getMillis());
	}

	/** Creates a term. */
	public static Term term(TextSchemaItem field, String value) {
		return term(checkItem(field), value);
	}

	/** Creates a term. */
	public static Term term(IntegerSchemaItem field, int value) {
		return term(checkItem(field), value);
	}

	/** Creates a term. */
	public static Term term(LongSchemaItem field, long value) {
		return term(checkItem(field), value);
	}

	/** Creates a term. */
	public static Term term(FloatSchemaItem field, float value) {
		return term(checkItem(field), value);
	}

	/** Creates a term. */
	public static Term term(DoubleSchemaItem field, double value) {
		return term(checkItem(field), value);
	}

	/** Creates a term. */
	public static Term term(BooleanSchemaItem field, boolean value) {
		return term(checkItem(field), value);
	}

	/** Creates a term. */
	public static Term term(UUIDSchemaItem field, UUID value) {
		return term(checkItem(field), value);
	}

	/** Creates a term. */
	public static Term term(InstantSchemaItem field, ReadableInstant value) {
		return term(checkItem(field), value);
	}

	/** Creates a function from a value to a term. */
	public static Function<String, Term> stringTermBuilder(String field) {
		return new StringTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<Integer, Term> intTermBuilder(String field) {
		return new IntTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<Long, Term> longTermBuilder(String field) {
		return new LongTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<Float, Term> floatTermBuilder(String field) {
		return new FloatTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<Double, Term> doubleTermBuilder(String field) {
		return new DoubleTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<Boolean, Term> booleanTermBuilder(String field) {
		return new BooleanTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<ReadableInstant, Term> instantTermBuilder(String field) {
		return new InstantTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<UUID, Term> uuidTermBuilder(String field) {
		return new UUIDTermBuilder(field);
	}

	/** Creates a function from a value to a term. */
	public static Function<String, Term> termBuilder(TextSchemaItem field) {
		return stringTermBuilder(checkItem(field));
	}

	/** Creates a function from a value to a term. */
	public static Function<Integer, Term> termBuilder(IntegerSchemaItem field) {
		return intTermBuilder(checkItem(field));
	}

	/** Creates a function from a value to a term. */
	public static Function<Long, Term> termBuilder(LongSchemaItem field) {
		return longTermBuilder(checkItem(field));
	}

	/** Creates a function from a value to a term. */
	public static Function<Float, Term> termBuilder(FloatSchemaItem field) {
		return floatTermBuilder(checkItem(field));
	}

	/** Creates a function from a value to a term. */
	public static Function<Double, Term> termBuilder(DoubleSchemaItem field) {
		return doubleTermBuilder(checkItem(field));
	}

	/** Creates a function from a value to a term. */
	public static Function<Boolean, Term> termBuilder(BooleanSchemaItem field) {
		return booleanTermBuilder(checkItem(field));
	}

	/** Creates a function from a value to a term. */
	public static Function<ReadableInstant, Term> termBuilder(InstantSchemaItem field) {
		return instantTermBuilder(checkItem(field));
	}

	/** Creates a function from a value to a term. */
	public static Function<UUID, Term> termBuilder(UUIDSchemaItem field) {
		return uuidTermBuilder(checkItem(field));
	}

	/** Term builder function. */
	private static abstract class TermBuilder<F> implements Function<F, Term> {
		/** Field. */
		final String field;

		TermBuilder(String field) {
			this.field = checkField(field);
		}
	}

	/** String value to term function. */
	private static final class StringTermBuilder extends TermBuilder<String> {
		StringTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(String input) {
			return term(field, input);
		}
	}

	/** Int value to term function. */
	private static final class IntTermBuilder extends TermBuilder<Integer> {
		IntTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(Integer input) {
			return term(field, input);
		}
	}

	/** Long value to term function. */
	private static final class LongTermBuilder extends TermBuilder<Long> {
		LongTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(Long input) {
			return term(field, input);
		}
	}

	/** Float value to term function. */
	private static final class FloatTermBuilder extends TermBuilder<Float> {
		FloatTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(Float input) {
			return term(field, input);
		}
	}

	/** BooleanInt value to term function. */
	private static final class BooleanTermBuilder extends TermBuilder<Boolean> {
		BooleanTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(Boolean input) {
			return term(field, input);
		}
	}

	/** Double value to term function. */
	private static final class DoubleTermBuilder extends TermBuilder<Double> {
		DoubleTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(Double input) {
			return term(field, input);
		}
	}

	/** UUID value to term function. */
	private static final class UUIDTermBuilder extends TermBuilder<UUID> {
		UUIDTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(UUID input) {
			return term(field, input);
		}
	}

	/** Instant value to term function. */
	private static final class InstantTermBuilder extends TermBuilder<ReadableInstant> {
		InstantTermBuilder(String field) {
			super(field);
		}

		@Override
		public Term apply(ReadableInstant input) {
			return term(field, input);
		}
	}

}
