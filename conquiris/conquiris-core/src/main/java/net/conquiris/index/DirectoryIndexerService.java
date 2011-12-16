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
package net.conquiris.index;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.GuardedBy;

import net.conquiris.api.index.IndexInfo;
import net.conquiris.api.index.IndexStatus;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.google.common.base.Supplier;
import com.google.common.io.Closeables;

/**
 * Default directory-based indexer service implementation.
 * @author Andres Rodriguez
 */
public final class DirectoryIndexerService extends AbstractLocalIndexerService {
	/** Lucene directory to use. */
	private final Directory directory;
	/** Writer configuration supplier. */
	private final Supplier<IndexWriterConfig> configSupplier;
	/** Last known index status. */
	private volatile IndexStatus indexStatus = IndexStatus.OK;
	/** Service lock. */
	private final Lock lock = new ReentrantLock();
	/** Service session. */
	@GuardedBy("lock")
	private volatile Session session;

	public DirectoryIndexerService(Directory directory, Supplier<IndexWriterConfig> configSupplier) {
		this.directory = checkNotNull(directory, "The directory to use must be provided");
		this.configSupplier = checkNotNull(configSupplier, "The index writer configuration supplier must be provided");
	}

	@Override
	public IndexInfo getIndexInfo() {
		return new GetIndexInfo().get();
	}

	@Override
	public IndexStatus getIndexStatus() {
		return indexStatus;
	}

	@Override
	public void start() {
		lock.lock();
		try {
			if (session != null) {
				return; // already started
			}
			session = new Session();
			// TODO: schedule task
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void stop() {
		lock.lock();
		try {
			final Session s = session;
			if (s == null) {
				return; // already stopped
			}
			session = null;
			s.shutdown();
		} finally {
			lock.unlock();
		}
	}

	

	void template() {
		lock.lock();
		try {
			// TODO Auto-generated method stub
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void setCheckpoint(String checkpoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reindex() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isIndexStarted() {
		return session != null;
	}

	/** Wrapped Lucene operation. */
	private abstract class Wrapped<T> {
		private String message;
		private T fallback;

		Wrapped(String message, T fallback) {
			this.message = checkNotNull(message);
			this.fallback = fallback;
		}

		/** Called when there is an exception obtaining the index information. */
		private void error(Exception e, IndexStatus status) {
			log().error(e, message);
			if (status != null) {
				indexStatus = status;
			}
		}

		final T get() {
			try {
				return run();
			} catch (LockObtainFailedException e) {
				error(e, IndexStatus.LOCKED);
			} catch (CorruptIndexException e) {
				error(e, IndexStatus.CORRUPT);
			} catch (IOException e) {
				error(e, IndexStatus.IOERROR);
			} catch (RuntimeException e) {
				error(e, IndexStatus.ERROR);
			}
			return fallback;
		}

		abstract T run() throws IOException;
	}

	/** Get index info. */
	private final class GetIndexInfo extends Wrapped<IndexInfo> {
		GetIndexInfo() {
			super("Unable to get index info", IndexInfo.empty());
		}

		@Override
		IndexInfo run() throws IOException {
			if (!IndexReader.indexExists(directory)) {
				return IndexInfo.empty();
			}
			final IndexReader reader = IndexReader.open(directory);
			try {
				return IndexInfo.fromMap(reader.getCommitUserData());
			} finally {
				Closeables.closeQuietly(reader);
			}
		}
	}

	/** Open writer. */
	private final class OpenWriter extends Wrapped<IndexWriter> {
		OpenWriter() {
			super("Unable to open index writer", null);
		}

		@Override
		IndexWriter run() throws IOException {
			IndexWriterConfig config = checkNotNull(configSupplier.get(), "Null writer config supplied");
			return new IndexWriter(directory, config);
		}
	}
	
	/** Service session. */
	private final class Session {
		/** Executor. */
		private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		/** Index writer. */
		@GuardedBy("lock")
		private IndexWriter indexWriter = null;
		/** Whether the session is active. */
		@GuardedBy("lock")
		private boolean active = true;

		IndexWriter getOpenWriter() {
			lock.lock();
			try {
				if (!active) {
					return null;
				}
				if (indexWriter == null) {
					indexWriter = new OpenWriter().get();
				}
				return indexWriter;
			} finally {
				lock.unlock();
			}
		}
		
		void closeWriter() {
			lock.lock();
			try {
				if (indexWriter != null) {
					Closeables.closeQuietly(indexWriter);
					indexWriter = null;
				}
			} finally {
				lock.unlock();
			}
		}
		
		void shutdown() {
			active = false;
			executor.shutdownNow();
			closeWriter();
		}
	
	}

	/** Base task. */
	private abstract class Task implements Runnable {
		private final Session session;
		
		public Task(Session session) {
			this.session = checkNotNull(session);
		}
		
		@Override
		public void run() {
			lock.lock();
			try {
				boolean ok = true;
				final IndexWriter indexWriter = session.getOpenWriter();
			} finally {
				lock.unlock();
			}

			// TODO Auto-generated method stub

		}

		// abstract IndexStatus

	}

}