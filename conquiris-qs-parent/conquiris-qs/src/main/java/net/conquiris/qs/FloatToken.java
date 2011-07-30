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

import com.google.common.primitives.Floats;

/**
 * Float QS tokens.
 * @author Andres Rodriguez
 */
public final class FloatToken {
	/** Token value. */
	private final float value;

	/**
	 * Constructor.
	 * @param value Token value.
	 */
	FloatToken(float value) {
		this.value = value;
	}

	/** Returns the token value. */
	public float getValue() {
		return value;
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
