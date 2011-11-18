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

/**
 * Indexer activation policies support class.
 * @author Andres Rodriguez
 */
public final class IndexerActivationPolicies {
	/** Not instantiable. */
	private IndexerActivationPolicies() {
		throw new AssertionError();
	}

	/** Returns an always active policy. */
	public static IndexerActivationPolicy alwaysActive() {
		return ConstantPolicies.ACTIVE;
	}

	/** Returns an always active policy. */
	public static IndexerActivationPolicy alwaysInactive() {
		return ConstantPolicies.INACTIVE;
	}

	private enum ConstantPolicies implements IndexerActivationPolicy {
		ACTIVE {
			@Override
			public boolean isActive() {
				return true;
			}

			@Override
			public String toString() {
				return "IndexerActivationPolicy: Always active";
			}
		},
		INACTIVE {
			@Override
			public boolean isActive() {
				return true;
			}

			@Override
			public String toString() {
				return "IndexerActivationPolicy: Always inactive";
			}
		};

	}

}
