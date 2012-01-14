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
package net.conquiris.lucene;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.annotation.Nullable;

import net.derquinse.common.reflect.This;

import org.apache.lucene.document.Field;

/**
 * Fieldable builder base class. Fieldables are not stored by default. Builders are NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public abstract class FieldableBuilder<B extends FieldableBuilder<B>> extends This<B> {
	/** Field name. */
	private final String name;
	/** Whether to store the field. */
	private boolean store = false;

	/**
	 * Constructor.
	 * @param name Field name.
	 */
	FieldableBuilder(String name) {
		this.name = checkNotNull(name, "The field name must be provided");
	}

	/** Returns the field name. */
	final String name() {
		return name;
	}

	/** Returns the field store value. */
	final Field.Store fieldStore() {
		return store ? Field.Store.YES : Field.Store.NO;
	}

	/** Sets whether to store the field. */
	public final B store(boolean store) {
		this.store = store;
		return thisValue();
	}

	/** Sets the field to be stored. */
	public final B store() {
		return store(true);
	}

	/**
	 * Sets whether to store the field.
	 * @param store The field will be stored if the argument is Field.Store.YES.
	 */
	public final B store(@Nullable Field.Store store) {
		return store(Field.Store.YES == store);
	}

	/**
	 * Checks the fieldable is either indexed or stored.
	 * @param index Whether to index the field.
	 * @return The store value.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	final Field.Store checkUsed(boolean index) {
		checkState(index || store, "Field neither stored nor indexed");
		return fieldStore();
	}

}
