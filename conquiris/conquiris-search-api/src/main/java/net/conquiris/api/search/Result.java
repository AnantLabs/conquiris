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
package net.conquiris.api.search;

import java.io.Serializable;

/**
 * Base class for search results.
 * @author Andres Rodriguez
 */
public class Result implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 8616534195601811296L;
	/** Total hits of the query. */
	private final int totalHits;
	/** Maximum score. */
	private final float maxScore;
	/** Time taken by the query (ms). */
	private final long time;

	/**
	 * Constructor.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 */
	public Result(final int totalHits, final float maxScore, final long time) {
		this.totalHits = totalHits;
		this.maxScore = maxScore;
		this.time = time;
	}

	public final float getMaxScore() {
		return maxScore;
	}

	public final long getTime() {
		return time;
	}

	public final int getTotalHits() {
		return totalHits;
	}
}
