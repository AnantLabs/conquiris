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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.FixedBitSet;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

/**
 * A wrapping filter based on Guava {@link Cache}. Uses a caching mode equivalent to Lucene's
 * DeletesMode.RECACHE.
 * @author Andres Rodriguez
 */
public final class GuavaCachingFilter extends Filter {
	/** Serial UID. */
	private static final long serialVersionUID = -3469818482930960618L;

	/** Wrapped filter. */
	private final Filter filter;
	/** Cache. */
	private final Cache<Object, DocIdSet> cache;

	/**
	 * Caching factory method. The primary cache is that from the filter key to the cachinf filter.
	 * The seondary cache is that contained in the caching filter itself.
	 * @param loader Filter loader. Its result will be wrapped in a caching filter.
	 * @param primarySize the maximum size of the primary cache.
	 * @param primaryDuration the length of time after an entry is last accessed that it should be
	 *          automatically removed from the primary cache.
	 * @param primaryUnit the unit that {@code primaryDuration} is expressed in
	 * @param secondarySize the maximum size of the secondary cache.
	 * @param secondaryDuration the length of time after an entry is last accessed that it should be
	 *          automatically removed from the secondary cache.
	 * @param secondaryUnit the unit that {@code secondaryDuration} is expressed in
	 * @throws IllegalArgumentException if any of the sizes is negative
	 * @throws IllegalArgumentException if any of the durations is negative
	 */
	public static <K, V extends Filter> LoadingCache<K, Filter> cachingFactory(final CacheLoader<K, V> loader,
			final int primarySize, final long primaryDuration, final TimeUnit primaryUnit, final int secondarySize,
			final long secondaryDuration, final TimeUnit secondaryUnit) {
		checkArgument(primarySize >= 0 && primaryDuration >= 0 && secondarySize >= 0 && secondaryDuration >= 0);
		checkNotNull(primaryUnit);
		checkNotNull(secondaryUnit);
		final CacheLoader<K, Filter> primaryLoader = new CacheLoader<K, Filter>() {
			public Filter load(K key) throws Exception {
				return new GuavaCachingFilter(loader.load(key), secondarySize, secondaryDuration, secondaryUnit);
			}
		};
		return CacheBuilder.newBuilder().softValues().maximumSize(primarySize)
				.expireAfterAccess(primaryDuration, primaryUnit).build(primaryLoader);
	}

	/**
	 * Factory method.
	 * @param filter Filter to cache results of.
	 * @param size the maximum size of the cache.
	 * @param duration the length of time after an entry is last accessed that it should be
	 *          automatically removed.
	 * @param unit the unit that {@code duration} is expressed in
	 * @throws IllegalArgumentException if {@code size} is negative
	 * @throws IllegalArgumentException if {@code duration} is negative
	 */
	public static GuavaCachingFilter of(Filter filter, int size, long duration, TimeUnit unit) {
		return new GuavaCachingFilter(filter, size, duration, unit);
	}

	/**
	 * Factory method with a maximum size of 100 elements and a expiration time of 10 minutes.
	 * @param filter Filter to cache results of
	 */
	public static GuavaCachingFilter of(Filter filter) {
		return of(filter, 100, 10, TimeUnit.MINUTES);
	}

	/**
	 * Constructor.
	 * @param filter Filter to cache results of.
	 * @param size the maximum size of the cache.
	 * @param duration the length of time after an entry is last accessed that it should be
	 *          automatically removed.
	 * @param unit the unit that {@code duration} is expressed in
	 * @throws IllegalArgumentException if {@code size} is negative
	 * @throws IllegalArgumentException if {@code duration} is negative
	 */
	private GuavaCachingFilter(Filter filter, int size, long duration, TimeUnit unit) {
		this.filter = Preconditions.checkNotNull(filter, "The filter to cache must be provided");
		this.cache = CacheBuilder.newBuilder().softValues().maximumSize(size).expireAfterAccess(duration, unit).build();
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		final Object key = reader.hasDeletions() ? reader.getDeletesCacheKey() : reader.getCoreCacheKey();
		try {
			return cache.get(key, new Loader(reader));
		} catch (Throwable t) {
			Throwable cause = t.getCause();
			if (cause instanceof IOException) {
				throw (IOException) cause;
			}
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			}
			if (cause instanceof Error) {
				throw (Error) cause;
			}
			if (t instanceof UncheckedExecutionException) {
				throw (UncheckedExecutionException) t;
			}
			throw new UncheckedExecutionException(cause);
		}
	}

	/** Returns a current snapshot of this cache's cumulative statistics. */
	public CacheStats stats() {
		return cache.stats();
	}

	@Override
	public String toString() {
		return "GuavaCachingFilter(" + filter + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GuavaCachingFilter))
			return false;
		return this.filter.equals(((GuavaCachingFilter) o).filter);
	}

	@Override
	public int hashCode() {
		return filter.hashCode() ^ 0x1117BF25;
	}

	/** Cache loader. */
	private final class Loader implements Callable<DocIdSet> {
		private final IndexReader reader;

		Loader(IndexReader reader) {
			this.reader = checkNotNull(reader, "The index reader must be provided");
		}

		@Override
		public DocIdSet call() throws Exception {
			return docIdSetToCache(filter.getDocIdSet(reader));
		}

		/**
		 * Provide the DocIdSet to be cached, using the DocIdSet provided by the wrapped Filter.
		 * <p>
		 * This implementation returns the given {@link DocIdSet}, if {@link DocIdSet#isCacheable}
		 * returns <code>true</code>, else it copies the {@link DocIdSetIterator} into an
		 * {@link FixedBitSet}.
		 */
		private DocIdSet docIdSetToCache(DocIdSet docIdSet) throws IOException {
			if (docIdSet == null) {
				// this is better than returning null, as the nonnull result can be cached
				return DocIdSet.EMPTY_DOCIDSET;
			} else if (docIdSet.isCacheable()) {
				return docIdSet;
			} else {
				final DocIdSetIterator it = docIdSet.iterator();
				// null is allowed to be returned by iterator(),
				// in this case we wrap with the empty set,
				// which is cacheable.
				if (it == null) {
					return DocIdSet.EMPTY_DOCIDSET;
				} else {
					final FixedBitSet bits = new FixedBitSet(reader.maxDoc());
					bits.or(it);
					return bits;
				}
			}
		}
	}
}
