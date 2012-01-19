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

import static com.google.common.base.Preconditions.checkNotNull;
import net.conquiris.schema.BinarySchemaItem;

import org.apache.lucene.document.Field;

/**
 * Base class for binary field builders. Binary fields are stored and not indexed.
 * @author Andres Rodriguez
 */
public abstract class BaseBinaryFieldBuilder<B extends BaseBinaryFieldBuilder<B>> extends FieldBuilder<B> {
	/**
	 * Constructor.
	 * @param name Field name.
	 */
	BaseBinaryFieldBuilder(String name) {
		super(name);
	}

	/**
	 * Constructor based on a schema item..
	 * @param item Schema item to base this builder on.
	 */
	BaseBinaryFieldBuilder(BinarySchemaItem item) {
		super(item);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.document.FieldableBuilder#isStored()
	 */
	@Override
	public final boolean isStored() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.document.FieldableBuilder#isIndexed()
	 */
	@Override
	public final boolean isIndexed() {
		return false;
	}

	/**
	 * Builds a binary field with the provided value.
	 * @param value Field value.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final Field build(byte[] value) {
		checkNotNull(value, "The field value must be provided");
		return new Field(name(), value);
	}

}
