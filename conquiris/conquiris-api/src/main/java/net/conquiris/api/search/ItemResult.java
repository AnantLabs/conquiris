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
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.annotation.Nullable;

import com.google.common.base.Objects;

/**
 * Result representing a single item.
 * @param <T> Item type.
 * @author Andres Rodriguez
 */
public final class ItemResult<T> extends Result {
	/** Serial UID. */
	private static final long serialVersionUID = 2086856876372169518L;

	/** Empty item. */
	private static final ItemResult<Object> EMPTY = new ItemResult<Object>(0, 0.0f, 0L, null);

	/** Item returned by the query. */
	private final T item;

	/**
	 * Returns the empty item.
	 * @return The empty item.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ItemResult<T> empty() {
		return (ItemResult<T>) EMPTY;
	}

	/**
	 * Returns a not found item.
	 * @param time Time taken by the query (ms).
	 */
	public static <T> ItemResult<T> notFound(long time) {
		return new ItemResult<T>(0, 0, time, null);
	}

	/**
	 * Returns a found item.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 * @param item Found item.
	 */
	public static <T> ItemResult<T> found(int totalHits, float maxScore, long time, T item) {
		return new ItemResult<T>(totalHits, maxScore, time, item);
	}

	/**
	 * Constructor.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 * @param item Found item.
	 */
	private ItemResult(final int totalHits, final float maxScore, final long time, @Nullable T item) {
		super(totalHits, maxScore, time);
		if (totalHits > 0) {
			checkNotNull(item, "No item provided");
		} else {
			checkArgument(item == null, "Superfluous item provided");
		}
		this.item = item;
	}

	/**
	 * Returns the item. Will be null iff totalHits = 0
	 * @return The found item.
	 */
	public T getItem() {
		return item;
	}

	@Override
	public boolean equals(Object obj) {
		final ItemResult<?> other = equalsResult(obj, ItemResult.class);
		if (other != null) {
			return Objects.equal(this.item, other.item);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), item);
	}

	// =================================================================
	// Serialization proxy

	private static class SerializationProxy<T> implements Serializable {
		/** Serial UID. */
		private static final long serialVersionUID = -7598986838377254525L;
		/** Total hits of the query. */
		private final int totalHits;
		/** Maximum score. */
		private final float maxScore;
		/** Time taken by the query (ms). */
		private final long time;
		/** Item returned by the query. */
		private final T item;

		public SerializationProxy(ItemResult<T> r) {
			this.totalHits = r.getTotalHits();
			this.maxScore = r.getMaxScore();
			this.time = r.getTime();
			this.item = r.getItem();
		}

		private Object readResolve() {
			return new ItemResult<T>(totalHits, maxScore, time, item);
		}
	}

	private Object writeReplace() {
		return new SerializationProxy<T>(this);
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required");
	}

}
