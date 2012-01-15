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

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;

/**
 * Base interface for field adders.
 * @author Andres Rodriguez
 */
public interface FieldAdder<B extends BaseDocumentBuilder<B>> {
	/**
	 * Adds the field to the document field with the current information and the provided value.
	 * @param value Field value.
	 * @return The document builder.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 * @throws IllegalStateException if the document was already built.
	 */
	B add(String value);

	/**
	 * Adds the field to the document indexed, tokenized but not stored field with the current term
	 * vector information..
	 * @param reader Field value reader.
	 * @return The document builder.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 * @throws IllegalStateException if the document was already built.
	 */
	B add(Reader reader);

	/**
	 * Adds the field to the document indexed, tokenized but not stored field with the current term
	 * vector information..
	 * @param tokenStream Field value token stream.
	 * @return The document builder.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 * @throws IllegalStateException if the document was already built.
	 */
	B add(TokenStream tokenStream);
}
