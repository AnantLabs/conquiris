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
import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.Searcher;
import net.conquiris.api.search.SearcherService;

import org.apache.lucene.search.IndexSearcher;

import com.google.common.base.Function;
import com.google.common.io.Closeables;

/**
 * Default searcher implementation, based on a single index searcher that it's never closed by this
 * object.
 * @author Andres Rodriguez
 */
final class DefaultSearcherService extends AbstractSearcher implements SearcherService {
	/** Reader supplier to use. */
	private final ReaderSupplier supplier;

	/** Constructor. */
	DefaultSearcherService(ReaderSupplier supplier) {
		this.supplier = checkNotNull(supplier, "The reader supplier must be provided");
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.SearcherService#search(com.google.common.base.Function)
	 */
	@Override
	public <T> T search(final Function<Searcher, T> query) {
		checkNotNull(query, "The complex query to perform must be provided");
		return perform(new Op<T>() {
			@Override
			T perform(IndexSearcher searcher) throws Exception {
				return query.apply(Searchers.of(searcher));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.search.AbstractSearcher#getIndexSearcher()
	 */
	IndexSearcher getIndexSearcher() {
		return new IndexSearcher(supplier.get().get());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.conquiris.search.AbstractSearcher#disposeIndexSearcher(org.apache.lucene.search.IndexSearcher
	 * )
	 */
	void disposeIndexSearcher(IndexSearcher searcher) {
		Closeables.closeQuietly(searcher);
		Closeables.closeQuietly(searcher.getIndexReader());
	}
}
