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

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.conquiris.api.index.IndexException;
import net.conquiris.api.index.Indexer;
import net.conquiris.api.index.Writer;

import com.beust.jcommander.internal.Lists;

/**
 * Multithreaded indexer used for tests.
 * @author Andres Rodriguez
 */
public final class TestMultiIndexer extends TestIndexer {
	private int target = 5000;
	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	private final SecureRandom random = new SecureRandom();

	public TestMultiIndexer() {
	}

	@Override
	int index(int cp, Writer writer) throws InterruptedException, IndexException {
		int start = cp + 1;
		final int end = Math.min(target, start + 19);
		List<Indexer> list = Lists.newArrayList();
		for (int i = start; i <= end; i++) {
			list.add(new Subindexer(i));
		}
		writer.runSubindexers(executor, list);
		return end;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	final class Subindexer implements Indexer {
		private final int id;

		Subindexer(int id) {
			this.id = id;
		}

		@Override
		public void index(Writer writer) throws InterruptedException, IndexException {
			Thread.sleep(1 + random.nextInt(15));
			writer.add(TestSupport.document(id));
		}
	}
}
