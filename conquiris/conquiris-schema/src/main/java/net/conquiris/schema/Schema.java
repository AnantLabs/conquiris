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

import com.google.common.collect.BiMap;

/**
 * Conquiris Lucene document schema definition interface. Implementations MUST BE immutable.
 * @author Andres Rodriguez
 */
public interface Schema extends BiMap<String, SchemaItem> {
	/**
	 * Creates a new schema extending this one. No item with an existing name is allowed,
	 * @param items Schema items to add.
	 * @return The extended schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if an item with an existing name is added.
	 */
	Schema extend(Iterable<? extends SchemaItem> items);

	/**
	 * Creates a new schema extending this one. No item with an existing name is allowed,
	 * @param items Schema items to add.
	 * @return The extended schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if an item with an existing name is added.
	 */
	Schema extend(SchemaItem... items);

	/**
	 * Creates a new schema extending this one with another. No item with an existing name is allowed,
	 * @param schema Schema to add.
	 * @return The extended schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if an item with an existing name is added.
	 */
	Schema extend(Schema schema);

	/**
	 * Creates a new schema extending this one. Provided items override existing ones, but no new
	 * items with the same name are allowed.
	 * @param items Schema items to add.
	 * @return The extended schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if an item with an existing name is added.
	 */
	Schema override(SchemaItem... items);

	/**
	 * Creates a new schema extending this one. Provided items override existing ones, but no new
	 * items with the same name are allowed.
	 * @param items Schema items to add.
	 * @return The extended schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if an item with an existing name is added.
	 */
	Schema override(Iterable<? extends SchemaItem> items);

	/**
	 * Creates a new schema extending this one with another. Provided items override existing ones.
	 * @param schema Schema to add.
	 * @return The extended schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if an item with an existing name is added.
	 */
	Schema override(Schema schema);

}
