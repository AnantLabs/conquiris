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

import com.google.common.base.Function;

/**
 * Conquiris Searcher Service. Each independent call may used a different lucene searcher.
 * @author Andres Rodriguez
 */
public interface SearcherService extends Searcher {
	/**
	 * Perform a personalized and potentially compound query. The searcher provided to the query
	 * performs every operation using a single lucene searcher is guaranteed to be open only during
	 * the execution of the operation.
	 * @param query Query to perform.
	 * @return The query result.
	 */
	<T> T search(Function<Searcher, T> query);
}
