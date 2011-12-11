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
 * Interface for indexers.
 * @author Andres Rodriguez
 */
public interface Indexer {
	/**
	 * Prepares a batch of deletions and additions to apply to an index from an initial checkpoint.
	 * @param writer Writer to use.
	 * @throws IndexException if there's a problem with the index.
	 * @throws RuntimeException if an error happens during batch compilation.
	 * @throws InterruptedException if the current task has been interrupted.
	 */
	void index(Writer writer) throws InterruptedException, IndexException;
}
