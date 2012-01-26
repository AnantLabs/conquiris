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
}
