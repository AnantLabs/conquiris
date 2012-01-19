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
package net.conquiris.lucene.document;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.Reader;
import java.util.UUID;

import net.conquiris.schema.DoubleSchemaItem;
import net.conquiris.schema.FieldSchemaItem;
import net.conquiris.schema.FloatSchemaItem;
import net.conquiris.schema.InstantSchemaItem;
import net.conquiris.schema.IntegerSchemaItem;
import net.conquiris.schema.LongSchemaItem;
import net.conquiris.schema.NumericSchemaItem;
import net.conquiris.schema.SchemaItem;
import net.conquiris.schema.StreamSchemaItem;
import net.conquiris.schema.UUIDSchemaItem;
import net.derquinse.common.base.Builder;
import net.derquinse.common.reflect.This;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.joda.time.ReadableInstant;

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

	/**
	 * Creates a new text field builder that adds to the current document.
	 * @param name Field name.
	 */
	public final DocTextFieldBuilder text(String name) {
		return new DocTextFieldBuilder(name);
	}

	/**
	 * Creates a new binary field builder that adds to the current document.
	 * @param name Field name.
	 */
	public final DocBinaryFieldBuilder binary(String name) {
		return new DocBinaryFieldBuilder(name);
	}

	/**
	 * Checks whether a value for a schema item can be added.
	 * @throws IllegalStateException if the maximum number of occurrences has been reached.
	 */
	private void checkItem(SchemaItem item) {
		checkNotNull(item, "The schema item must be provided");
		String name = item.getName();
		checkState(fields.count(name) < item.getMaxOccurs(), "Maximum number of occurrences for field [%s] reached", name);
	}

	/**
	 * Returns a numeric field builder based on a schema item.
	 * @param item Schema item to base the builder on.
	 * @throws IllegalStateException if the maximum number of occurrences has been reached.
	 */
	private DocNumericFieldBuilder numeric(NumericSchemaItem<?> item) {
		checkItem(item);
		return new DocNumericFieldBuilder(item);
	}

	/**
	 * Returns a text builder based on a schema item.
	 * @param item Schema item to base the builder on.
	 * @throws IllegalStateException if the maximum number of occurrences has been reached.
	 */
	private DocTextFieldBuilder text(FieldSchemaItem item) {
		checkItem(item);
		return new DocTextFieldBuilder(item);
	}

	/**
	 * Adds an integer field based on a schema item.
	 * @param item Schema item.
	 * @param value Field value.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(IntegerSchemaItem item, int value) {
		return numeric(item).add(value);
	}

	/**
	 * Adds a long field based on a schema item.
	 * @param item Schema item.
	 * @param value Field value.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(LongSchemaItem item, long value) {
		return numeric(item).add(value);
	}

	/**
	 * Adds a float field based on a schema item.
	 * @param item Schema item.
	 * @param value Field value.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(FloatSchemaItem item, float value) {
		return numeric(item).add(value);
	}

	/**
	 * Adds a double field based on a schema item.
	 * @param item Schema item.
	 * @param value Field value.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(DoubleSchemaItem item, double value) {
		return numeric(item).add(value);
	}

	/**
	 * Adds a textual field based on a schema item.
	 * @param item Schema item.
	 * @param value Field value.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(FieldSchemaItem item, String value) {
		return text(item).add(value);
	}

	/**
	 * Adds a streamed textual field based on a schema item.
	 * @param item Schema item.
	 * @param reader Field value reader.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(StreamSchemaItem item, Reader reader) {
		return text(item).add(reader);
	}

	/**
	 * Adds a streamed textual field based on a schema item.
	 * @param item Schema item.
	 * @param tokenStream Field value token stream.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(StreamSchemaItem item, TokenStream tokenStream) {
		return text(item).add(tokenStream);
	}

	/**
	 * Adds an UUID field based on a schema item.
	 * @param item Schema item.
	 * @param value Field value.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(UUIDSchemaItem item, UUID value) {
		return text(item).add(value.toString());
	}

	/**
	 * Adds an instant field based on a schema item.
	 * @param item Schema item.
	 * @param value Field value.
	 * @return This builder.
	 * @throws IllegalStateException if the maximum number of occurrences for this field has been
	 *           reached.
	 */
	public final B add(InstantSchemaItem item, ReadableInstant value) {
		return numeric(item).add(value.getMillis());
	}

	/** Numeric field builder that adds to the current document builder. */
	public final class DocNumericFieldBuilder extends BaseNumericFieldBuilder<DocNumericFieldBuilder> implements
			NumericFieldAdder<B> {
		/**
		 * Constructor.
		 * @param name Field name.
		 */
		private DocNumericFieldBuilder(String name) {
			super(name);
		}

		/**
		 * Constructor based on a schema item.
		 * @param item Schema item to base this builder on.
		 */
		private DocNumericFieldBuilder(NumericSchemaItem<?> item) {
			super(item);
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

	/** Text field builder that adds to the current document builder. */
	public final class DocTextFieldBuilder extends BaseTextFieldBuilder<DocTextFieldBuilder> implements TextFieldAdder<B> {
		/**
		 * Constructor.
		 * @param name Field name.
		 */
		private DocTextFieldBuilder(String name) {
			super(name);
		}

		/**
		 * Constructor based on a schema item.
		 * @param item Schema item to base this builder on.
		 */
		private DocTextFieldBuilder(FieldSchemaItem item) {
			super(item);
		}

		@Override
		public B add(String value) {
			return BaseDocumentBuilder.this.add(build(value));
		}

		@Override
		public B add(Reader reader) {
			return BaseDocumentBuilder.this.add(build(reader));
		}

		@Override
		public B add(TokenStream tokenStream) {
			return BaseDocumentBuilder.this.add(build(tokenStream));
		}
	}

	/** Binary field builder that adds to the current document builder. */
	public final class DocBinaryFieldBuilder extends BaseBinaryFieldBuilder<DocBinaryFieldBuilder> implements
			BinaryFieldAdder<B> {
		/**
		 * Constructor.
		 * @param name Field name.
		 */
		private DocBinaryFieldBuilder(String name) {
			super(name);
		}

		/**
		 * Constructor based on a schema item.
		 * @param item Schema item to base this builder on.
		 */
		private DocBinaryFieldBuilder(SchemaItem item) {
			super(item);
		}

		@Override
		public B add(byte[] value) {
			return BaseDocumentBuilder.this.add(build(value));
		}
	}

}
