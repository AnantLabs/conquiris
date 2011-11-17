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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.conquiris.api.search.CountResult;
import net.conquiris.api.search.DocMapper;
import net.conquiris.api.search.Highlight;
import net.conquiris.api.search.IndexNotAvailableException;
import net.conquiris.api.search.ItemResult;
import net.conquiris.api.search.PageResult;
import net.conquiris.api.search.SearchException;
import net.conquiris.api.search.Searcher;
import net.conquiris.lucene.ScoredTotalHitCountCollector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;

import com.google.common.base.Function;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

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

	/** Fragment highlighting helper method. */
	private static Multimap<String, String> getFragments(Highlighter highlighter, Document doc, Highlight highlight) {
		final Multimap<String, String> fragments = ArrayListMultimap.create();
		final Map<String, Integer> fields = highlight.getFields();
		final Analyzer analyzer = highlight.getAnalyzer();
		for (Map.Entry<String, Integer> entry : fields.entrySet()) {
			final String field = entry.getKey();
			final Integer maxNumFragments = entry.getValue();
			if (maxNumFragments >= 0) {
				final String text = doc.get(field);

				if (text != null) {
					try {
						highlighter.setTextFragmenter(maxNumFragments > 0 ? new SimpleFragmenter() : new NullFragmenter());
						String[] fr = highlighter.getBestFragments(analyzer, field, text, maxNumFragments);

						if (fr != null && fr.length > 0) {
							fragments.putAll(field, Arrays.asList(fr));
						}
					} catch (IOException e) {
					} catch (InvalidTokenOffsetsException e) {
					}
				}
			}
		}
		// TODO: fix exceptions.

		return fragments;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.Searcher#getFirst(net.conquiris.api.search.DocMapper,
	 * org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.Sort,
	 * net.conquiris.api.search.Highlight)
	 */
	public final <T> ItemResult<T> getFirst(final DocMapper<T> mapper, final Query query, final @Nullable Filter filter,
			final @Nullable Sort sort, final @Nullable Highlight highlight) {
		return perform(new Op<ItemResult<T>>() {
			public ItemResult<T> perform(IndexSearcher searcher) throws Exception {
				final Stopwatch w = new Stopwatch().start();
				final TopDocs docs = getTopDocs(searcher, query, filter, sort, 1);
				if (docs.totalHits > 0) {
					final ScoreDoc sd = docs.scoreDocs[0];
					final Document doc = searcher.doc(sd.doc);
					final Multimap<String, String> fragments;
					if (highlight != null && !highlight.getFields().isEmpty()) {
						final Query rewrote = searcher.rewrite(query);
						final Highlighter highlighter = new Highlighter(highlight.getFormatter(), new QueryScorer(rewrote));
						fragments = getFragments(highlighter, doc, highlight);
					} else {
						fragments = ArrayListMultimap.create();
					}
					final float score = sd.score;
					final T item = mapper.map(sd.doc, score, doc, fragments);
					return ItemResult.found(docs.totalHits, score, w.elapsedMillis(), item);
				} else {
					return ItemResult.notFound(w.elapsedMillis());
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
	public final <T> PageResult<T> getPage(final DocMapper<T> mapper, final Query query, final int firstRecord,
			final int maxRecords, final @Nullable Filter filter, final @Nullable Sort sort,
			final @Nullable Highlight highlight) {
		return perform(new Op<PageResult<T>>() {
			public PageResult<T> perform(IndexSearcher searcher) throws Exception {
				final Stopwatch w = new Stopwatch().start();
				final int total = firstRecord + maxRecords;
				final TopDocs docs = getTopDocs(searcher, query, filter, sort, total);
				if (docs.totalHits > 0) {
					final int n = Math.min(total, docs.scoreDocs.length);
					final float score = docs.getMaxScore();
					if (n > firstRecord) {
						final List<T> items = new ArrayList<T>(n - firstRecord);
						final Query rewrote = searcher.rewrite(query);
						final Highlighter highlighter = new Highlighter(highlight.getFormatter(), new QueryScorer(rewrote));
						for (int i = firstRecord; i < n; i++) {
							final ScoreDoc sd = docs.scoreDocs[i];
							final Document doc = searcher.doc(sd.doc);
							final Multimap<String, String> fragments = getFragments(highlighter, doc, highlight);
							final T item = mapper.map(sd.doc, score, doc, fragments);
							items.add(item);
						}
						return PageResult.found(docs.totalHits, score, w.elapsedMillis(), firstRecord, items);
					} else {
						return PageResult.notFound(docs.totalHits, score, w.elapsedMillis(), firstRecord);
					}
				} else {
					return PageResult.notFound(w.elapsedMillis(), firstRecord);
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
				final Stopwatch w = new Stopwatch().start();
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
				return CountResult.of(collector.getTotalHits(), maxScore, w.elapsedMillis());
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

		abstract T perform(IndexSearcher searcher) throws Exception;
	}

}
