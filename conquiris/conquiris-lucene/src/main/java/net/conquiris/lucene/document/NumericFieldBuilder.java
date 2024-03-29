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

/**
 * Builder for numeric fields. Numeric fields are indexed but not stored by default.
 * @author Andres Rodriguez
 */
public final class NumericFieldBuilder extends BaseNumericFieldBuilder<NumericFieldBuilder> {

	/**
	 * Creates a new builder.
	 * @param name Field name.
	 */
	public static NumericFieldBuilder create(String name) {
		return new NumericFieldBuilder(name);
	}

	/**
	 * Constructor.
	 * @param name Field name.
	 */
	private NumericFieldBuilder(String name) {
		super(name);
	}
}
