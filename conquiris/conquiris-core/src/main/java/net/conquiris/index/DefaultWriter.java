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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.concurrent.GuardedBy;

import net.conquiris.api.index.IndexException;
import net.conquiris.api.index.IndexStatus;
import net.conquiris.api.index.Indexer;
import net.conquiris.api.index.Writer;
import net.derquinse.common.util.concurrent.Interruptions;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.ThreadInterruptedException;
import org.slf4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.google.common.util.concurrent.Atomics;

/**
 * Default writer implementation.
 * @author Andres Rodriguez.
 */
final class DefaultWriter extends AbstractWriter {
	/** Writer state lock. */
	private final Lock lock = new ReentrantLock();
	/** Index writer lock. Is RW as the Index Writer has its own synchronization. */
	private final ReadWriteLock indexLock = new ReentrantReadWriteLock();
	/** Lucene index writer. */
	private final IndexWriter writer;
	/** Last commit user properties. */
	private final ImmutableMap<String, String> lastProperties;
	/** Current user properties. */
	@GuardedBy("lock")
	private final Map<String, String> properties;
	/** User properties key set. */
	private final Set<String> keys;
	/** Last commit checkpoint. */
	private final String checkpoint;
	/** Last commit timestamp. */
	private final long timestamp;
	/** Last commit sequence. */
	private final long sequence;
	/** Whether any indexer has been ever interrupted. */
	private volatile boolean interrupted = false;
	/** Whether the writer is available. */
	@GuardedBy("lock")
	private volatile boolean available = true;
	/** Whether the writer has been cancelled. */
	@GuardedBy("lock")
	private volatile boolean cancelled = false;
	/** Whether the index has been updated. */
	@GuardedBy("indexLock")
	private boolean updated = false;
	/** Index status. */
	@GuardedBy("indexLock")
	private final AtomicReference<IndexStatus> indexStatus = Atomics.newReference(IndexStatus.OK);
	/** Current checkpoint. */
	@GuardedBy("this")
	private String newCheckpoint;

	private static long safe2Long(String s) {
		if (s == null) {
			return 0L;
		}
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Default writer.
	 * @param writer Lucene index writer to use.
	 */
	DefaultWriter(Logger logger, IndexWriter writer) throws IndexException {
		this.writer = checkNotNull(writer, "The index writer must be provided");
		this.properties = new MapMaker().makeMap();
		this.keys = Collections.unmodifiableSet(this.properties.keySet());
		// Read properties
		try {
			final IndexReader reader = IndexReader.open(writer, false);
			try {
				Map<String, String> data = reader.getCommitUserData();
				if (data == null || data.isEmpty()) {
					this.lastProperties = ImmutableMap.of();
					this.checkpoint = null;
					this.timestamp = 0L;
					this.sequence = 0L;
				} else {
					this.lastProperties = ImmutableMap.copyOf(Maps.filterEntries(data, IS_USER_PROPERTY));
					properties.putAll(this.lastProperties);
					this.checkpoint = data.get(CHECKPOINT);
					this.timestamp = safe2Long(data.get(TIMESTAMP));
					this.sequence = safe2Long(data.get(SEQUENCE));
				}
				this.newCheckpoint = this.checkpoint;
			} finally {
				Closeables.closeQuietly(reader);
			}
		} catch (LockObtainFailedException e) {
			indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.LOCKED);
			throw new IndexException(e);
		} catch (CorruptIndexException e) {
			indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.CORRUPT);
			throw new IndexException(e);
		} catch (IOException e) {
			indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.IOERROR);
			throw new IndexException(e);
		}
	}

	/**
	 * Called when the writer can't be used any longer.
	 * @return True if the writer was commited.
	 */
	void done() throws InterruptedException {
		lock.lock();
		try {
			ensureAvailable();
			available = false;
			indexLock.writeLock().lock();
			try {
				// TODO: status and error handling.
				if (cancelled || !updated || Objects.equal(checkpoint, newCheckpoint) || lastProperties.equals(properties)) {
					// TODO: rollback
				}
				// TODO: commit
			} finally {
				indexLock.writeLock().unlock();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	boolean ensureAvailable() throws InterruptedException {
		checkState(available, "The writer can't be used any longer");
		if (interrupted) {
			throw new InterruptedException();
		}
		boolean ok = false;
		try {
			Interruptions.throwIfInterrupted();
			ok = true;
		} finally {
			if (!ok)
				interrupted = true;
		}
		return !cancelled && IndexStatus.OK == indexStatus.get();
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#cancel()
	 */
	@Override
	public void cancel() throws InterruptedException {
		lock.lock();
		try {
			ensureAvailable();
			if (!cancelled) {
				newCheckpoint = checkpoint;
				properties.clear();
				properties.putAll(lastProperties);
				cancelled = true;
			}
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#getCheckpoint()
	 */
	@Override
	public String getCheckpoint() throws InterruptedException {
		ensureAvailable();
		return checkpoint;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#getTimestamp()
	 */
	@Override
	public long getTimestamp() throws InterruptedException {
		ensureAvailable();
		return timestamp;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#getSequence()
	 */
	@Override
	public long getSequence() throws InterruptedException {
		ensureAvailable();
		return sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) throws InterruptedException {
		ensureAvailable();
		return properties.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#getPropertyKeys()
	 */
	@Override
	public Set<String> getPropertyKeys() throws InterruptedException {
		ensureAvailable();
		return keys;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#setCheckpoint(java.lang.String)
	 */
	@Override
	public Writer setCheckpoint(String checkpoint) throws InterruptedException {
		lock.lock();
		try {
			if (ensureAvailable()) {
				this.newCheckpoint = checkpoint;
			}
		} finally {
			lock.unlock();
		}
		return this;
	}

	private static void checkProperty(String key, String value) {
		checkNotNull(key, "Null commit property key [%s]", key);
		checkArgument(!IS_RESERVED.apply(key), "Reserved commit property key [%s]", key);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public Writer setProperty(String key, String value) throws InterruptedException {
		lock.lock();
		try {
			if (ensureAvailable()) {
				checkProperty(key, value);
				if (value != null) {
					properties.put(key, value);
				} else {
					properties.remove(key);
				}
			}
		} finally {
			lock.unlock();
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#setProperties(java.util.Map)
	 */
	@Override
	public Writer setProperties(Map<String, String> values) throws InterruptedException {
		checkNotNull(values, "The commit properties map is null");
		lock.lock();
		try {
			if (ensureAvailable()) {
				Map<String, String> put = Maps.newHashMapWithExpectedSize(values.size());
				Set<String> remove = Sets.newHashSet();
				for (Entry<String, String> e : values.entrySet()) {
					String key = e.getKey();
					String value = e.getValue();
					checkProperty(key, value);
					if (value != null) {
						put.put(key, value);
					} else {
						remove.add(key);
					}
				}
				properties.putAll(put);
				for (String k : remove) {
					properties.remove(k);
				}
			}
		} finally {
			lock.unlock();
		}
		return this;
	}

	/*
	 * Index Operations support.
	 */

	private Analyzer analyzer(Analyzer a) {
		return a != null ? a : writer.getAnalyzer();
	}

	private abstract class IndexOp {
		IndexOp() {
		}

		abstract boolean perform() throws IOException, InterruptedException;

		final void run() throws InterruptedException {
			indexLock.readLock().lock();
			boolean cancel = false;
			try {
				if (ensureAvailable()) {
					if (perform()) {
						updated = true;
					}
				}
			} catch (ThreadInterruptedException e) {
				interrupted = true;
				throw new InterruptedException();
			} catch (LockObtainFailedException e) {
				indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.LOCKED);
				throw new IndexException(e);
			} catch (CorruptIndexException e) {
				indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.CORRUPT);
				throw new IndexException(e);
			} catch (IOException e) {
				indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.IOERROR);
				throw new IndexException(e);
			} finally {
				if (cancel) {
					cancelled = true;
				}
				indexLock.readLock().unlock();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#add(org.apache.lucene.document.Document,
	 * org.apache.lucene.analysis.Analyzer)
	 */
	@Override
	public Writer add(final Document document, final Analyzer analyzer) throws InterruptedException {
		new IndexOp() {
			@Override
			boolean perform() throws IOException, InterruptedException {
				if (document != null) {
					writer.addDocument(document, analyzer(analyzer));
					return true;
				}
				return false;
			}
		}.run();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#deleteAll()
	 */
	@Override
	public Writer deleteAll() throws InterruptedException {
		new IndexOp() {
			@Override
			boolean perform() throws IOException, InterruptedException {
				writer.deleteDocuments(new MatchAllDocsQuery());
				return true;
			}
		}.run();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#delete(org.apache.lucene.index.Term)
	 */
	@Override
	public Writer delete(final Term term) throws InterruptedException {
		new IndexOp() {
			@Override
			boolean perform() throws IOException, InterruptedException {
				if (!isTermNull(term)) {
					writer.deleteDocuments(term);
					return true;
				}
				return false;
			}
		}.run();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#update(org.apache.lucene.index.Term,
	 * org.apache.lucene.document.Document, org.apache.lucene.analysis.Analyzer)
	 */
	@Override
	public Writer update(final Term term, final Document document, final Analyzer analyzer) throws InterruptedException {
		new IndexOp() {
			@Override
			boolean perform() throws IOException, InterruptedException {
				if (document != null) {
					if (isTermNull(term)) {
						writer.addDocument(document, analyzer(analyzer));
					} else {
						writer.updateDocument(term, document, analyzer(analyzer));
					}
					return true;
				}
				return false;
			}
		}.run();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#runSubindexers(java.util.concurrent.Executor,
	 * java.lang.Iterable)
	 */
	@Override
	public Writer runSubindexers(Executor executor, Iterable<? extends Indexer> subindexers) throws InterruptedException,
			IndexException {
		checkNotNull(executor, "The executor must be provided");
		checkNotNull(executor, "The subindexers must be provided");
		if (ensureAvailable()) {
			CompletionService<IndexStatus> ecs = new ExecutorCompletionService<IndexStatus>(executor);
			int n = 0;
			for (Indexer subindexer : Iterables.filter(subindexers, Predicates.notNull())) {
				ecs.submit(new SubindexerTask(subindexer));
				n++;
			}
			for (int i = 0; i < n && ensureAvailable(); i++) {
				ecs.take();
			}
		}
		return this;
	}

	private final class SubindexerTask implements Callable<IndexStatus> {
		private final Indexer indexer;

		SubindexerTask(Indexer indexer) {
			this.indexer = checkNotNull(indexer, "The subindexer must be provided");
		}

		@Override
		public IndexStatus call() throws Exception {
			if (ensureAvailable()) {
				indexer.index(DefaultWriter.this);
			}
			return indexStatus.get();
		}
	}

}
