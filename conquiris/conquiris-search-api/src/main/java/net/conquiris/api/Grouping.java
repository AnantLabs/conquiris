/*
 * Copyright 2009 the original author or authors.
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
package net.conquiris.api;

import java.util.Map;

/**
 * Base interface for grouped search results.
 * @author Andres Rodriguez
 * @param <T> Payload type.
 */
public interface Grouping<T> extends Map<String, Grouping<T>> {
	/**
	 * Returns the number of hits of int this group.
	 * @return The number of hits.
	 */
	int getHits();

	/**
	 * Returns whether this is the last grouping.
	 * @return True if this the last grouping.
	 */
	boolean isLast();

	/**
	 * Returns the grouping payload.
	 * @return The grouping payload.
	 * @throws IllegalStateException if this is not the last grouping.
	 */
	T getPayload();
}
