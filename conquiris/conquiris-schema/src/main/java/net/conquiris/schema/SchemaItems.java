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
	public static IntegerSchemaItem newInt(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultIntegerSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates an integer schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static IntegerSchemaItem newInt(String name, int minOccurs, boolean stored, boolean indexed) {
		return newInt(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates an integer schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static IntegerSchemaItem newInt(String name, boolean stored, boolean indexed) {
		return newInt(name, 0, stored, indexed);
	}

	/**
	 * Creates an integer schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static SingleIntegerSchemaItem newInt(String name, boolean required, boolean stored, boolean indexed) {
		return new DefaultSingleIntegerSchemaItem(name, required, stored, indexed);
	}

	/**
	 * Creates a long schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static LongSchemaItem newLong(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultLongSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates a long schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static LongSchemaItem newLong(String name, int minOccurs, boolean stored, boolean indexed) {
		return newLong(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates a long schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static LongSchemaItem newLong(String name, boolean stored, boolean indexed) {
		return newLong(name, 0, stored, indexed);
	}

	/**
	 * Creates a long schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static SingleLongSchemaItem newLong(String name, boolean required, boolean stored, boolean indexed) {
		return new DefaultSingleLongSchemaItem(name, required, stored, indexed);
	}

	/**
	 * Creates a float schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static FloatSchemaItem newFloat(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultFloatSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates a float schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static FloatSchemaItem newFloat(String name, int minOccurs, boolean stored, boolean indexed) {
		return newFloat(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates a float schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static FloatSchemaItem newFloat(String name, boolean stored, boolean indexed) {
		return newFloat(name, 0, stored, indexed);
	}

	/**
	 * Creates a float schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static SingleFloatSchemaItem newFloat(String name, boolean required, boolean stored, boolean indexed) {
		return new DefaultSingleFloatSchemaItem(name, required, stored, indexed);
	}

	/**
	 * Creates a double schema item.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static DoubleSchemaItem newDouble(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		return new DefaultDoubleSchemaItem(name, minOccurs, maxOccurs, stored, indexed);
	}

	/**
	 * Creates a double schema item with an unbounded number of occurrences.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static DoubleSchemaItem newDouble(String name, int minOccurs, boolean stored, boolean indexed) {
		return newDouble(name, minOccurs, Integer.MAX_VALUE, stored, indexed);
	}

	/**
	 * Creates a double schema item with any of occurrences.
	 * @param name Field name.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static DoubleSchemaItem newDouble(String name, boolean stored, boolean indexed) {
		return newDouble(name, 0, stored, indexed);
	}

	/**
	 * Creates a double schema item with a single occurrence.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	public static SingleDoubleSchemaItem newDouble(String name, boolean required, boolean stored, boolean indexed) {
		return new DefaultSingleDoubleSchemaItem(name, required, stored, indexed);
	}

}
