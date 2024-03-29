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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import net.conquiris.api.search.CountResult;
import net.conquiris.api.search.Highlight;
import net.conquiris.api.search.Highlight.HighlightedQuery;
import net.conquiris.api.search.HitMapper;
import net.conquiris.api.search.IndexNotAvailableException;
import net.conquiris.api.search.ItemResult;
import net.conquiris.api.search.PageResult;
import net.conquiris.api.search.SearchException;
import net.conquiris.api.search.Searcher;
import net.conquiris.lucene.search.Hit;
import net.conquiris.lucene.search.ScoredTotalHitCountCollector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TotalHitCountCollector;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

/**
 * Abstract searcher implementation.
 * @author Andres Rodriguez
 */

abstract class AbstractSearcher implements Searcher {
	/** Constructor. */
	AbstractSearcher() {
	}

	/**
	 * Fetch searcher to use.
	 * @throws IndexNotAvailableException if an error occurs.
	 */
	abstract IndexSearcher getIndexSearcher();

	/**
	 * Dispose used searcher.
	 * @param searcher Searcher to dispose.
	 */
	abstract void disposeIndexSearcher(IndexSearcher searcher);

	/**
	 * Performs a primitive operation.
	 * @param operation Operation to perform.
	 * @return Operation return value.
	 */
	<T> T perform(Op<T> operation) {
		final IndexSearcher searcher = getIndexSearcher();
		try {
			return operation.apply(searcher);
		} finally {
			disposeIndexSearcher(searcher);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#doc(int)
	 */
	public final Document doc(final int i) {
		return perform(new Op<Document>() {
			@Override
			Document perform(IndexSearcher searcher) throws Exception {
				return searcher.doc(i);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#rewrite(org.apache.lucene.search.Query)
	 */
	public final Query rewrite(final Query query) {
		return perform(new Op<Query>() {
			@Override
			Query perform(IndexSearcher searcher) throws Exception {
				return searcher.rewrite(query);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#search(org.apache.lucene.search.Query,
	 * org.apache.lucene.search.Filter, org.apache.lucene.search.Collector)
	 */
	public final void search(final Query query, final Filter filter, final Collector results) {
		perform(new Op<Object>() {
			@Override
			Object perform(IndexSearcher searcher) throws Exception {
				searcher.search(query, filter, results);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#search(org.apache.lucene.search.Query,
	 * org.apache.lucene.search.Filter, int)
	 */
	public final TopDocs search(final Query query, final Filter filter, final int n) {
		return perform(new Op<TopDocs>() {
			@Override
			TopDocs perform(IndexSearcher searcher) throws Exception {
				return searcher.search(query, filter, n);
			}
		});
	}

	public final TopFieldDocs search(final Query query, final Filter filter, final int n, final Sort sort) {
		return perform(new Op<TopFieldDocs>() {
			@Override
			TopFieldDocs perform(IndexSearcher searcher) throws Exception {
				return searcher.search(query, filter, n, sort);
			}
		});
	}

	/** TopDocs helper method. */
	private TopDocs getTopDocs(IndexSearcher searcher, Query query, Filter filter, Sort sort, int hits)
			throws IOException {
		final TopDocs docs;
		if (sort == null) {
			docs = searcher.search(query, filter, hits);
		} else {
			docs = searcher.search(query, filter, hits, sort);
		}
		return docs;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#getFirst(net.conquiris.api.search.DocMapper,
	 * org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.Sort,
	 * net.conquiris.api.search.Highlight)
	 */
	public final <T> ItemResult<T> getFirst(final HitMapper<T> mapper, final Query query, final @Nullable Filter filter,
			final @Nullable Sort sort, final @Nullable Highlight highlight) {
		return perform(new Op<ItemResult<T>>() {
			public ItemResult<T> perform(IndexSearcher searcher) throws Exception {
				Stopwatch w = Stopwatch.createStarted();
				Query rewritten = searcher.rewrite(query);
				TopDocs docs = getTopDocs(searcher, query, filter, sort, 1);
				if (docs.totalHits > 0) {
					ScoreDoc sd = docs.scoreDocs[0];
					HighlightedQuery highlighted = MoreObjects.firstNonNull(highlight, Highlight.no()).highlight(rewritten);
					float score = sd.score;
					T item = map(searcher, sd, highlighted, mapper);
					return ItemResult.found(docs.totalHits, score, w.elapsed(TimeUnit.MILLISECONDS), item);
				} else {
					return ItemResult.notFound(w.elapsed(TimeUnit.MILLISECONDS));
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#getPage(net.conquiris.api.search.DocMapper,
	 * org.apache.lucene.search.Query, int, int, org.apache.lucene.search.Filter,
	 * org.apache.lucene.search.Sort, net.conquiris.api.search.Highlight)
	 */
	public final <T> PageResult<T> getPage(final HitMapper<T> mapper, final Query query, final int firstRecord,
			final int maxRecords, final @Nullable Filter filter, final @Nullable Sort sort,
			final @Nullable Highlight highlight) {

		// Corner case
		if (maxRecords < 1) {
			CountResult r = getCount(query, filter, true);
			return PageResult.notFound(r.getTotalHits(), r.getMaxScore(), r.getTime(), firstRecord);
		}

		// Normal operation
		return perform(new Op<PageResult<T>>() {
			public PageResult<T> perform(IndexSearcher searcher) throws Exception {
				Stopwatch w = Stopwatch.createStarted();
				int total = firstRecord + maxRecords;
				Query rewritten = searcher.rewrite(query);
				TopDocs docs = getTopDocs(searcher, rewritten, filter, sort, total);
				if (docs.totalHits > 0) {
					int n = Math.min(total, docs.scoreDocs.length);
					float score = docs.getMaxScore();
					if (n > firstRecord) {
						final List<T> items = Lists.newArrayListWithCapacity(n - firstRecord);
						HighlightedQuery highlighted = MoreObjects.firstNonNull(highlight, Highlight.no()).highlight(rewritten);
						for (int i = firstRecord; i < n; i++) {
							ScoreDoc sd = docs.scoreDocs[i];
							T item = map(searcher, sd, highlighted, mapper);
							items.add(item);
						}
						return PageResult.found(docs.totalHits, score, w.elapsed(TimeUnit.MILLISECONDS), firstRecord, items);
					} else {
						return PageResult.notFound(docs.totalHits, score, w.elapsed(TimeUnit.MILLISECONDS), firstRecord);
					}
				} else {
					return PageResult.notFound(w.elapsed(TimeUnit.MILLISECONDS), firstRecord);
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#getCount(org.apache.lucene.search.Query,
	 * org.apache.lucene.search.Filter, boolean)
	 */
	@Override
	public CountResult getCount(final Query query, final @Nullable Filter filter, final boolean score) {
		return perform(new Op<CountResult>() {
			public CountResult perform(IndexSearcher searcher) throws Exception {
				final Stopwatch w = Stopwatch.createStarted();
				final ScoredTotalHitCountCollector scoredCollector;
				final TotalHitCountCollector collector;
				if (score) {
					scoredCollector = new ScoredTotalHitCountCollector();
					collector = scoredCollector;
				} else {
					scoredCollector = null;
					collector = new TotalHitCountCollector();
				}
				searcher.search(query, filter, collector);
				final float maxScore = score ? scoredCollector.getMaxScore() : 1.0f;
				return CountResult.of(collector.getTotalHits(), maxScore, w.elapsed(TimeUnit.MILLISECONDS));
			}
		});
	}

	/**
	 * Searcher primitive operation.
	 * @param <T> Return type.
	 */
	abstract class Op<T> implements Function<IndexSearcher, T> {
		@Override
		public final T apply(IndexSearcher searcher) {
			try {
				return perform(searcher);
			} catch (SearchException e) {
				throw e;
			} catch (Exception e) {
				throw new IndexNotAvailableException(e);
			}
		}

		final <H> H map(IndexSearcher searcher, ScoreDoc sd, HighlightedQuery q, HitMapper<H> mapper) throws Exception {
			final int id = sd.doc;
			final float score = sd.score;
			final Document doc;
			FieldSelector selector = mapper.getFieldSelector();
			if (selector == null) {
				doc = searcher.doc(id);
			} else {
				doc = searcher.doc(id, selector);
			}
			final Hit hit = Hit.of(id, score, doc, q.getFragments(doc));
			return mapper.apply(hit);
		}

		abstract T perform(IndexSearcher searcher) throws Exception;
	}

}
