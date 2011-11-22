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

/**
 * Interface for managed index reader suppliers. Managed reader suppliers try to reoped reopenable
 * readers and may reuse readers without trying to reopen. Implementations must be thread-safe.
 * @author Andres Rodriguez
 */
public interface ManagedReaderSupplier extends ReaderSupplier {
	/** Returns the number of successful requests that reused a previously opened reader. */
	long getReused();

	/** Returns the number of successful requests that where server reopening a reader. */
	long getReopened();

	/** Dispose any acquired resources. */
	void dispose();
}
