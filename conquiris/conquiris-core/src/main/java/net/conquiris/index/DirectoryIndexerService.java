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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.GuardedBy;

import net.conquiris.api.index.IndexInfo;
import net.conquiris.api.index.IndexStatus;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import com.google.common.io.Closeables;

/**
 * Default directory-based indexer service implementation.
 * @author Andres Rodriguez
 */
public final class DirectoryIndexerService extends AbstractLocalIndexerService {
	/** Lucene directory to use. */
	private final Directory directory;
	/** Last known index status. */
	private volatile IndexStatus indexStatus = IndexStatus.OK;
	/** Service lock. */
	private final Lock lock = new ReentrantLock();
	/** Executor. */
	@GuardedBy("lock")
	private volatile ScheduledExecutorService executor;

	public DirectoryIndexerService(Directory directory) {
		this.directory = checkNotNull(directory, "The directory to use must be provided");
	}

	/** Called when there is an exception obtaining the index information. */
	private void noInfo(Exception e, IndexStatus status) {
		log().error(e, "Unable to read index info");
		indexStatus = status;
	}

	@Override
	public IndexInfo getIndexInfo() {
		try {
			if (!IndexReader.indexExists(directory)) {
				return IndexInfo.empty();
			}
			final IndexReader reader = IndexReader.open(directory);
			try {
				return IndexInfo.fromMap(reader.getCommitUserData());
			} finally {
				Closeables.closeQuietly(reader);
			}
		} catch (LockObtainFailedException e) {
			noInfo(e, IndexStatus.LOCKED);
		} catch (CorruptIndexException e) {
			noInfo(e, IndexStatus.CORRUPT);
		} catch (IOException e) {
			noInfo(e, IndexStatus.IOERROR);
		}
		return IndexInfo.empty();
	}

	@Override
	public IndexStatus getIndexStatus() {
		return indexStatus;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

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
		return executor != null;
	}

}
