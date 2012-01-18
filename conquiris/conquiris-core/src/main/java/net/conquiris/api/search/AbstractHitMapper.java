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
package net.conquiris.api.search;

import java.util.Set;

import javax.annotation.Nullable;

import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.SetBasedFieldSelector;

import com.google.common.collect.ImmutableSet;

/**
 * Abstract implementation of a hit mapper.
 * @author Andres Rodriguez
 * @param <T> Type of the custom object.
 */
public abstract class AbstractHitMapper<T> implements HitMapper<T> {
	/** Field selector. */
	private final FieldSelector selector;

	/** Constructor with no selector. */
	protected AbstractHitMapper() {
		this.selector = null;
	}

	/** Constructor with a provided selector. */
	protected AbstractHitMapper(@Nullable FieldSelector selector) {
		this.selector = selector;
	}

	/**
	 * Creates a mapper with a selector that loads a certain set of fields eagerly, another set lazily
	 * and the rest are not loaded.
	 * @param fieldsToLoad Fields to load eagerly.
	 * @param fieldsToLoadLazily Fields to load lazily.
	 * @throws NullPointerException if any of the argument or any of their members is {@code null}.
	 */
	protected AbstractHitMapper(Iterable<String> fieldsToLoad, Iterable<String> fieldsToLoadLazily) {
		Set<String> eager = ImmutableSet.copyOf(fieldsToLoad);
		Set<String> lazy = ImmutableSet.copyOf(fieldsToLoadLazily);
		this.selector = new SetBasedFieldSelector(eager, lazy);
	}

	/**
	 * Creates a mapper with a selector that loads only a certain set of fields.
	 * @param fieldsToLoad Fields to load.
	 * @throws NullPointerException if the argument or any of its members is {@code null}.
	 */
	protected AbstractHitMapper(Iterable<String> fieldsToLoad) {
		this(fieldsToLoad, ImmutableSet.<String> of());
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.DocMapper#getFieldSelector()
	 */
	@Override
	public final FieldSelector getFieldSelector() {
		return selector;
	}
}
