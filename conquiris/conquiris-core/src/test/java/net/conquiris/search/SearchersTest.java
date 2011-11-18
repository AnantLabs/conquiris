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

import static net.conquiris.support.TestSupport.performEmptyQueries;
import static net.conquiris.support.TestSupport.performQueriesInService;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.conquiris.api.search.ManagedReaderSupplier;
import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.SearcherService;
import net.conquiris.support.TestSupport;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.testng.annotations.Test;

import com.google.common.primitives.Longs;

/**
 * Tests for searchers.
 * @author Andres Rodriguez
 */
public class SearchersTest {
	private static final int FROM = 1000;
	private static final int TO = 10000;
	private static final int ALPHA = TO*10;

	private Directory directory;
	private ReaderSupplier supplier;
	private ReaderSupplier unmanaged;
	private ManagedReaderSupplier managed;
	private SearcherService unmanagedSearcher;
	private SearcherService managedSearcher;
	private final SecureRandom random = new SecureRandom(Longs.toByteArray(System.currentTimeMillis()));

	private void create() {
		directory = new RAMDirectory();
		supplier = ReaderSuppliers.directory(directory);
		unmanaged = ReaderSuppliers.directory(directory);
		managed = ReaderSuppliers.managed(unmanaged, 50L);
		unmanagedSearcher = Searchers.service(supplier);
		managedSearcher = Searchers.service(managed);
	}
	
	private void write(int scale) {
		try {
			int delta = scale*ALPHA;
			TestSupport.write(directory, delta+FROM, delta+TO);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void createFull() {
		create();
		write(0);
	}

	@Test
	public void empty() {
		create();
		performEmptyQueries(unmanagedSearcher);
		performEmptyQueries(managedSearcher);
		assertEquals(managed.getRequested(), unmanaged.getRequested());
		assertEquals(managed.getReopened(), 0);
		assertEquals(managed.getReused(), 0);
		show("Empty");
	}
	
	private void show(String test) {
		System.out.printf("===== BEGIN %s\n", test);
		System.out.printf("Supplier: %d requested\n", supplier.getRequested());
		System.out.printf("Unmanaged: %d requested\n", unmanaged.getRequested());
		System.out.printf("Managed: %d requested, %d reopened, %d reused\n", managed.getRequested(), managed.getReopened(),
				managed.getReused());
		System.out.printf("===== END %s\n\n", test);
		assertTrue(unmanaged.getRequested() <= supplier.getRequested());
		assertEquals(managed.getRequested(), supplier.getRequested());
	}
	
	@Test(dependsOnMethods = "empty")
	public void added() {
		write(0);
		performQueriesInService(unmanagedSearcher, FROM, TO);
		performQueriesInService(managedSearcher, FROM, TO);
		show("Added over empty");
	}
	

	@Test(dependsOnMethods = "added")
	public void single() throws Exception {
		createFull();
		for (int i = 0; i < 5; i++) {
			Thread.sleep(20L);
			performQueriesInService(unmanagedSearcher, FROM, TO);
			performQueriesInService(managedSearcher, FROM, TO);
		}
		write(1);
		for (int i = 0; i < 5; i++) {
			Thread.sleep(30L);
			performQueriesInService(unmanagedSearcher, FROM, TO);
			performQueriesInService(managedSearcher, FROM, TO);
			write(2+i);
		}
		show("Simple");
	}
	
	private void sleep(long base, int range) {
		try {
			Thread.sleep(base + random.nextInt(range));
		} catch (InterruptedException e) {
		}
	}
	
	private ExecutorService executor(int threads, int tasks) throws Exception {
		createFull();
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		Runnable r = new Runnable() {
			@Override
			public void run() {
				sleep(19L, 15);
				performQueriesInService(unmanagedSearcher, FROM, TO);
				performQueriesInService(managedSearcher, FROM, TO);
			}
		};
		for (int i = 0; i < tasks; i++) {
			executor.submit(r);
		}
		return executor;
	}

	private void join(String test, ExecutorService executor) throws Exception {
		executor.shutdown();
		while (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
			System.out.printf("Waiting %s...\n", test);
		}
		show(test);
	}
	
	
	@Test(dependsOnMethods = "single")
	public void concurrent() throws Exception {
		createFull();
		join("Concurrent", executor(5, 200));
	}

	@Test(dependsOnMethods = "concurrent")
	public void complexConcurrent() throws Exception {
		createFull();
		ExecutorService s = executor(5, 2000);
		for (int i = 0; i < 10; i++) {
			sleep(2L, 10);
			write(3+i);
		}
		join("Complex Concurrent", s);
	}
	
	
}
