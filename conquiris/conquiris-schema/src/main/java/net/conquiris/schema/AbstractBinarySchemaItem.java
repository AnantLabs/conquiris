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
 * Abstract binary schema item implementation.
 * @author Andres Rodriguez
 */
class AbstractBinarySchemaItem extends AbstractSchemaItem implements BinarySchemaItem {
	/** Hash code. */
	private final int hash;

	/**
	 * Constructor.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 */
	AbstractBinarySchemaItem(String name, int minOccurs, int maxOccurs) {
		super(name, minOccurs, maxOccurs);
		this.hash = Objects.hashCode(BinarySchemaItem.class, name, minOccurs, maxOccurs);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.SchemaItem#isStored()
	 */
	@Override
	public final boolean isStored() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.SchemaItem#isIndexed()
	 */
	@Override
	public final boolean isIndexed() {
		return false;
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
		if (obj instanceof BinarySchemaItem) {
			BinarySchemaItem other = (BinarySchemaItem) obj;
			return hash == other.hashCode() && getName().equals(other.getName()) && getMinOccurs() == other.getMinOccurs()
					&& getMaxOccurs() == other.getMaxOccurs();
		}
		return false;
	}

}
