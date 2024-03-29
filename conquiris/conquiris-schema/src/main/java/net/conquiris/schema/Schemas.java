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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

/**
 * Schema factory methods.
 * @author Andres Rodriguez
 */
public final class Schemas {
	/** Not instantiable. */
	private Schemas() {
		throw new AssertionError();
	}

	/** Empty schema. */
	private static final Schema EMPTY = new SchemaImpl(ImmutableBiMap.<String, SchemaItem> of());

	/** Returns the empty schema. */
	public static Schema empty() {
		return EMPTY;
	}

	/**
	 * Creates a new schema.
	 * @param items Schema items.
	 * @return The created schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if there are duplicate items and/or item names.
	 */
	public static Schema of(Iterable<? extends SchemaItem> items) {
		return EMPTY.override(items);
	}

	/**
	 * Creates a new schema.
	 * @param items Schema items.
	 * @return The created schema.
	 * @throws NullPointerException if the argument or any of the items is {@code null}.
	 * @throws IllegalArgumentException if there are duplicate items and/or item names.
	 */
	public static Schema of(SchemaItem... items) {
		return of(Arrays.asList(items));
	}

	/**
	 * Creates a new schema with a single item.
	 * @param item Schema item.
	 * @return The created schema.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public static Schema of(SchemaItem item) {
		checkNotNull(item, "The schema item must be provided");
		return new SchemaImpl(ImmutableMap.of(item.getName(), item));
	}

}
