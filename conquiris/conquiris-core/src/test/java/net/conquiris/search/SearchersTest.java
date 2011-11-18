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

import java.io.IOException;

import net.conquiris.api.search.ManagedReaderSupplier;
import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.SearcherService;
import net.conquiris.support.TestSupport;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.testng.annotations.Test;

/**
 * Tests for searchers.
 * @author Andres Rodriguez
 */
public class SearchersTest {
	private static final int FROM = 1000;
	private static final int TO = 10000;

	private Directory directory;
	private ReaderSupplier supplier;
	private ReaderSupplier unmanaged;
	private ManagedReaderSupplier managed;
	private SearcherService unmanagedSearcher;
	private SearcherService managedSearcher;

	private void create() {
		directory = new RAMDirectory();
		supplier = ReaderSuppliers.directory(directory);
		unmanaged = ReaderSuppliers.directory(directory);
		managed = ReaderSuppliers.managed(unmanaged, 500L);
		unmanagedSearcher = Searchers.service(supplier);
		managedSearcher = Searchers.service(managed);
	}
	
	private void createFull() throws IOException {
		create();
		TestSupport.write(directory, FROM, TO);
	}
	

	@Test
	public void empty() {
		create();
		performEmptyQueries(unmanagedSearcher);
		performEmptyQueries(managedSearcher);
		assertEquals(managed.getRequested(), supplier.getRequested());
		assertEquals(managed.getRequested(), unmanaged.getRequested());
		assertEquals(managed.getReopened(), 0);
		assertEquals(managed.getReused(), 0);
	}

	@Test(dependsOnMethods="empty")
	public void single() throws IOException {
		createFull();
		performQueriesInService(unmanagedSearcher, FROM, TO);
	}
	
}
