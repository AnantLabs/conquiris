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
package net.conquiris.api.search;

import net.derquinse.common.base.Disposable;

import org.apache.lucene.index.IndexReader;

import com.google.common.base.Supplier;

/**
 * Interface for managed index reader sources. Managed instances MUST NOT be closed manually.
 * Instead, they MUST be disposed when no longer needed. Implementations must be thread-safe.
 * @author Andres Rodriguez
 */
public interface ManagedReaderSource extends Supplier<Disposable<IndexReader>> {
	/**
	 * Returns a managed instance.
	 * @return A managed index reader.
	 * @throws IndexNotAvailableException if an error occurs.
	 */
	Disposable<IndexReader> get();
}
