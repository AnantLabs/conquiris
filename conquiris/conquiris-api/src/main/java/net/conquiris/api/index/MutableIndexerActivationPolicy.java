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

import javax.annotation.concurrent.ThreadSafe;

/**
 * Indexer activation policy based on a mutable boolean value.
 * @author Andres Rodriguez.
 */
@ThreadSafe
public final class MutableIndexerActivationPolicy implements IndexerActivationPolicy {
	/** Current value. */
	private volatile boolean active;

	/**
	 * Creates a new policy.
	 * @param active Initial value.
	 * @return The requested policy.
	 */
	public static MutableIndexerActivationPolicy create(boolean active) {
		return new MutableIndexerActivationPolicy(active);
	}

	/**
	 * Creates a new policy with an initial value of true.
	 * @return The requested policy.
	 */
	public static MutableIndexerActivationPolicy active() {
		return create(true);
	}

	/**
	 * Creates a new policy with an initial value of false.
	 * @return The requested policy.
	 */
	public static MutableIndexerActivationPolicy inactive() {
		return create(true);
	}

	/**
	 * Constructor.
	 * @param active Initial value.
	 */
	private MutableIndexerActivationPolicy(boolean active) {
		this.active = active;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.IndexerActivationPolicy#isActive()
	 */
	@Override
	public boolean isActive() {
		return active;
	}
}
