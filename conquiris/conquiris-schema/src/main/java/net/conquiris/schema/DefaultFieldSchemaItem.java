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
 * Default textual schema item implementation.
 * @author Andres Rodriguez
 */
class DefaultFieldSchemaItem extends AbstractSchemaItem implements FieldSchemaItem {
	/** Hash code. */
	private final int hash;
	/** Whether the field is tokenized. */
	private final boolean tokenized;
	/** Whether norms are stored. */
	private final boolean norms;
	/** Whether term vectors are stored. */
	private final boolean vectors;
	/** Whether term vectors are stored with positions. */
	private final boolean positions;
	/** Whether term vectors are stored with offsets. */
	private final boolean offsets;

	/**
	 * Constructor.
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
	DefaultFieldSchemaItem(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed, boolean tokenized,
			boolean norms, boolean vectors, boolean positions, boolean offsets) {
		super(name, minOccurs, maxOccurs, stored, indexed);
		this.tokenized = tokenized;
		this.norms = norms;
		this.vectors = vectors;
		this.positions = vectors && positions;
		this.offsets = vectors && offsets;
		this.hash = Objects.hashCode(getClass(), name, minOccurs, maxOccurs, stored, indexed, norms, vectors,
				this.positions, this.offsets);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.FieldSchemaItem#isTokenized()
	 */
	@Override
	public final boolean isTokenized() {
		return tokenized;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.FieldSchemaItem#isNorms()
	 */
	@Override
	public final boolean isNorms() {
		return norms;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.FieldSchemaItem#isVectors()
	 */
	@Override
	public final boolean isVectors() {
		return vectors;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.FieldSchemaItem#isPositions()
	 */
	@Override
	public final boolean isPositions() {
		return positions;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.FieldSchemaItem#isOffsets()
	 */
	@Override
	public final boolean isOffsets() {
		return offsets;
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
		if (obj instanceof DefaultFieldSchemaItem) {
			DefaultFieldSchemaItem other = (DefaultFieldSchemaItem) obj;
			return hash == other.hash && getClass() == other.getClass() && getName().equals(other.getName())
					&& getMinOccurs() == other.getMinOccurs() && getMaxOccurs() == other.getMaxOccurs()
					&& isStored() == other.isStored() && isIndexed() == other.isIndexed() && tokenized == other.tokenized
					&& norms == other.norms && vectors == other.vectors && positions == other.positions
					&& offsets == other.offsets;
		}
		return false;
	}

}
