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

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.lucene.index.IndexReader;

import com.google.common.base.Supplier;

/**
 * A reference to an exact instance of an index reader.
 * @author Andres Rodriguez
 */
public final class Reader implements Supplier<IndexReader> {
	/** Index reader. */
	private final IndexReader reader;
	/** Whether the reader is reopenable. */
	private final boolean reopenable;

	/**
	 * Creates a new reader reference.
	 * @param reader Index reader.
	 * @param reopenable Whether the reader is reopenable.
	 * @return The requested reference.
	 */
	public static Reader of(IndexReader reader, boolean reopenable) {
		return new Reader(reader, reopenable);
	}

	/**
	 * Constructor.
	 * @param reader Index reader.
	 * @param reopenable Whether the reader is reopenable.
	 */
	private Reader(IndexReader reader, boolean reopenable) {
		this.reader = checkNotNull(reader, "The index reader must be provided");
		this.reopenable = reopenable;
	}

	/** Returns the index reader. */
	public IndexReader get() {
		return reader;
	}

	/** Returns whether the index is reopenable. */
	public boolean isReopenable() {
		return reopenable;
	}
}
