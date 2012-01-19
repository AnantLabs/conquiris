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

import javax.annotation.Nullable;

import net.conquiris.schema.SchemaItem;

import org.apache.lucene.document.Field;

/**
 * Base class for indexable field builders. Indexable fields are indexed but not stored by default.
 * @author Andres Rodriguez
 */
public abstract class IndexableFieldBuilder<B extends IndexableFieldBuilder<B>> extends FieldBuilder<B> {
	/** Whether to store the field. */
	private boolean store = false;
	/** Whether to index the field. */
	private boolean index = true;

	/**
	 * Constructor.
	 * @param name Field name.
	 */
	IndexableFieldBuilder(String name) {
		super(name);
	}

	/**
	 * Constructor based on a schema item..
	 * @param item Schema item to base this builder on.
	 */
	IndexableFieldBuilder(SchemaItem item) {
		super(item);
		this.index = item.isIndexed();
		this.store = item.isStored();
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.document.FieldableBuilder#isStored()
	 */
	@Override
	public final boolean isStored() {
		return store;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.lucene.document.FieldableBuilder#isIndexed()
	 */
	@Override
	public final boolean isIndexed() {
		return index;
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

	/** Sets whether to index the field. */
	public final B index(boolean index) {
		this.index = index;
		return thisValue();
	}

}
