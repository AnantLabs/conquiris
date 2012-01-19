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

import java.io.Reader;

import javax.annotation.Nullable;

import net.conquiris.schema.TextSchemaItem;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.TermVector;

import com.google.common.base.Objects;

/**
 * Base class for text field builders. Text fields are indexed, tokenized but not stored by default.
 * Term vectors are not stored by default.
 * @author Andres Rodriguez
 */
public abstract class BaseTextFieldBuilder<B extends BaseTextFieldBuilder<B>> extends IndexableFieldBuilder<B> {
	/** Whether to tokenize the field. */
	private boolean tokenize = true;
	/** Whether to store norms. */
	private boolean norms = true;
	/** Term vector. */
	private TermVector vector = TermVector.NO;

	/**
	 * Constructor.
	 * @param name Field name.
	 */
	BaseTextFieldBuilder(String name) {
		super(name);
	}

	/**
	 * Constructor based on a schema item.
	 * @param item Schema item to base this builder on.
	 */
	BaseTextFieldBuilder(TextSchemaItem item) {
		super(item);
		tokenize = item.isTokenized();
		norms = item.isNorms();
		if (item.isVectors()) {
			if (item.isPositions()) {
				this.vector = item.isOffsets() ? TermVector.WITH_POSITIONS_OFFSETS : TermVector.WITH_POSITIONS;
			} else if (item.isOffsets()) {
				this.vector = TermVector.WITH_OFFSETS;
			} else {
				this.vector = TermVector.YES;
			}
		} else {
			this.vector = TermVector.NO;
		}
	}

	/** Returns the field index value. */
	private Index fieldIndex() {
		if (isIndexed()) {
			if (tokenize) {
				return norms ? Index.ANALYZED : Index.ANALYZED_NO_NORMS;
			} else {
				return norms ? Index.NOT_ANALYZED : Index.NOT_ANALYZED_NO_NORMS;
			}
		}
		return Index.NO;
	}

	/**
	 * Sets whether to index the field. The field will not be indexed if the argument is {@code null}.
	 */
	public final B index(@Nullable Index index) {
		if (index == Index.ANALYZED) {
			index(true);
			this.tokenize = true;
			this.norms = true;
		} else if (index == Index.ANALYZED_NO_NORMS) {
			index(true);
			this.tokenize = true;
			this.norms = false;
		} else if (index == Index.NOT_ANALYZED) {
			index(true);
			this.tokenize = false;
			this.norms = true;
		} else if (index == Index.NOT_ANALYZED_NO_NORMS) {
			index(true);
			this.tokenize = false;
			this.norms = false;
		} else {
			index(false);
		}
		return thisValue();
	}

	/** Sets whether to tokenize the field. */
	public final B tokenize(boolean tokenize) {
		this.tokenize = tokenize;
		return thisValue();
	}

	/** Sets whether to store norms. */
	public final B norms(boolean norms) {
		this.norms = norms;
		return thisValue();
	}

	/** Sets whether to store term vectors. They will not be stored if the argument is {@code null}. */
	public final B vector(@Nullable TermVector vector) {
		this.vector = Objects.firstNonNull(vector, TermVector.NO);
		return thisValue();
	}

	/**
	 * Builds a field with the current information and the provided value.
	 * @param value Field value.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final Field build(String value) {
		checkNotNull(value, "The field value must be provided");
		return new Field(name(), value, checkUsed(), fieldIndex(), vector);
	}

	private Field decorateIndexed(Field field) {
		if (!norms) {
			field.setOmitNorms(true);
		}
		return field;
	}

	/**
	 * Builds an indexed, tokenized but not stored field with the current term vector information.
	 * @param reader Field value reader.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final Field build(Reader reader) {
		checkNotNull(reader, "The field value reader must be provided");
		return decorateIndexed(new Field(name(), reader, vector));
	}

	/**
	 * Builds an indexed, tokenized but not stored field with the current term vector information.
	 * @param tokenStream Field value token stream.
	 * @return The created field.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 */
	public final Field build(TokenStream tokenStream) {
		checkNotNull(tokenStream, "The field value token stream must be provided");
		return decorateIndexed(new Field(name(), tokenStream, vector));
	}

}
