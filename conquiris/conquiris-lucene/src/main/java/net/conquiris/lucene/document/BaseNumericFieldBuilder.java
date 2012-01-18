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
package net.conquiris.lucene.document;

import net.conquiris.schema.NumericSchemaItem;

import org.apache.lucene.document.NumericField;

/**
 * Base class for numeric field builders. Numeric fields are indexed but not stored by default.
 * @author Andres Rodriguez
 */
public abstract class BaseNumericFieldBuilder<B extends BaseNumericFieldBuilder<B>> extends FieldableBuilder<B> {
	/** Whether to index the field. */
	private boolean index = true;

	/**
	 * Constructor.
	 * @param name Field name.
	 */
	BaseNumericFieldBuilder(String name) {
		super(name);
	}

	/**
	 * Constructor based on a schema item.
	 * @param item Schema item to base this builder on.
	 */
	BaseNumericFieldBuilder(NumericSchemaItem<?> item) {
		super(item);
		index = item.isIndexed();
	}

	/** Returns whether the field will be indexed. */
	public final boolean isIndexed() {
		return index;
	}

	/**
	 * Builds a numeric field with the current information.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	private NumericField buildField() {
		return new NumericField(name(), checkUsed(index), index);
	}

	/** Sets whether to index the field. */
	public final B index(boolean index) {
		this.index = index;
		return thisValue();
	}

	/**
	 * Builds a numeric field with the current information and an int value.
	 * @param value Field value.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final NumericField build(int value) {
		return buildField().setIntValue(value);
	}

	/**
	 * Builds a numeric field with the current information and a long value.
	 * @param value Field value.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final NumericField build(long value) {
		return buildField().setLongValue(value);
	}

	/**
	 * Builds a numeric field with the current information and a float value.
	 * @param value Field value.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final NumericField build(float value) {
		return buildField().setFloatValue(value);
	}

	/**
	 * Builds a numeric field with the current information and a double value.
	 * @param value Field value.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final NumericField build(double value) {
		return buildField().setDoubleValue(value);
	}

}
