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

import net.conquiris.api.index.Checkpoints;
import net.conquiris.api.index.Delays;
import net.conquiris.api.index.Indexer;
import net.conquiris.api.search.SearcherService;
import net.conquiris.lucene.Conquiris;
import net.conquiris.search.ReaderSuppliers;
import net.conquiris.search.Searchers;
import net.conquiris.support.TestSupport;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * Tests for DefaultIndexerService.
 * @author Andres Rodriguez
 */
public class AbstractDirectoryIndexerServiceTest {
	private Directory directory;
	private SearcherService searcher;
	DirectoryIndexerService service;

	final int count() {
		return TestSupport.getCount(searcher);
	}

	final void found(int value) {
		TestSupport.found(searcher, value);
	}

	final void notFound(int value) {
		TestSupport.notFound(searcher, value);
	}

	final int checkpoint() {
		return Checkpoints.ofInt(service.getIndexInfo().getCheckpoint(), 0);
	}

	final void create(Indexer indexer) {
		directory = new RAMDirectory();
		searcher = Searchers.service(ReaderSuppliers.directory(directory));
		service = new DirectoryIndexerService(indexer, directory, Conquiris.writerConfigSupplier());
		service.setDelays(Delays.constant(50));
	}
}
