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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

/**
 * Conquiris index info at a certain commit point.
 * @author Andres Rodriguez
 */
@Immutable
public final class IndexInfo {
	/** Reserved commit properties prefix. */
	public static final String RESERVED_PREFIX = "cq:";
	/** Checkpoint property unqualified name. */
	private static final String CHECKPOINT_NAME = "checkpoint";
	/** Timestamp property unqualified name. */
	private static final String TIMESTAMP_NAME = "timestamp";
	/** Sequence property unqualified name. */
	private static final String SEQUENCE_NAME = "sequence";
	/** Checkpoint property. */
	public static final String CHECKPOINT = RESERVED_PREFIX + CHECKPOINT_NAME;
	/** Timestamp property. */
	public static final String TIMESTAMP = RESERVED_PREFIX + TIMESTAMP_NAME;
	/** Sequence property. */
	public static final String SEQUENCE = RESERVED_PREFIX + SEQUENCE_NAME;
	private static final Predicate<String> IS_RESERVED = new Predicate<String>() {
		@Override
		public boolean apply(String input) {
			return input != null && input.startsWith(RESERVED_PREFIX);
		}
	};
	/** User property entry filter. */
	private static final Predicate<Entry<String, String>> IS_USER_PROPERTY = new Predicate<Entry<String, String>>() {
		@Override
		public boolean apply(Entry<String, String> input) {
			String k = input.getKey();
			return k != null && input.getValue() != null && !IS_RESERVED.apply(k);
		}
	};

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

	/** Last commit checkpoint. */
	private final String checkpoint;
	/** Last commit timestamp. */
	private final long timestamp;
	/** Last commit sequence. */
	private final long sequence;
	/** User properties. */
	private final ImmutableMap<String, String> properties;

	private IndexInfo(String checkpoint, long timestamp, long sequence, Map<String, String> properties) {
		checkArgument(timestamp >= 0);
		checkArgument(sequence >= 0);
		checkNotNull(properties);
		checkArgument(Iterables.all(properties.entrySet(), isUserProperty()));
		this.checkpoint = checkpoint;
		this.timestamp = timestamp;
		this.sequence = sequence;
		this.properties = ImmutableMap.copyOf(properties);
	}

	/** Returns the checkpoint. */
	public String getCheckpoint() {
		return checkpoint;
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
	public String toString() {
		return Objects.toStringHelper(this).add(CHECKPOINT_NAME, checkpoint).add(TIMESTAMP_NAME, timestamp)
				.add(SEQUENCE_NAME, sequence).add("properties", properties).toString();
	}

}
