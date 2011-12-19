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

import static org.testng.Assert.assertEquals;
import net.conquiris.api.index.Checkpoints;
import net.conquiris.api.index.Delays;
import net.conquiris.api.search.SearcherService;
import net.conquiris.lucene.Conquiris;
import net.conquiris.search.ReaderSuppliers;
import net.conquiris.search.Searchers;
import net.conquiris.support.TestIndexer;
import net.conquiris.support.TestSupport;

import org.apache.lucene.store.RAMDirectory;
import org.testng.annotations.Test;

/**
 * Tests for DefaultIndexerService.
 * @author Andres Rodriguez
 */
public class DirectoryIndexerServiceTest {
	private final RAMDirectory directory = new RAMDirectory();
	private final SearcherService searcher = Searchers.service(ReaderSuppliers.directory(directory));
	private final DirectoryIndexerService service = new DirectoryIndexerService(new TestIndexer(), directory,
			Conquiris.writerConfigSupplier());

	private int count() {
		return TestSupport.getCount(searcher);
	}

	private void found(int value) {
		TestSupport.found(searcher, value);
	}

	private void notFound(int value) {
		TestSupport.notFound(searcher, value);
	}

	private void check() {
		int cp = Checkpoints.ofInt(service.getIndexInfo().getCheckpoint(), 0);
		if (cp > 0) {
			found(cp - 3);
			notFound(cp + 100000);
			assertEquals(count(), cp);
		}
	}

	@Test
	public void start() {
		check();
		service.setDelays(Delays.constant(5));
		service.start();
	}

	@Test(dependsOnMethods = "start")
	public void test() throws InterruptedException {
		for (int i = 0; i < 50; i++) {
			Thread.sleep(20L);
			check();
		}
	}

	@Test(dependsOnMethods = "test")
	public void shutdown() {
		service.stop();
	}

}
