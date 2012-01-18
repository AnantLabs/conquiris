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
import net.conquiris.support.TestSimpleIndexer;

import org.testng.annotations.Test;

/**
 * Tests for DefaultIndexerService: Methods.
 * @author Andres Rodriguez
 */
public class DirectoryIndexerServiceTest extends AbstractDirectoryIndexerServiceTest {
	private static final int CP1 = 1000000;
	private static final int CP2 = 10 * CP1;

	private final TestIndexer indexer = new TestSimpleIndexer();

	private void create(int target) {
		indexer.setTarget(target);
		create(indexer);
		service.start();
	}

	@Test
	public void worker() throws InterruptedException {
		create(Integer.MAX_VALUE - 10);
		assertTrue(checkpoint() >= 0);
		Thread.sleep(100L);
		int cp1 = checkpoint();
		Thread.sleep(500L);
		int cp2 = checkpoint();
		assertTrue(cp2 > cp1);
		service.stop();
		int cp3 = checkpoint();
		assertTrue(cp3 >= cp2);
		Thread.sleep(500L);
		assertTrue(checkpoint() == cp3);
		service.setCheckpoint(Integer.toString(CP1));
		Thread.sleep(100L);
		assertTrue(checkpoint() == cp3);
		service.reindex();
		Thread.sleep(100L);
		assertTrue(checkpoint() == cp3);
		service.start();
		service.setCheckpoint(Integer.toString(CP1));
		Thread.sleep(500L);
		assertTrue(checkpoint() > CP1);
		service.reindex();
		Thread.sleep(100L);
		assertTrue(checkpoint() < CP1);
		service.setCheckpoint(Integer.toString(CP2));
		Thread.sleep(500L);
		assertTrue(checkpoint() > CP2);
		service.setCheckpoint(null);
		Thread.sleep(500L);
		assertTrue(checkpoint() < CP1);
		service.stop();
	}
	
	private void checkEmpty() throws InterruptedException {
		Thread.sleep(200L);
		assertTrue(checkpoint() == 0);
	}

	@Test
	public void empty() throws InterruptedException {
		create(0);
		checkEmpty();
		service.stop();
		checkEmpty();
		service.setCheckpoint(Integer.toString(CP1));
		checkEmpty();
		service.reindex();
		checkEmpty();
		service.start();
		checkEmpty();
		service.setCheckpoint(Integer.toString(CP1));
		checkEmpty();
		service.reindex();
		checkEmpty();
		service.setCheckpoint(Integer.toString(CP2));
		checkEmpty();
		service.setCheckpoint(null);
		checkEmpty();
	}
}
