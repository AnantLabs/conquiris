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
package net.conquiris.jersey.itest;

import net.conquiris.api.index.LocalIndexerService;
import net.conquiris.index.DirectoryIndexerService;
import net.conquiris.lucene.Conquiris;

import org.apache.lucene.store.RAMDirectory;

/**
 * Test indexer service provider.
 * @author Andres Rodriguez
 */
class TestIndexerServiceProvider {
	private static LocalIndexerService service = null;

	public static synchronized LocalIndexerService get() {
		if (service == null) {
			RAMDirectory d = new RAMDirectory();
			service = new DirectoryIndexerService(new TestIndexer(), d, Conquiris.writerConfigSupplier());
		}
		return service;
	}
}
