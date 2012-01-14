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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import net.derquinse.common.base.Builder;
import net.derquinse.common.reflect.This;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

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

	/**
	 * Adds a fieldable to the document.
	 * @param fieldable Fieldable to add.
	 * @return This builder.
	 * @throws IllegalStateException if the document was already built.
	 */
	public final B add(Fieldable fieldable) {
		checkAvailable();
		checkNotNull(fieldable, "The fieldable to add must be provided");
		fields.add(fieldable.name());
		document.add(fieldable);
		return thisValue();
	}

	/**
	 * Adds several fieldables to the document.
	 * @param fieldables Fieldables to add.
	 * @return This builder.
	 * @throws IllegalStateException if the document was already built.
	 */
	public final B add(Iterable<? extends Fieldable> fieldables) {
		checkAvailable();
		checkNotNull(fieldables, "The fieldables to add must be provided");
		for (Fieldable f : fieldables) {
			add(f);
		}
		return thisValue();
	}

	/**
	 * Creates a new numeric field builder that adds to the current document.
	 * @param name Field name.
	 */
	public final DocNumericFieldBuilder numeric(String name) {
		return new DocNumericFieldBuilder(name);
	}

	/** Numeric field builder that adds to the current builder. */
	public final class DocNumericFieldBuilder extends BaseNumericFieldBuilder<DocNumericFieldBuilder> implements
			NumericFieldAdder<B> {
		/**
		 * Constructor.
		 * @param name Field name.
		 */
		private DocNumericFieldBuilder(String name) {
			super(name);
		}

		@Override
		public B add(int value) {
			return BaseDocumentBuilder.this.add(build(value));
		}

		@Override
		public B add(long value) {
			return BaseDocumentBuilder.this.add(build(value));
		}

		@Override
		public B add(float value) {
			return BaseDocumentBuilder.this.add(build(value));
		}

		@Override
		public B add(double value) {
			return BaseDocumentBuilder.this.add(build(value));
		}
	}

}
