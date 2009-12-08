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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.conquiris.api.Grouping;

import org.apache.lucene.document.Document;
import org.testng.v6.Maps;

import com.google.common.collect.ImmutableMap;

/**
 * Grouping builder.
 * @author Andres Rodriguez
 * @param <T> Payload type.
 */
final class GroupingBuilder<T> {
	/** Number of levels. */
	private final int levels;
	/** Current level. */
	private final int level;
	/** Number of hits. */
	private int hits = 1;
	/** Payload. */
	private T payload;
	/** Next levels. */
	private final Map<String, GroupingBuilder<T>> next;

	/**
	 * Constructor
	 * @param path Hit path.
	 * @param level Current level.
	 * @param document Lucene document.
	 * @param g Payload calculator.
	 */
	GroupingBuilder(List<String> path, int level, Document document, Group<T> g) {
		checkNotNull(path);
		checkArgument(!path.isEmpty());
		this.levels = path.size();
		checkArgument(level <= this.levels);
		this.level = level;
		if (level == levels) {
			this.payload = g != null ? g.perform(path, document, 1, null) : null;
			this.next = null;
		} else {
			this.payload = null;
			this.next = Maps.newHashMap();
			this.next.put(path.get(level), new GroupingBuilder<T>(path, level + 1, document, g));
		}
	}

	/**
	 * Adds a new hit.
	 * @param path Hit path.
	 * @param document Lucene document.
	 * @param g Payload calculator.
	 */
	void add(List<String> path, Document document, Group<T> g) {
		checkNotNull(path);
		checkArgument(levels == path.size());
		hits++;
		if (next == null) {
			this.payload = g != null ? g.perform(path, document, hits, null) : null;
		} else {
			final String key = path.get(level);
			GroupingBuilder<T> gb = next.get(key);
			if (gb == null) {
				next.put(key, new GroupingBuilder<T>(path, level + 1, document, g));
			} else {
				gb.add(path, document, g);
			}
		}
	}

	/**
	 * Builds a new grouping.
	 * @return A new grouping.
	 */
	Grouping<T> build() {
		if (next == null) {
			return new GroupingImpl.Leaf<T>(hits, payload);
		}
		ImmutableMap.Builder<String, Grouping<T>> builder = ImmutableMap.builder();
		for (Entry<String, GroupingBuilder<T>> entry : next.entrySet()) {
			builder.put(entry.getKey(), entry.getValue().build());
		}
		return new GroupingImpl.Branch<T>(hits, builder.build());
	}

	/**
	 * Builds a new grouping.
	 * @return A new grouping.
	 */
	Grouping<Integer> buildCounting() {
		if (next == null) {
			return new GroupingImpl.CountingLeaf(hits);
		}
		ImmutableMap.Builder<String, Grouping<Integer>> builder = ImmutableMap.builder();
		for (Entry<String, GroupingBuilder<T>> entry : next.entrySet()) {
			builder.put(entry.getKey(), entry.getValue().buildCounting());
		}
		return new GroupingImpl.Branch<Integer>(hits, builder.build());
	}

}
