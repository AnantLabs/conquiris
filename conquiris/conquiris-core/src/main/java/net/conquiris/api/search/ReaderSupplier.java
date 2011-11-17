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

import com.google.common.base.Supplier;

/**
 * Interface for index reader suppliers. Provided readers MUST be closed by the caller when no
 * longer needed. Implementations must be thread-safe.
 * @author Andres Rodriguez
 */
public interface ReaderSupplier extends Supplier<Reader> {
	/**
	 * Returns an index reader. Whether a new instance of a shared one is returned is
	 * implementation-dependant.
	 * @return An index reader.
	 * @throws IndexNotAvailableException if an error occurs.
	 */
	Reader get();
	
	/** Returns the number of successful requests done. */
	long getRequested();
}
