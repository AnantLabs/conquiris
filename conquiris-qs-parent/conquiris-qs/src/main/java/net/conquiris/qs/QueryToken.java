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

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * Base class for query QS tokens.
 * @author Andres Rodriguez
 */
public abstract class QueryToken extends Token {
	/** Token arguments. */
	private final ImmutableList<Token> arguments;

	/**
	 * Helper method to call the constructor.
	 * @param t Token.
	 * @return Constructor argument.
	 */
	protected static Iterable<Token> tokens(Token t) {
		return ImmutableList.of(t);
	}

	/**
	 * Helper method to call the constructor.
	 * @param tokens Array of tokens.
	 * @return Constructor argument.
	 */
	protected static Iterable<Token> tokens(Token... tokens) {
		return asList(tokens);
	}

	/**
	 * Helper method to call the constructor.
	 * @param a Iterable of tokens.
	 * @param b Array of tokens.
	 * @return Constructor argument.
	 */
	protected static Iterable<Token> tokens(Iterable<? extends Token> a, Token... b) {
		return concat(a, tokens(b));
	}
	
	/**
	 * Helper method to call the constructor.
	 * @param tokens Array of strings.
	 * @return Constructor argument.
	 */
	protected static Iterable<StringToken> strings(String... strings) {
		return transform(Arrays.asList(strings), StringToken.STRING2TOKEN);
	}
	

	/**
	 * Constructor.
	 * @param arguments Query arguments.
	 * @throws NullPointerException if any of the arguments is null.
	 */
	protected QueryToken(Iterable<? extends Token> arguments) {
		this.arguments = ImmutableList.copyOf(arguments);
	}

	/** Returns the query arguments. */
	public ImmutableList<Token> getArguments() {
		return arguments;
	}

	/**
	 * Returns an argument with a specific token type.
	 * @param i Argument index.
	 * @param type Argument type.
	 * @return The requested argument.
	 * @throws IndexOutOfBoundsException if the index is incorrect.
	 */
	public <T extends Token> T getArgument(int i, Class<T> type) {
		return type.cast(arguments.get(i));
	}

	@Override
	void write(QS qs, Appendable a) throws IOException {
		a.append('(');
		a.append(qs.getQueryKey(this));
		a.append(' ');
		for (Token t : arguments) {
			t.write(false, qs, a);
		}
		a.append(')');
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getClass(), arguments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		return arguments.equals(((QueryToken) obj).arguments);
	}

}
