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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * Base class for query QS tokens.
 * @author Andres Rodriguez
 */
public abstract class QueryToken {
	/** Token arguments. */
	private final ImmutableList<Token> arguments;

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
