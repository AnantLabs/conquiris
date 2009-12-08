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

/**
 * Basic skeletal class for results that stores elapsed timeand total number of hits.
 * @author Andres Rodriguez
 */
public abstract class AbstractCountingResult extends AbstractResult {
	/** The total number of hits of the performed query. */
	private final int totalHits;

	/**
	 * Constructor.
	 * @param time The elapsed time in milliseconds.
	 * @param totalHits The total number of hits of the performed query.
	 */
	protected AbstractCountingResult(long time, int totalHits) {
		super(time);
		this.totalHits = totalHits;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.Result#getTotalHits()
	 */
	@Override
	public final int getTotalHits() {
		return totalHits;
	}

}
