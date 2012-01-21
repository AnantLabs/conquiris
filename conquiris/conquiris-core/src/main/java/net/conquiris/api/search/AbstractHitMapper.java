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

import javax.annotation.Nullable;

import org.apache.lucene.document.FieldSelector;

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

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.DocMapper#getFieldSelector()
	 */
	@Override
	public final FieldSelector getFieldSelector() {
		return selector;
	}
}
