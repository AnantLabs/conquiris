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

import net.derquinse.common.base.ByteString;

/**
 * Base interface for binary field adders.
 * @author Andres Rodriguez
 */
public interface BinaryFieldAdder<B extends BaseDocumentBuilder<B>> {
	/**
	 * Adds the field to the document with the provided value.
	 * @param value Field value.
	 * @return The document builder.
	 * @throws IllegalStateException if the document was already built.
	 */
	B add(byte[] value);

	/**
	 * Adds the field to the document with the provided value.
	 * @param value Field value.
	 * @return The document builder.
	 * @throws IllegalStateException if the document was already built.
	 */
	B add(ByteString value);

}
