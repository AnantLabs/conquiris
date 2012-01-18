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

import net.conquiris.lucene.search.Hit;

import org.apache.lucene.document.FieldSelector;

import com.google.common.base.Function;

/**
 * Mapper from a search hit to a custom object.
 * @author Andres Rodriguez
 * @param <T> Type of the custom object.
 */
public interface HitMapper<T> extends Function<Hit, T> {
	/**
	 * Returns the field selector to use.
	 * @return The field selector or {@code null} if no selector is used.
	 */
	FieldSelector getFieldSelector();
}
