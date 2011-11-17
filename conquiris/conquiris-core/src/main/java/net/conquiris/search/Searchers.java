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

import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.Searcher;
import net.conquiris.api.search.SearcherService;

import org.apache.lucene.search.IndexSearcher;

/**
 * Searchers support class.
 * @author Andres Rodriguez
 */
public final class Searchers {
	/** Not instantiable. */
	private Searchers() {
		throw new AssertionError();
	}

	/**
	 * Returns a searcher for a Lucene index searcher. The searcher is never closed and all operations
	 * are performed with the provided searcher.
	 * @param searcher Index searcher to use.
	 * @return The requested object.
	 */
	public static Searcher of(IndexSearcher searcher) {
		return new DefaultSearcher(searcher);
	}

	/**
	 * Returns a searcher service.
	 * @param supplier Reader supplier to use.
	 * @return The requested service.
	 */
	public static SearcherService service(ReaderSupplier supplier) {
		return new DefaultSearcherService(supplier);
	}

}
