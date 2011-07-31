/*
 * Copyright 2008-2011 the original author or authors.
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
package net.conquiris.qs;

/**
 * Single term query tokens.
 * @author Andres Rodriguez
 */
@QueryKey(QSTerm.KEY)
public final class QSTerm extends QueryToken {
	static final String KEY = "t";

	/**
	 * Constructor.
	 * @param field Term field.
	 * @param value Term value.
	 */
	public QSTerm(StringToken field, StringToken value) {
		super(tokens(field, value));
	}

	/**
	 * Constructor.
	 * @param field Term field.
	 * @param value Term value.
	 */
	public QSTerm(String field, String value) {
		super(strings(field, value));
	}

	/** Returns the field value. */
	public String getField() {
		return getArgument(0, StringToken.class).getValue();
	}

	/** Returns the term value. */
	public String getValue() {
		return getArgument(1, StringToken.class).getValue();
	}
}
