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
package net.conquiris.search;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import net.conquiris.api.search.IndexNotAvailableException;
import net.conquiris.api.search.Reader;
import net.conquiris.api.search.ReaderSupplier;

import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;

/**
 * Single directory unmanaged reader supplier implementation.
 * @author Andres Rodriguez
 */
final class DirectoryReaderSupplier implements ReaderSupplier {
	/** Directory. */
	private final Directory directory;

	/**
	 * Constructor.
	 * @param directory Directory to use.
	 */
	DirectoryReaderSupplier(Directory directory) {
		this.directory = checkNotNull(directory, "The index directory must be provided");
	}

	public Reader get() {
		try {
			return tryGet();
		} catch (IndexNotAvailableException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexNotAvailableException(e);
		}
	}

	private Reader tryGet() throws IOException {
		try {
			final IndexReader reader = IndexReader.open(directory, true);
			return Reader.of(reader, true);
		} catch (IndexNotFoundException e) {
			return ReaderSuppliers.empty().get();
		}
	}

}
