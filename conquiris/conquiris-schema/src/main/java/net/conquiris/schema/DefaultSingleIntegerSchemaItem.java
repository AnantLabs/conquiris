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
 * Default int schema item with a single value implementation.
 * @author Andres Rodriguez
 */
class DefaultSingleIntegerSchemaItem extends DefaultIntegerSchemaItem implements SingleIntegerSchemaItem {
	/**
	 * Constructor.
	 * @param name Field name.
	 * @param required Whether the field is required.
	 * @param maxOccurs Maximum number of occurrences.
	 * @param stored Whether the field is stored.
	 * @param indexed Whether the field is indexed.
	 */
	DefaultSingleIntegerSchemaItem(String name, boolean required, boolean stored, boolean indexed) {
		super(name, required ? 1 : 0, 1, stored, indexed);
	}

}
