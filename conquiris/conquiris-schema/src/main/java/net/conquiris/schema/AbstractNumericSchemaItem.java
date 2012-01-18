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

import com.google.common.base.Objects;

/**
 * Base class for numeric schema item implementations.
 * @author Andres Rodriguez
 */
abstract class AbstractNumericSchemaItem<T extends Number> extends AbstractSchemaItem implements NumericSchemaItem<T> {
	/** Hash code. */
	private final int hash;

	/**
	 * Constructor.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	AbstractNumericSchemaItem(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		super(name, minOccurs, maxOccurs, stored, indexed);
		this.hash = Objects.hashCode(getClass(), name, minOccurs, maxOccurs, stored, indexed);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof AbstractNumericSchemaItem) {
			AbstractNumericSchemaItem<?> other = (AbstractNumericSchemaItem<?>) obj;
			return hash == other.hash && getClass() == other.getClass() && getName().equals(other.getName())
					&& getMinOccurs() == other.getMinOccurs() && getMaxOccurs() == other.getMaxOccurs()
					&& isStored() == other.isStored() && isIndexed() == other.isIndexed();
		}
		return false;
	}

}
