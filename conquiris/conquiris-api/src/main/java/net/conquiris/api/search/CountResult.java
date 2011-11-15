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

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Count result.
 * @author Andres Rodriguez
 */
public final class CountResult extends Result {
	/** Serial UID. */
	private static final long serialVersionUID = 4419889292418874612L;
	/** Empty count. */
	private static final CountResult EMPTY = of(0, 0.0f, 0);

	/**
	 * Result for a count query.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 */
	public static CountResult of(int totalHits, float maxScore, long time) {
		return new CountResult(totalHits, maxScore, time);
	}

	/** Empty count. */
	public static CountResult empty() {
		return EMPTY;
	}

	/**
	 * Constructor. Subclasses only allowed in the same package.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 */
	private CountResult(final int totalHits, final float maxScore, final long time) {
		super(totalHits, maxScore, time);
	}

	@Override
	public boolean equals(Object obj) {
		return equalsResult(obj, CountResult.class) != null;
	}

	// =================================================================
	// Serialization proxy

	private static class SerializationProxy implements Serializable {
		/** Serial UID. */
		private static final long serialVersionUID = 3058933937818618604L;
		/** Total hits of the query. */
		private final int totalHits;
		/** Maximum score. */
		private final float maxScore;
		/** Time taken by the query (ms). */
		private final long time;

		public SerializationProxy(CountResult r) {
			this.totalHits = r.getTotalHits();
			this.maxScore = r.getMaxScore();
			this.time = r.getTime();
		}

		private Object readResolve() {
			return new CountResult(totalHits, maxScore, time);
		}
	}

	private Object writeReplace() {
		return new SerializationProxy(this);
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required");
	}

}
