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
package net.conquiris.search;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.lucene.search.IndexSearcher;

/**
 * Default searcher implementation, based on a single index searcher that it's never closed by this
 * object.
 * @author Andres Rodriguez
 */
final class DefaultSearcher extends AbstractSearcher {
	/** Index searcher to use. */
	private final IndexSearcher searcher;

	/** Constructor. */
	DefaultSearcher(IndexSearcher searcher) {
		this.searcher = checkNotNull(searcher, "The index searcher must be provided");
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.search.AbstractSearcher#getIndexSearcher()
	 */
	IndexSearcher getIndexSearcher() {
		return searcher;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.conquiris.search.AbstractSearcher#disposeIndexSearcher(org.apache.lucene.search.IndexSearcher
	 * )
	 */
	void disposeIndexSearcher(IndexSearcher searcher) {
		// Nothing to do
	}
}
