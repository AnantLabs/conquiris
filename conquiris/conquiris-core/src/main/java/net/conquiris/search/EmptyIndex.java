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

import java.io.IOException;
import java.lang.ref.WeakReference;

import net.conquiris.api.search.IndexNotAvailableException;
import net.conquiris.api.search.Reader;
import net.conquiris.lucene.Conquiris;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * Empty RAM-based index.
 * @author Andres Rodriguez
 */
final class EmptyIndex extends AbstractReaderSupplier {
	/** Instance. */
	private static volatile WeakReference<EmptyIndex> instance;

	/** Returns the shared instance. */
	static EmptyIndex getInstance() {
		try {
			final WeakReference<EmptyIndex> r = instance;
			if (r != null) {
				EmptyIndex i = r.get();
				if (i != null) {
					return i;
				}
			}
			return create();
		} catch (IOException e) {
			// Should not happen.
			throw new IndexNotAvailableException(e);
		}
	}

	/** Returns a new initalized RAM-based empty directory. */
	static Directory newDirectory() {
		try {
			return new EmptyIndex().directory;
		} catch (IOException e) {
			// Should not happen.
			throw new IndexNotAvailableException(e);
		}
	}

	private static synchronized EmptyIndex create() throws IOException {
		if (instance != null) {
			EmptyIndex i = instance.get();
			if (i != null) {
				return i;
			}
		}
		EmptyIndex i = new EmptyIndex();
		instance = new WeakReference<EmptyIndex>(i);
		return i;
	}

	/** Directory. */
	private final Directory directory;

	/** Constructor. */
	private EmptyIndex() throws IOException {
		this.directory = new RAMDirectory();
		IndexWriter w = new IndexWriter(directory, Conquiris.writerConfig());
		w.close();
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.search.AbstractReaderSupplier#doGet()
	 */
	@Override
	Reader doGet() throws IOException {
		return Reader.of(IndexReader.open(getInstance().directory), false);
	}

}
