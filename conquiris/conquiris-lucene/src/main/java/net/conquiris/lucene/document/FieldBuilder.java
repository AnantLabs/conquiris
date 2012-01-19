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
import static com.google.common.base.Preconditions.checkState;
import net.conquiris.schema.IsIndexedFlag;
import net.conquiris.schema.IsStoredFlag;
import net.conquiris.schema.SchemaItem;
import net.derquinse.common.reflect.This;

import org.apache.lucene.document.Field;

/**
 * Field builders base class. Builders are NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public abstract class FieldBuilder<B extends FieldBuilder<B>> extends This<B> implements IsStoredFlag,
		IsIndexedFlag {
	/** Field name. */
	private final String name;

	/**
	 * Constructor.
	 * @param name Field name.
	 */
	FieldBuilder(String name) {
		this.name = checkNotNull(name, "The field name must be provided");
	}

	/**
	 * Constructor based on a schema item..
	 * @param item Schema item to base this builder on.
	 */
	FieldBuilder(SchemaItem item) {
		checkNotNull(item, "The schema item must be provided");
		this.name = checkNotNull(item.getName(), "The field name must be provided");
	}

	/** Returns the field name. */
	final String name() {
		return name;
	}

	/** Returns the field store value. */
	final Field.Store fieldStore() {
		return isStored() ? Field.Store.YES : Field.Store.NO;
	}

	/**
	 * Checks the field is either indexed or stored.
	 * @return The store value.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	final Field.Store checkUsed() {
		checkState(isIndexed() || isStored(), "Field neither stored nor indexed");
		return fieldStore();
	}

}
