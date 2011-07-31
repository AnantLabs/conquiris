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

import java.io.IOException;

import com.google.common.base.Objects;

/**
 * Decorator QS tokens.
 * @author Andres Rodriguez
 * @param <T> Qualifier type.
 */
public abstract class DecoratorToken<T extends Enum<T>> extends Token {
	/** Qualifier. */
	private final T qualifier;
	/** Decorator argument. */
	private final QueryToken argument;

	/**
	 * Constructor.
	 * @param argument Decorator argument.
	 */
	protected DecoratorToken(T qualifier, QueryToken argument) {
		this.qualifier = DecoratorDescriptor.check(getClass(), qualifier);
		this.argument = checkNotNull(argument);
	}

	/** Returns the qualifier. */
	public final T getQualifier() {
		return qualifier;
	}

	/** Returns the decorator argument. */
	public final QueryToken getArgument() {
		return argument;
	}

	@Override
	final void write(QS qs, Appendable a) throws IOException {
		a.append('(');
		a.append(qualifier.name());
		a.append(' ');
		argument.write(false, qs, a);
		a.append(')');
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getClass(), qualifier, argument);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		DecoratorToken<T> other = (DecoratorToken<T>) obj;
		return qualifier == other.qualifier && argument.equals(other.argument);
	}

}
