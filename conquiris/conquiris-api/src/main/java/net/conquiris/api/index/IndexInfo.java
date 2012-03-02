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
package net.conquiris.api.index;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

/**
 * Conquiris index info at a certain commit point.
 * @author Andres Rodriguez
 */
@Immutable
public final class IndexInfo {
	/** Reserved commit properties prefix. */
	public static final String RESERVED_PREFIX = "cq:";
	/** Checkpoint property unqualified name. */
	public static final String CHECKPOINT_NAME = "checkpoint";
	/** Target checkpoint property unqualified name. */
	public static final String TARGET_CHECKPOINT_NAME = "targetCheckpoint";
	/** Timestamp property unqualified name. */
	public static final String TIMESTAMP_NAME = "timestamp";
	/** Sequence property unqualified name. */
	public static final String SEQUENCE_NAME = "sequence";
	/** Checkpoint property. */
	public static final String CHECKPOINT = RESERVED_PREFIX + CHECKPOINT_NAME;
	/** Target checkpoint property. */
	public static final String TARGET_CHECKPOINT = RESERVED_PREFIX + TARGET_CHECKPOINT_NAME;
	/** Timestamp property. */
	public static final String TIMESTAMP = RESERVED_PREFIX + TIMESTAMP_NAME;
	/** Sequence property. */
	public static final String SEQUENCE = RESERVED_PREFIX + SEQUENCE_NAME;

	/** Returns whether a property name is reserved. */
	public static boolean isReserved(String key) {
		return key != null && key.startsWith(RESERVED_PREFIX);
	}

	/** Reserved predicate. */
	private enum IsReserved implements Predicate<String> {
		INSTANCE;

		@Override
		public boolean apply(String input) {
			return isReserved(input);
		}

		@Override
		public String toString() {
			return "Reserved property key predicate";
		}

	}

	/** Returns the reserved property name predicate. */
	public static Predicate<String> isReserved() {
		return IsReserved.INSTANCE;
	}

	/** Returns whether the provided entry is a valid user property. */
	public static boolean isUserProperty(String key, String value) {
		return key != null && value != null && !isReserved(key);
	}

	/** Returns whether the provided entry is a valid user property. */
	public static boolean isUserProperty(Entry<String, String> entry) {
		return entry != null && isUserProperty(entry.getKey(), entry.getValue());
	}

	/** User property predicate. */
	private enum IsUserProperty implements Predicate<Entry<String, String>> {
		INSTANCE;

		@Override
		public boolean apply(Entry<String, String> input) {
			return isUserProperty(input);
		}

		@Override
		public String toString() {
			return "Valid user property predicate";
		}

	}

	/** Returns the valid user property predicate. */
	public static Predicate<Entry<String, String>> isUserProperty() {
		return IsUserProperty.INSTANCE;
	}

	/** Empty object. */
	private static final IndexInfo EMPTY = IndexInfo.of(null, null, 0, 0, 0, ImmutableMap.<String, String> of());

	/** Returns the empty object. */
	public static IndexInfo empty() {
		return EMPTY;
	}

	/**
	 * Creates a new index info object.
	 * @param checkpoint Checkpoint.
	 * @param targetCheckpoint Target checkpoint.
	 * @param documents Number of documents. Must be >= 0.
	 * @param timestamp Commit timestamp. Must be >= 0.
	 * @param sequence Commit sequence. Must be >= 0.
	 * @param properties User properties. Must be non-null and contain no reserved properties.
	 * @return The created object.
	 * @throws IllegalArgumentException if any of the arguments is invalid.
	 */
	public static IndexInfo of(@Nullable String checkpoint, @Nullable String targetCheckpoint, int documents,
			long timestamp, long sequence, Map<String, String> properties) {
		return new IndexInfo(checkpoint, targetCheckpoint, documents, timestamp, sequence, properties);
	}

	private static long safe2Long(String s) {
		if (s == null) {
			return 0L;
		}
		try {
			return Math.max(0L, Long.parseLong(s));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Creates a new index info object from a string map. Reserved properties are extracted with
	 * default values provided if needed. Invalid user properties are filtered out.
	 * @param documents Number of documents. Must be >= 0.
	 * @param data Commit data.
	 * @return The created object.
	 */
	public static IndexInfo fromMap(int documents, Map<String, String> data) {
		if (data == null || data.isEmpty()) {
			return of(null, null, documents, 0L, 0L, ImmutableMap.<String, String> of());
		}
		return of(data.get(CHECKPOINT), data.get(TARGET_CHECKPOINT), documents, safe2Long(data.get(TIMESTAMP)),
				safe2Long(data.get(SEQUENCE)), Maps.filterEntries(data, isUserProperty()));
	}

	/** Last commit checkpoint. */
	private final String checkpoint;
	/** Last known target checkpoint. */
	private final String targetCheckpoint;
	/** Number of documents. */
	private final int documents;
	/** Last commit timestamp. */
	private final long timestamp;
	/** Last commit sequence. */
	private final long sequence;
	/** User properties. */
	private final ImmutableMap<String, String> properties;

	private IndexInfo(String checkpoint, String targetCheckpoint, int documents, long timestamp, long sequence,
			Map<String, String> properties) {
		checkArgument(documents >= 0);
		checkArgument(timestamp >= 0);
		checkArgument(sequence >= 0);
		checkNotNull(properties);
		checkArgument(Iterables.all(properties.entrySet(), isUserProperty()));
		this.checkpoint = checkpoint;
		this.targetCheckpoint = targetCheckpoint;
		this.documents = documents;
		this.timestamp = timestamp;
		this.sequence = sequence;
		this.properties = ImmutableMap.copyOf(properties);
	}

	/** Returns a basic version without user properties. */
	public IndexInfo asBasic() {
		if (properties.isEmpty()) {
			return this;
		}
		return new IndexInfo(checkpoint, targetCheckpoint, documents, timestamp, sequence,
				ImmutableMap.<String, String> of());
	}

	/** Returns the checkpoint. */
	public String getCheckpoint() {
		return checkpoint;
	}

	/** Returns the last known target checkpoint. */
	public String getTargetCheckpoint() {
		return targetCheckpoint;
	}

	/** Returns the number of documents. */
	public int getDocuments() {
		return documents;
	}

	/** Returns the commit timestamp. */
	public long getTimestamp() {
		return timestamp;
	}

	/** Returns the commit sequence. */
	public long getSequence() {
		return sequence;
	}

	/** Returns the commit properties. */
	public ImmutableMap<String, String> getProperties() {
		return properties;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(checkpoint, targetCheckpoint, documents, timestamp, sequence, properties);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof IndexInfo) {
			IndexInfo other = (IndexInfo) obj;
			return this.documents == other.documents && this.timestamp == other.timestamp && this.sequence == other.sequence
					&& equal(checkpoint, other.checkpoint) && equal(targetCheckpoint, other.targetCheckpoint)
					&& equal(properties, other.properties);
		}
		return false;
	}

	@Override
	public String toString() {
		ToStringHelper h = Objects.toStringHelper(this).add(CHECKPOINT_NAME, checkpoint).add("documents", documents)
				.add(TARGET_CHECKPOINT_NAME, targetCheckpoint).add(TIMESTAMP_NAME, timestamp).add(SEQUENCE_NAME, sequence);
		if (!properties.isEmpty()) {
			h.add("properties", properties);
		}
		return h.toString();
	}

}
