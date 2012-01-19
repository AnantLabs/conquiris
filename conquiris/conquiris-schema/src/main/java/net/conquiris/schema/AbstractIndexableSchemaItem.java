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
 * Base class for schema item implementations.
 * @author Andres Rodriguez
 */
abstract class AbstractIndexableSchemaItem extends AbstractSchemaItem {
	/** Whether the field is stored. */
	private boolean stored;
	/** Whether the field is indexed. */
	private boolean indexed;

	/**
	 * Constructor.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	AbstractIndexableSchemaItem(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		super(name, minOccurs, maxOccurs);
		this.stored = stored;
		this.indexed = indexed;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.SchemaItem#isStored()
	 */
	@Override
	public final boolean isStored() {
		return stored;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.SchemaItem#isIndexed()
	 */
	@Override
	public final boolean isIndexed() {
		return indexed;
	}
}
