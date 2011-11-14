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
package net.conquiris.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

/**
 * A collector that simply counts the number of hits. This collector is not thread-safe and may not
 * be reused in different searches.
 * @author Andres Rodriguez
 */
public class CountingCollector extends Collector {
	private int totalHits = 0;

	/** Constructor. */
	public CountingCollector() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.search.Collector#acceptsDocsOutOfOrder()
	 */
	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.search.Collector#collect(int)
	 */
	@Override
	public void collect(int doc) throws IOException {
		totalHits++;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.search.Collector#setScorer(org.apache.lucene.search .Scorer)
	 */
	@Override
	public void setScorer(Scorer scorer) throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.search.Collector#setNextReader(org.apache.lucene.index .IndexReader,
	 * int)
	 */
	@Override
	public void setNextReader(IndexReader reader, int docBase) throws IOException {
	}

	/**
	 * Returns the total number of hits.
	 * @return The total number of hits.
	 */
	public final int getTotalHits() {
		return totalHits;
	}
}
