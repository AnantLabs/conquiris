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
 * String-related methods.
 * @author Andres Rodriguez
 */
public final class QSStrings {
	/** Not instantiable. */
	private QSStrings() {
		throw new AssertionError();
	}

	/** Character matcher to use for separators. */
	static final CharMatcher SEPARATOR = CharMatcher.WHITESPACE.or(CharMatcher.JAVA_ISO_CONTROL);

	/** Character matcher for not a java identifier part. */
	private static final CharMatcher NOT_JAVA_IDENTIFIER_PART = new CharMatcher() {
		@Override
		public boolean matches(char c) {
			return !Character.isJavaIdentifierPart(c);
		}
	};

	static String toVisibleString(char c) {
		if (CharMatcher.INVISIBLE.matches(c)) {
			Integer.toHexString(c);
		}
		return Character.toString(c);
	}

	/**
	 * Checks that a string is a valid key.
	 * @param key String to check.
	 * @return The provided key.
	 * @throws IllegalArgumentException if the string is empty.
	 * @throws IllegalKeyException if the string contains invalid characters.
	 */
	public static String checkKey(String key) {
		checkNotNull(key, "Null keys not allowed");
		int n = key.length();
		checkArgument(n > 0, "Empty keys not allowed");
		char c = key.charAt(0);
		checkArgument(Character.isJavaIdentifierStart(c), "Invalid key start character [%s]", c);
		if (n > 1) {
			int bad = NOT_JAVA_IDENTIFIER_PART.indexIn(key, 1);
			if (bad > 0) {
				throw new IllegalKeyException(key, key.charAt(bad));
			}
		}
		return key;
	}
}
