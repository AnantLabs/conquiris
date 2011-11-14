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
import org.apache.lucene.util.OpenBitSet;

/**
 * A collector that simply stores the doc ids of the hits not taking scores into account. The hits
 * are stored in a OpenBitSet. This collector is not thread-safe and may not be reused in different
 * searches.
 * @author Andres Rodriguez
 */
public class AllCollector extends Collector {
	/** Doc Ids. */
	private final OpenBitSet docIds = new OpenBitSet();
	/** Current reader base. */
	private int docBase = 0;

	/** Constructor. */
	public AllCollector() {
	}

	/**
	 * Returns the doc ids of the hits.
	 * @return A bit set with the doc ids.
	 */
	public OpenBitSet getDocIds() {
		return docIds;
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
		docIds.set(docBase + doc);
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
		this.docBase = docBase;
	}
}
