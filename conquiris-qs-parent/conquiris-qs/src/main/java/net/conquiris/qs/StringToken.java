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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;

/**
 * String QS tokens.
 * @author Andres Rodriguez
 */
public final class StringToken extends Token {
	/** String to StringToken function. */
	public static final Function<String, StringToken> STRING2TOKEN = new Function<String, StringToken>() {
		public StringToken apply(String input) {
			return StringToken.of(input);
		}
	};

	/**
	 * Builds a token with the specified value.
	 * @param value Token value.
	 * @returns The requested token.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if the provided value is empty.
	 */
	public static StringToken of(String value) {
		return new StringToken(value);
	}

	/** Token value. */
	private final String value;

	/**
	 * Constructor.
	 * @param value Token value.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if the provided value is empty.
	 */
	private StringToken(String value) {
		checkNotNull(value, "Null string tokens not allowed");
		checkArgument(value.length() > 0, "Empty string tokens not allowed");
		this.value = value;
	}

	/** Returns the token value. */
	public String getValue() {
		return value;
	}

	@Override
	void write(QS qs, Appendable a) {
		// TODO Auto-generated method stub
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof StringToken) {
			return value.equals(((StringToken) obj).value);
		}
		return false;
	}
}
