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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import net.conquiris.api.index.IndexInfo;
import net.conquiris.api.index.IndexStatus;
import net.conquiris.api.index.Indexer;
import net.conquiris.api.index.WriterResult;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.google.common.base.Supplier;
import com.google.common.io.Closeables;

/**
 * Default directory-based indexer service implementation.
 * @author Andres Rodriguez
 */
public final class DirectoryIndexerService extends AbstractLocalIndexerService {
	/** Indexer to use. */
	private final Indexer indexer;
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

	public DirectoryIndexerService(Indexer indexer, Directory directory, Supplier<IndexWriterConfig> configSupplier) {
		this.indexer = checkNotNull(indexer, "The indexer to use must be provided");
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
			new Task(session, 0L);
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

	@Override
	public void setCheckpoint(String checkpoint) {
		lock.lock();
		try {
			final Session s = session;
			if (s == null) {
				return; // already stopped
			}
			new Task(s, checkpoint);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void reindex() {
		lock.lock();
		try {
			final Session s = session;
			if (s == null) {
				return; // already stopped
			}
			new Task(s);
		} finally {
			lock.unlock();
		}
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

		/** Called when there is an exception running the operation. */
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
		private final boolean create;

		OpenWriter(boolean create) {
			super("Unable to open index writer", null);
			this.create = create;
		}

		@Override
		IndexWriter run() throws IOException {
			IndexWriterConfig config = checkNotNull(configSupplier.get(), "Null writer config supplied");
			config.setOpenMode(create ? OpenMode.CREATE : OpenMode.CREATE_OR_APPEND);
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

		IndexWriter getOpenWriter(boolean create) {
			lock.lock();
			try {
				if (!active) {
					return null;
				}
				// If we are recreating we don't reuse the writer
				if (create && indexWriter != null) {
					closeWriter();
				}
				if (indexWriter == null) {
					indexWriter = new OpenWriter(create).get();
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
			lock.lock();
			try {
				active = false;
				executor.shutdownNow();
				closeWriter();
			} finally {
				lock.unlock();
			}
		}

		void schelude(Task task, long delay) {
			lock.lock();
			try {
				if (active) {
					executor.schedule(task, delay, TimeUnit.MILLISECONDS);
				}
			} finally {
				lock.unlock();
			}
		}

	}

	/** Base task. */
	private final class Task implements Runnable {
		private final Session session;
		private final boolean scheduled;
		/** Whether to override checkpoint. */
		private final boolean overrideCheckpoint;
		/** Overridden checkpoint value. */
		private final String checkpoint;
		/** Whether to recreate the index. */
		private final boolean create;

		/** Internal constructor. */
		private Task(Session session, boolean scheduled, long delay, boolean overrideCheckpoint, String checkpoint,
				boolean create) {
			this.session = checkNotNull(session);
			this.scheduled = scheduled;
			this.overrideCheckpoint = overrideCheckpoint;
			this.checkpoint = checkpoint;
			this.create = create;
			session.schelude(this, delay);
		}

		/** Scheduled task constructor. */
		Task(Session session, long delay) {
			this(session, true, delay, false, null, false);
		}

		/** Checkpoint setter constructor. */
		Task(Session session, @Nullable String checkpoint) {
			this(session, false, 0L, true, checkpoint, false);
		}

		/** Index recreation. */
		Task(Session session) {
			this(session, false, 0L, false, null, true);
		}

		@Override
		public final void run() {
			boolean ok = false;
			WriterResult result = WriterResult.ERROR;
			final IndexWriter indexWriter = session.getOpenWriter(create);
			try {
				final DefaultWriter writer = new DefaultWriter(writerLog(), indexWriter, overrideCheckpoint, checkpoint, create);
				try {
					indexer.index(writer);
					result = writer.done();
					ok = true;
				} catch (InterruptedException e) {
					// Nothing to do.
				} catch (Exception e) {
					log().error(e, "Uncaught exception");
				}
			} finally {
				if (!ok) {
					session.closeWriter();
				}
				if (scheduled) {
					final long delay;
					switch (result) {
					case NORMAL:
						delay = getDelays().getNormal();
						break;
					case IDLE:
						delay = getDelays().getIdle();
						break;
					default:
						delay = getDelays().getError();
						break;
					}
					new Task(session, delay);
				}
			}
		}
	}

}
