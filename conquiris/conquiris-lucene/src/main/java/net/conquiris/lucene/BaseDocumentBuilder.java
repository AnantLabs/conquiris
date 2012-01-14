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
package net.conquiris.lucene;

import static com.google.common.base.Preconditions.checkState;
import net.derquinse.common.base.Builder;
import net.derquinse.common.reflect.This;

import org.apache.lucene.document.Document;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

/**
 * Base class for document builders. Document builders can only be used once and are NOT THREAD
 * SAFE.
 * @author Andres Rodriguez
 */
public abstract class BaseDocumentBuilder<B extends BaseDocumentBuilder<B>> extends This<B> implements
		Builder<Document> {
	/** Document. */
	private Document document = new Document();
	/** Added fields. */
	private final Multiset<String> fields = HashMultiset.create();

	/**
	 * Constructor.
	 */
	protected BaseDocumentBuilder() {
	}

	/** Checks the document has not been built yet. */
	private void checkAvailable() {
		checkState(document != null, "Document already built");
	}

	/**
	 * Completes the document before building.
	 * @param fields A live but unmodifiable view of the fields added to the document.
	 * @throws IllegalStateException if the document must not be built.
	 */
	protected void complete(Multiset<String> fields) {
	}

	/**
	 * Builds the document.
	 * @throws IllegalStateException if the document can't be built or was already built.
	 */
	@Override
	public final Document build() {
		checkAvailable();
		complete(Multisets.unmodifiableMultiset(fields));
		Document d = document;
		document = null;
		return d;
	}

}
