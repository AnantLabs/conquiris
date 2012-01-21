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

import java.util.Arrays;
import java.util.Set;

import net.conquiris.schema.SchemaItem;

import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.SetBasedFieldSelector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

/**
 * Support class for field selectors.
 * @author Andres Rodriguez
 * @see FieldSelector.
 */
public final class FieldSelectors {
	/** Constructor with no selector. */
	private FieldSelectors() {
		throw new AssertionError();
	}

	/**
	 * Creates a mapper with a selector that loads a certain set of fields eagerly, another set lazily
	 * and the rest are not loaded.
	 * @param fieldsToLoad Fields to load eagerly.
	 * @param fieldsToLoadLazily Fields to load lazily.
	 * @throws NullPointerException if any of the argument or any of their members is {@code null}.
	 */
	public static FieldSelector loadFields(Iterable<String> fieldsToLoad, Iterable<String> fieldsToLoadLazily) {
		Set<String> eager = ImmutableSet.copyOf(fieldsToLoad);
		Set<String> lazy = ImmutableSet.copyOf(fieldsToLoadLazily);
		return new SetBasedFieldSelector(eager, lazy);
	}

	/**
	 * Creates a mapper with a selector that loads a certain set of fields eagerly and the rest are
	 * not loaded.
	 * @param fieldsToLoad Fields to load eagerly.
	 * @throws NullPointerException if any the argument or any of its members is {@code null}.
	 */
	public static FieldSelector loadFields(Iterable<String> fieldsToLoad) {
		return loadFields(fieldsToLoad, ImmutableSet.<String> of());
	}

	/**
	 * Creates a mapper with a selector that loads a certain set of fields eagerly and the rest are
	 * not loaded.
	 * @param fieldsToLoad Fields to load eagerly.
	 * @throws NullPointerException if any the arguments is {@code null}.
	 */
	public static FieldSelector loadFields(String... fieldsToLoad) {
		return loadFields(Arrays.asList(fieldsToLoad));
	}

	/**
	 * Creates a mapper with a selector that loads a certain set of fields (represented by schema
	 * items) eagerly, another set lazily and the rest are not loaded.
	 * @param itemsToLoad Fields to load eagerly.
	 * @param itemsToLoadLazily Fields to load lazily.
	 * @throws NullPointerException if any of the argument or any of their members is {@code null}.
	 */
	public static FieldSelector loadItems(Iterable<? extends SchemaItem> itemsToLoad,
			Iterable<? extends SchemaItem> itemsToLoadLazily) {
		return loadFields(Iterables.transform(itemsToLoad, SchemaItem.NAME),
				Iterables.transform(itemsToLoadLazily, SchemaItem.NAME));
	}

	/**
	 * Creates a mapper with a selector that loads a certain set of fields (represented by schema
	 * items) eagerly and the rest are not loaded.
	 * @param itemsToLoad Fields to load eagerly.
	 * @throws NullPointerException if any the argument or any of its members is {@code null}.
	 */
	public static FieldSelector loadItems(Iterable<? extends SchemaItem> itemsToLoad) {
		return loadFields(Iterables.transform(itemsToLoad, SchemaItem.NAME), ImmutableSet.<String> of());
	}

	/**
	 * Creates a mapper with a selector that loads a certain set of fields (represented by schema
	 * items) eagerly and the rest are not loaded.
	 * @param itemsToLoad Fields to load eagerly.
	 * @throws NullPointerException if any the arguments is {@code null}.
	 */
	public static FieldSelector loadItems(SchemaItem... itemsToLoad) {
		return loadItems(Arrays.asList(itemsToLoad));
	}

}
