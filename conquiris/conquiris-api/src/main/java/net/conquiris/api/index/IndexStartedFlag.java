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
 * Interface for the index started flag.
 * @author Andres Rodriguez.
 */
public interface IndexStartedFlag {
	/** Property. */
	MetaFlag<IndexStartedFlag> INDEX_STARTED = new MetaFlag<IndexStartedFlag>("indexStarted") {
		@Override
		public boolean apply(IndexStartedFlag input) {
			return input.isIndexStarted();
		}
	};

	/**
	 * Returns true if and only if the service is started.
	 */
	boolean isIndexStarted();
}
