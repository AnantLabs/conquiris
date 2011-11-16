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
import net.conquiris.api.search.IndexNotAvailableException;
import net.conquiris.api.search.Reader;
import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.UnmanagedReaderSource;
import net.derquinse.common.util.concurrent.RefCounted;
import net.derquinse.common.util.concurrent.Refs;

import org.apache.lucene.index.IndexReader;

import com.google.common.base.Stopwatch;

/**
 * Single directory unmanaged reader source implementation.
 * @author Andres Rodriguez
 */
final class DirectoryUnmanagedReaderSource implements ReaderSupplier {
	/** Directory. */
	private final UnmanagedReaderSource unmanaged;
	/** Reader hold time in ms. */
	private final long holdTime;
	/** Current watch. */
	private final Stopwatch watch;
	/** Current reader. */
	private IndexReader reader;
	/** Current managed reader. */
	private volatile RefCounted<IndexReader> managed;

	/**
	 * Constructor.
	 * @param unmanaged Unmanaged reader source.
	 * @param holdTime Reader hold time (ms). If less than 0, zero will be used.
	 */
	DirectoryUnmanagedReaderSource(UnmanagedReaderSource unmanaged, long holdTime) {
		this.unmanaged = checkNotNull(unmanaged, "The unmanaged reader source must be provided");
		this.holdTime = Math.max(0, holdTime);
		this.watch = this.holdTime > 0 ? new Stopwatch() : null;
	}

	public final Reader get() {
		while (true) {
			final IndexReader opened;
			if (managed == null) {
				opened = unmanaged.get();
			} else {
				opened = null;
			}
			final Reader ref = tryGet(opened);
			if (ref != null) {
				return ref;
			}
		}
	}

	/**
	 * Try get a new managed reference.
	 * @param opened Opened reader.
	 * @return The managed reference or null if the operation has to be retried.
	 */
	private synchronized Reader tryGet(IndexReader opened) {
		boolean used = false;
		try {
			if (managed == null) {
				if (opened != null) {
					start(opened);
					used = true;
				} else {
					return null;
				}
			} else if (watch == null || watch.elapsedMillis() > holdTime) {
				IndexReader reopened = reader.reopen();
				if (reopened != reader) {
					start(reopened);
				}
			}
			return null; // TODO;
		} catch (IndexNotAvailableException e) {
			shutdown();
			throw e;
		} catch (Exception e) {
			shutdown();
			throw new IndexNotAvailableException(e);
		} finally {
			if (opened != null && !used) {
				new ReaderCloser(opened).run();
			}
		}
	}

	private void shutdown() {
		if (managed != null) {
			managed.shutdown();
			managed = null;
		}
		if (reader != null) {
			reader = null;
		}
		if (watch != null) {
			watch.reset();
		}
	}

	private void start(IndexReader newReader) {
		shutdown();
		this.reader = newReader;
		this.managed = Refs.counted(newReader, new ReaderCloser(newReader));
		if (watch != null) {
			watch.start();
		}
	}

}
