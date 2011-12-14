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

import net.derquinse.common.meta.MetaProperty;

/**
 * Interface for the last known index status property.
 * @author Andres Rodriguez.
 */
public interface IndexStatusProperty {
	/** Property. */
	MetaProperty<IndexStatusProperty, IndexStatus> INDEX_STATUS = new MetaProperty<IndexStatusProperty, IndexStatus>(
			"indexStatus", true) {
		@Override
		public IndexStatus apply(IndexStatusProperty input) {
			return input.getIndexStatus();
		}
	};

	/**
	 * Returns the last known index status.
	 */
	IndexStatus getIndexStatus();
}
