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

import net.derquinse.common.meta.MetaFlag;

/**
 * Interface for the index active flag. An index is active if and only if the service is started and
 * the current policy is to keep the index active.
 * @author Andres Rodriguez.
 */
public interface IndexActiveFlag {
	/** Property. */
	MetaFlag<IndexActiveFlag> INDEX_ACTIVE = new MetaFlag<IndexActiveFlag>("indexActive") {
		@Override
		public boolean apply(IndexActiveFlag input) {
			return input.isIndexActive();
		}
	};

	/**
	 * Returns true if and only if the service is started and the current policy is to keep the index
	 * active.
	 */
	boolean isIndexActive();
}
