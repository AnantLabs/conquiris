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

import javax.annotation.Nullable;

/**
 * Checkpoints support class.
 * @author Andres Rodriguez
 */
public final class Checkpoints {
	private Checkpoints() {
		throw new AssertionError();
	}

	/**
	 * Parses a checkpoint as a long.
	 * @param checkpoint Checkpoint to parse.
	 * @param fallback Fallback value.
	 * @return The checkpoint.
	 */
	public static long ofLong(@Nullable String checkpoint, long fallback) {
		if (checkpoint == null) {
			return fallback;
		}
		try {
			return Long.parseLong(checkpoint);
		} catch (NumberFormatException e) {
			return fallback;
		}
	}

	/**
	 * Parses a checkpoint as an int.
	 * @param checkpoint Checkpoint to parse.
	 * @param fallback Fallback value.
	 * @return The checkpoint.
	 */
	public static int ofInt(@Nullable String checkpoint, int fallback) {
		if (checkpoint == null) {
			return fallback;
		}
		try {
			return Integer.parseInt(checkpoint);
		} catch (NumberFormatException e) {
			return fallback;
		}
	}
}
