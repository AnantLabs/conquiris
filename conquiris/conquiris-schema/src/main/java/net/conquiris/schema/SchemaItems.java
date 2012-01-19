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
package net.conquiris.schema;

/**
 * Schema items factory methods.
 * @author Andres Rodriguez
 */
public final class SchemaItems {
	/** Not instantiable. */
	private SchemaItems() {
		throw new AssertionError();
	}

	/**
	 * Creates an integer schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static IntegerSchemaItem intValue(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultIntegerSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates an integer schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static IntegerSchemaItem intValue(String name, int minOccurs, boolean stored, boolean indexed) {
		return intValue(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates an integer schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static IntegerSchemaItem intValue(String name, boolean stored, boolean indexed) {
		return intValue(name, 0, stored, indexed);
	}

	/**
	 * Creates an integer schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static IntegerSchemaItem intValue(String name, boolean required, boolean stored, boolean indexed) {
		return intValue(name, required ? 1 : 0, 1, stored, indexed);
	}

	/**
	 * Creates a long schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static LongSchemaItem longValue(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultLongSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates a long schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static LongSchemaItem longValue(String name, int minOccurs, boolean stored, boolean indexed) {
		return longValue(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates a long schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static LongSchemaItem longValue(String name, boolean stored, boolean indexed) {
		return longValue(name, 0, stored, indexed);
	}

	/**
	 * Creates a long schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static LongSchemaItem longValue(String name, boolean required, boolean stored, boolean indexed) {
		return longValue(name, required ? 1 : 0, 1, stored, indexed);
	}

	/**
	 * Creates a float schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static FloatSchemaItem floatValue(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultFloatSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates a float schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static FloatSchemaItem floatValue(String name, int minOccurs, boolean stored, boolean indexed) {
		return floatValue(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates a float schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static FloatSchemaItem floatValue(String name, boolean stored, boolean indexed) {
		return floatValue(name, 0, stored, indexed);
	}

	/**
	 * Creates a float schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static FloatSchemaItem floatValue(String name, boolean required, boolean stored, boolean indexed) {
		return floatValue(name, required ? 1 : 0, 1, stored, indexed);
	}

	/**
	 * Creates a double schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static DoubleSchemaItem doubleValue(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultDoubleSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates a double schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static DoubleSchemaItem doubleValue(String name, int minOccurs, boolean stored, boolean indexed) {
		return doubleValue(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates a double schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static DoubleSchemaItem doubleValue(String name, boolean stored, boolean indexed) {
		return doubleValue(name, 0, stored, indexed);
	}

	/**
	 * Creates a double schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static DoubleSchemaItem doubleValue(String name, boolean required, boolean stored, boolean indexed) {
		return doubleValue(name, required ? 1 : 0, 1, stored, indexed);
	}

	/**
	 * Creates a textual schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static TextSchemaItem text(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed,
			boolean tokenized, boolean norms, boolean vectors, boolean positions, boolean offsets) {
		return new DefaultTextSchemaItem(name, minOccurs, maxOccurs, stored, indexed, tokenized, norms, vectors, positions,
				offsets);
	}

	/**
	 * Creates a textual schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static TextSchemaItem text(String name, int minOccurs, boolean stored, boolean indexed, boolean tokenized,
			boolean norms, boolean vectors, boolean positions, boolean offsets) {
		return text(name, minOccurs, Integer.MAX_VALUE, stored, indexed, tokenized, norms, vectors, positions, offsets);
	}

	/**
	 * Creates a textual schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static TextSchemaItem text(String name, boolean stored, boolean indexed, boolean tokenized, boolean norms,
			boolean vectors, boolean positions, boolean offsets) {
		return text(name, 0, stored, indexed, tokenized, norms, vectors, positions, offsets);
	}

	/**
	 * Creates a textual schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static TextSchemaItem text(String name, boolean required, boolean stored, boolean indexed, boolean tokenized,
			boolean norms, boolean vectors, boolean positions, boolean offsets) {
		return text(name, required ? 1 : 0, 1, stored, indexed, tokenized, norms, vectors, positions, offsets);
	}

	/**
	 * Creates a textual schema item and no term vectors.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 */
	public static TextSchemaItem text(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed,
			boolean tokenized, boolean norms) {
		return text(name, minOccurs, maxOccurs, stored, indexed, tokenized, norms, false, false, false);
	}

	/**
	 * Creates a textual schema item with an unbounded number of occurrences and no term vectors.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 */
	public static TextSchemaItem text(String name, int minOccurs, boolean stored, boolean indexed, boolean tokenized,
			boolean norms) {
		return text(name, minOccurs, Integer.MAX_VALUE, stored, indexed, tokenized, norms);
	}

	/**
	 * Creates a textual schema item with any of occurrences and no term vectors.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 */
	public static TextSchemaItem text(String name, boolean stored, boolean indexed, boolean tokenized, boolean norms) {
		return text(name, 0, stored, indexed, tokenized, norms);
	}

	/**
	 * Creates a textual schema item with a single occurrence and no term vectors.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 * @param tokenized Whether the field is tokenized.
	 * @param norms Whether norms are stored.
	 */
	public static TextSchemaItem text(String name, boolean required, boolean stored, boolean indexed, boolean tokenized,
			boolean norms) {
		return text(name, required, stored, indexed, tokenized, norms, false, false, false);
	}

	/**
	 * Creates a streamed textual schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static StreamSchemaItem stream(String name, int minOccurs, int maxOccurs, boolean norms, boolean vectors,
			boolean positions, boolean offsets) {
		return new DefaultStreamSchemaItem(name, minOccurs, maxOccurs, norms, vectors, positions, offsets);
	}

	/**
	 * Creates a streamed textual schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static StreamSchemaItem stream(String name, int minOccurs, boolean norms, boolean vectors, boolean positions,
			boolean offsets) {
		return stream(name, minOccurs, Integer.MAX_VALUE, norms, vectors, positions, offsets);
	}

	/**
	 * Creates a streamed textual schema item with any of occurrences.
	 * @param name Field name.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static StreamSchemaItem stream(String name, boolean norms, boolean vectors, boolean positions, boolean offsets) {
		return stream(name, 0, norms, vectors, positions, offsets);
	}

	/**
	 * Creates a streamed textual schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	public static StreamSchemaItem stream(String name, boolean required, boolean norms, boolean vectors,
			boolean positions, boolean offsets) {
		return stream(name, required ? 1 : 0, 1, norms, vectors, positions, offsets);
	}

	/**
	 * Creates an id textual schema item. Ids are not tokenized, and have no norms and no vectors.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static TextSchemaItem id(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new IdSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates an id textual schema item with an unbounded number of occurrences. Ids are not
	 * tokenized, and have no norms and no vectors.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static TextSchemaItem id(String name, int minOccurs, boolean stored, boolean indexed) {
		return id(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates an id textual schema item with any of occurrences. Ids are not tokenized, and have no
	 * norms and no vectors.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static TextSchemaItem id(String name, boolean stored, boolean indexed) {
		return id(name, 0, stored, indexed);
	}

	/**
	 * Creates an id textual schema item with a single occurrence. Ids are not tokenized, and have no
	 * norms and no vectors.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static TextSchemaItem id(String name, boolean required, boolean stored, boolean indexed) {
		return id(name, required ? 1 : 0, 1, stored, indexed);
	}

	/**
	 * Creates an id textual schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static UUIDSchemaItem uuid(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultUUIDSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates an id textual schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static UUIDSchemaItem uuid(String name, int minOccurs, boolean stored, boolean indexed) {
		return uuid(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates an id textual schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static UUIDSchemaItem uuid(String name, boolean stored, boolean indexed) {
		return uuid(name, 0, stored, indexed);
	}

	/**
	 * Creates an id textual schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static UUIDSchemaItem uuid(String name, boolean required, boolean stored, boolean indexed) {
		return uuid(name, required ? 1 : 0, 1, stored, indexed);
	}

	/**
	 * Creates a instant schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static InstantSchemaItem instant(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultInstantSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates a instant schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static InstantSchemaItem instant(String name, int minOccurs, boolean stored, boolean indexed) {
		return instant(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates a instant schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static InstantSchemaItem instant(String name, boolean stored, boolean indexed) {
		return instant(name, 0, stored, indexed);
	}

	/**
	 * Creates a instant schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static InstantSchemaItem instant(String name, boolean required, boolean stored, boolean indexed) {
		return instant(name, required ? 1 : 0, 1, stored, indexed);
	}

	/**
	 * Creates a binary schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 */
	public static BinarySchemaItem binary(String name, int minOccurs, int maxOccurs) {
		return new DefaultBinarySchemaItem(name, minOccurs, maxOccurs);
	}

	/**
	 * Creates a binary schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 */
	public static BinarySchemaItem binary(String name, int minOccurs) {
		return binary(name, minOccurs, Integer.MAX_VALUE);
	}

	/**
	 * Creates a binary schema item with any of occurrences.
	 * @param name Field name.
	 */
	public static BinarySchemaItem binary(String name) {
		return binary(name, 0);
	}

	/**
	 * Creates a binary schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 */
	public static BinarySchemaItem binary(String name, boolean required) {
		return binary(name, required ? 1 : 0, 1);
	}

}
