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

import java.io.IOException;

import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TotalHitCountCollector;

/**
 * A collector that counts the number of hits and keeps track of the maximum score.
 * @author Andres Rodriguez
 */
public class ScoredTotalHitCountCollector extends TotalHitCountCollector {
	private Scorer scorer;
	private float maxScore = 0.0f;

	/** Constructor. */
	public ScoredTotalHitCountCollector() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.search.TotalHitCountCollector#collect(int)
	 */
	@Override
	public void collect(int doc) {
		super.collect(doc);
		if (scorer != null) {
			try {
				maxScore = Math.max(maxScore, scorer.score());
			} catch (IOException e) {
				// TODO
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.search.TotalHitCountCollector#setScorer(org.apache.lucene.search.Scorer)
	 */
	@Override
	public void setScorer(Scorer scorer) {
		super.setScorer(scorer);
		this.scorer = scorer;
	}

	/**
	 * Returns the maximum collected score.
	 * @return The maximum collected score.
	 */
	public final float getMaxScore() {
		return maxScore;
	}
}
