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
package net.conquiris.support;

import net.conquiris.api.index.Checkpoints;
import net.conquiris.api.index.IndexException;
import net.conquiris.api.index.Indexer;
import net.conquiris.api.index.Writer;

/**
 * Indexer used for tests.
 * @author Andres Rodriguez
 */
public final class TestIndexer implements Indexer {
	public TestIndexer() {
	}

	@Override
	public void index(Writer writer) throws InterruptedException, IndexException {
		int start = Checkpoints.ofInt(writer.getCheckpoint(), 0) + 1;
		final int end = start + 10;
		for (int i = start; i <= end; i++) {
			writer.add(TestSupport.document(i));
		}
		writer.setCheckpoint(Integer.toString(end));
	}
}
