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

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Base class for search results.
 * @author Andres Rodriguez
 */
public abstract class Result implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 2164177824714263309L;

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
	Result(final int totalHits, final float maxScore, final long time) {
		checkArgument(totalHits >= 0, "The number of hits must be >= 0");
		checkArgument(time >= 0, "The query time must be >= 0");
		this.totalHits = totalHits;
		this.maxScore = maxScore;
		this.time = time;
	}

	/** Returns the maximum score of the results of the query. */
	public final float getMaxScore() {
		return maxScore;
	}

	/** Returns the time taken by the query (ms). */
	public final long getTime() {
		return time;
	}

	/** Returns the total hits of the query. */
	public final int getTotalHits() {
		return totalHits;
	}

	final <T extends Result> T equalsResult(Object obj, Class<T> klass) {
		if (obj != null && klass.equals(obj.getClass())) {
			@SuppressWarnings("unchecked")
			final T r = (T) obj;
			if (totalHits == r.totalHits && maxScore == r.maxScore && time == r.time) {
				return r;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return equalsResult(obj, Result.class) != null;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(totalHits, maxScore, time);
	}
}
