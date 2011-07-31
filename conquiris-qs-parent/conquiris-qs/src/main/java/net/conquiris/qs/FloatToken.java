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

import java.io.IOException;

import com.google.common.primitives.Floats;

/**
 * Float QS tokens.
 * @author Andres Rodriguez
 */
final class FloatToken extends Token {
	/** Token value. */
	private final float value;

	/**
	 * Builds a token with the specified value.
	 * @param value Token value.
	 * @returns The requested token.
	 */
	static FloatToken of(float value) {
		return new FloatToken(value);
	}

	/**
	 * Constructor.
	 * @param value Token value.
	 */
	private FloatToken(float value) {
		this.value = value;
	}

	/** Returns the token value. */
	public float getValue() {
		return value;
	}

	@Override
	void write(QS qs, Appendable a) throws IOException {
		a.append(Float.toString(value));
	}

	@Override
	public int hashCode() {
		return Floats.hashCode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof FloatToken) {
			return value == ((FloatToken) obj).value;
		}
		return false;
	}
}
