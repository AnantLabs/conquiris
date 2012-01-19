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
package net.conquiris.lucene.search;

import java.util.List;

import net.conquiris.schema.FieldNameProperty;

import com.google.common.base.Optional;

/**
 * Interface for the fields of a search hit. All methods return non-null values.
 * @author Andres Rodriguez
 * @param <T> Field value type.
 */
public interface FieldValues<T> extends FieldNameProperty {
	/** Returns all the values of the field. */
	List<T> getAll();

	/** Return the optional first value of the field. */
	Optional<T> getOptional();

	/**
	 * Returns the required first value of the field.
	 * @throws IllegalStateException if the document contains no value for the field.
	 */
	T get();
}
