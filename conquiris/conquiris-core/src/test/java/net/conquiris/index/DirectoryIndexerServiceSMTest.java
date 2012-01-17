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

import static org.testng.Assert.assertTrue;
import net.conquiris.support.TestIndexer;
import net.conquiris.support.TestMultiIndexer;
import net.conquiris.support.TestSimpleIndexer;

import org.testng.annotations.Test;

/**
 * Tests for DefaultIndexerService: Simple and multi indexers.
 * @author Andres Rodriguez
 */
public class DirectoryIndexerServiceSMTest extends AbstractDirectoryIndexerServiceTest {
	private TestIndexer indexer;

	private void check() {
		int cp = checkpoint();
		if (cp > 0) {
			found(cp - 3);
			notFound(cp + 100000);
			assertTrue(count() >= cp);
		}
	}

	private void create() {
		create(indexer);
	}

	private void testService() throws InterruptedException {
		create();
		check();
		service.start();
		doTest();
		service.stop();
		indexer.setTarget(2 * indexer.getTarget());
		service.start();
		doTest();
		service.stop();
		System.out.println("Count: " + count());
	}

	private void doTest() throws InterruptedException {
		for (int i = 0; i < 50; i++) {
			Thread.sleep(20L);
			check();
		}
		assertTrue(checkpoint() >= 0);
		assertTrue(count() >= 0);
	}

	@Test
	public void simple() throws InterruptedException {
		indexer = new TestSimpleIndexer();
		testService();
	}

	@Test(dependsOnMethods = "simple")
	public void multi() throws InterruptedException {
		indexer = new TestMultiIndexer();
		indexer.setTarget(20000);
		testService();
	}

}
