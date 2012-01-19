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
 * Default streamed textual schema item implementation.
 * @author Andres Rodriguez
 */
class DefaultStreamSchemaItem extends DefaultTextSchemaItem implements StreamSchemaItem {
	/**
	 * Constructor.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param norms Whether norms are stored.
	 * @param vectors Whether term vectors are stored.
	 * @param positions Whether term vectors are stored with positions (ignored if vectors is false).
	 * @param offsets Whether term vectors are stored with offsets (ignored if vectors is false).
	 */
	DefaultStreamSchemaItem(String name, int minOccurs, int maxOccurs, boolean norms, boolean vectors, boolean positions,
			boolean offsets) {
		super(name, minOccurs, maxOccurs, false, true, true, norms, vectors, positions, offsets);
	}

}
