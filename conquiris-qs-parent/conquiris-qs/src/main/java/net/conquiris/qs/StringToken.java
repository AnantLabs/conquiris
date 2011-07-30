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

import com.google.common.base.CharMatcher;

/**
 * String QS tokens.
 * @author Andres Rodriguez
 */
public final class StringToken {
	/** Character matcher to use. */
	private static final CharMatcher MATCHER = CharMatcher.WHITESPACE.or(CharMatcher.JAVA_ISO_CONTROL);
	
	/**
	 * Cleans a string value for a string token.
	 * @param string Input string.
	 * @return String value to use.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if the resulting string is empty.
	 */
	static String clean(String string) {
		checkNotNull(string, "Null string tokens not allowed");
		string = MATCHER.trimFrom(string);
		checkArgument(string.length() > 0, "Empty string tokens not allowed");
		return string;
	}
	
	/** Token value. */
	private final String value;
	
	/**
	 * Constructor.
	 * @param value Token value.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if the resulting string is empty.
	 */
	StringToken(String value) {
		this.value = clean(value);
	}
	
	/** Returns the token value. */
	public String getValue() {
		return value;
	}

}
