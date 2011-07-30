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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * Decorator QS tokens.
 * @author Andres Rodriguez
 */
public abstract class DecoratorToken {
	/** Decorator argument. */
	private final QueryToken argument;

	/**
	 * Constructor.
	 * @param argument Decorator argument.
	 */
	protected DecoratorToken(QueryToken argument) {
		this.argument = checkNotNull(argument);
	}

	/** Returns the decorator argument. */
	public QueryToken getArgument() {
		return argument;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getClass(), argument);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		return argument.equals(((DecoratorToken) obj).argument);
	}

}
