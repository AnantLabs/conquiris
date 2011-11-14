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

/**
 * Base interface for search results.
 * @author Andres Rodriguez
 */
public interface Result {
	/**
	 * Returns the time needed to get the result.
	 * @return The elapsed time in milliseconds.
	 */
	long getTime();

	/**
	 * Returns the total number of hits of the performed query.
	 * @return The total number of hits.
	 */
	int getTotalHits();
}
