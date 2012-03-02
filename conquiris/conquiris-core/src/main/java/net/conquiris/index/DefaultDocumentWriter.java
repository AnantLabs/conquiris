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

import static com.google.common.base.Preconditions.checkNotNull;
import net.conquiris.api.index.DocumentWriter;
import net.conquiris.api.index.IndexException;
import net.conquiris.api.index.Writer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

/**
 * Default document writer implementation.
 * @author Andres Rodriguez.
 */
final class DefaultDocumentWriter implements DocumentWriter {
	/** Writer to use. */
	private final Writer writer;

	/**
	 * Constructor.
	 * @param writer Writer to use.
	 */
	DefaultDocumentWriter(Writer writer) {
		this.writer = checkNotNull(writer, "The writer must be provided");
	}

	@Override
	public void cancel() throws InterruptedException {
		writer.cancel();
	}

	@Override
	public DocumentWriter add(Document document) throws InterruptedException, IndexException {
		writer.add(document);
		return this;
	}

	@Override
	public DocumentWriter add(Document document, Analyzer analyzer) throws InterruptedException, IndexException {
		writer.add(document, analyzer);
		return this;
	}

	@Override
	public DocumentWriter deleteAll() throws InterruptedException, IndexException {
		writer.deleteAll();
		return this;
	}

	@Override
	public DocumentWriter delete(String field, String text) throws InterruptedException, IndexException {
		writer.delete(field, text);
		return this;
	}

	@Override
	public DocumentWriter delete(Term term) throws InterruptedException, IndexException {
		writer.delete(term);
		return this;
	}

	@Override
	public DocumentWriter update(String field, String text, Document document) throws InterruptedException,
			IndexException {
		writer.update(field, text, document);
		return this;
	}

	@Override
	public DocumentWriter update(Term term, Document document) throws InterruptedException, IndexException {
		writer.update(term, document);
		return this;
	}

	@Override
	public DocumentWriter update(String field, String text, Document document, Analyzer analyzer)
			throws InterruptedException, IndexException {
		writer.update(field, text, document, analyzer);
		return this;
	}

	@Override
	public DocumentWriter update(Term term, Document document, Analyzer analyzer) throws InterruptedException,
			IndexException {
		writer.update(term, document, analyzer);
		return this;
	}

}
