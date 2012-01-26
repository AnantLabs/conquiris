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

import net.conquiris.api.index.Checkpoints;
import net.conquiris.api.index.IndexException;
import net.conquiris.api.index.Indexer;
import net.conquiris.api.index.Writer;
import net.conquiris.lucene.document.DocumentBuilder;
import net.conquiris.support.TestSupport;

/**
 * Test indexer.
 * @author Andres Rodriguez
 */
public class TestIndexer implements Indexer {
	@Override
	public void index(Writer writer) throws InterruptedException, IndexException {
		String checkpoint = writer.getCheckpoint();
		int cp = Checkpoints.ofInt(checkpoint, 0) + 1;
		cp++;
		writer.add(DocumentBuilder.create().add(TestSupport.ID, cp).build());
		writer.setCheckpoint(Integer.toString(cp));
	}
}
