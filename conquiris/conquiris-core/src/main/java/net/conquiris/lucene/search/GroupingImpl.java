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

import java.util.Map;

import net.conquiris.api.Grouping;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;

/**
 * Grouping implementation.
 * @author Andres Rodriguez
 * @param <T> Payload type.
 */
abstract class GroupingImpl<T> extends ForwardingMap<String, Grouping<T>> implements Grouping<T> {
	/** Number of hits. */
	private final int hits;

	/**
	 * Constructor
	 * @param hits Number of hits.
	 */
	GroupingImpl(int hits) {
		this.hits = hits;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.Grouping#getHits()
	 */
	@Override
	public int getHits() {
		return hits;
	}

	static final class Branch<T> extends GroupingImpl<T> {
		/** Next levels. */
		private final ImmutableMap<String, Grouping<T>> next;

		/**
		 * Constructor
		 * @param hits Number of hits.
		 * @param next Next levels.
		 */
		Branch(int hits, Map<String, Grouping<T>> next) {
			super(hits);
			this.next = ImmutableMap.copyOf(next);
		}

		/*
		 * (non-Javadoc)
		 * @see net.conquiris.api.Grouping#isLast()
		 */
		@Override
		public boolean isLast() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see net.conquiris.api.Grouping#getPayload()
		 */
		@Override
		public T getPayload() {
			throw new IllegalStateException("This is not the last grouping level");
		}

		@Override
		protected Map<String, Grouping<T>> delegate() {
			return next;
		}

	}

	static abstract class AbstractLeaf<T> extends GroupingImpl<T> {
		/**
		 * Constructor
		 * @param hits Number of hits.
		 */
		AbstractLeaf(int hits) {
			super(hits);
		}

		/*
		 * (non-Javadoc)
		 * @see net.conquiris.api.Grouping#isLast()
		 */
		@Override
		public final boolean isLast() {
			return true;
		}

		@Override
		protected final Map<String, Grouping<T>> delegate() {
			return ImmutableMap.of();
		}

	}

	static final class Leaf<T> extends AbstractLeaf<T> {
		/** Payload. */
		private final T payload;

		/**
		 * Constructor
		 * @param hits Number of hits.
		 * @param payload Payload.
		 */
		Leaf(int hits, T payload) {
			super(hits);
			this.payload = payload;
		}

		/*
		 * (non-Javadoc)
		 * @see net.conquiris.api.Grouping#getPayload()
		 */
		@Override
		public T getPayload() {
			return payload;
		}
	}

	static final class CountingLeaf extends AbstractLeaf<Integer> {
		/**
		 * Constructor
		 * @param hits Number of hits.
		 */
		CountingLeaf(int hits) {
			super(hits);
		}

		/*
		 * (non-Javadoc)
		 * @see net.conquiris.api.Grouping#getPayload()
		 */
		@Override
		public Integer getPayload() {
			return getHits();
		}
	}

}
