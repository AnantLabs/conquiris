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

import static org.testng.Assert.assertEquals;
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

	@Test
	public void empty() {
		create();
		TestSupport.performEmptyQueries(unmanagedSearcher);
		TestSupport.performEmptyQueries(managedSearcher);
		assertEquals(managed.getRequested(), supplier.getRequested());
		assertEquals(managed.getRequested(), unmanaged.getRequested());
		assertEquals(managed.getReopened(), 0);
		assertEquals(managed.getReused(), 0);
	}

}
