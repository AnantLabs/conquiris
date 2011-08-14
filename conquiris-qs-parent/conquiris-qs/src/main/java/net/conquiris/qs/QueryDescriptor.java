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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ComputationException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;

/**
 * Query token descriptor.
 * @author Andres Rodriguez
 */
final class QueryDescriptor {
	/** Descriptor map. */
	private static final Map<Class<?>, QueryDescriptor> MAP = new MapMaker().makeComputingMap(Computer.INSTANCE);
	
	/** Argument kind. */
	private enum Kind {
		SINGLE, ARRAY;
	};

	/** Token type. */
	private final Class<?> tokenType;
	/** Token arguments types. */
	private final ImmutableList<Class<? extends Token>> types;
	/** Last argument kind. */
	private final Kind last;

	/**
	 * Returns the descriptor for a token class.
	 * @param tokenType Token type.
	 * @return The decorator descriptos.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if the token type is not well-defined of the qualifier is of
	 *           an incorrect type.
	 */
	static QueryDescriptor get(Class<? extends DecoratorToken<?>> tokenType) {
		try {
			return MAP.get(tokenType);
		} catch (ComputationException e) {
			throw (RuntimeException) e.getCause();
		}
	}

	/**
	 * Checks the token class is correctly defined and that the quealifier is also correct.
	 * @param tokenType Token type.
	 * @param qualifier Qualifier value.
	 * @return The qualifier value.
	 * @throws NullPointerException if any argument is {@code null}.
	 * @throws IllegalArgumentException if the token type is not well-defined of the qualifier is of
	 *           an incorrect type.
	 */
	static <E extends Enum<E>, T extends DecoratorToken<?>> E check(Class<T> tokenType, E qualifier) {
		checkNotNull(tokenType, "The decorator token type must be provided");
		checkNotNull(qualifier, "The qualifier value type must be provided");
		QueryDescriptor d = get(tokenType);
		checkArgument(d.qualifierType.isAssignableFrom(qualifier.getClass()),
				"Invalid type parameter: Not same class as qualifier.");
		return qualifier;
	}

	/**
	 * Constructor.
	 * @param tokenType Token type.
	 * @param qualifierType Qualifier type.
	 */
	private QueryDescriptor(Class<?> tokenType, Class<?> qualifierType) {
		this.tokenType = tokenType;
		this.qualifierType = qualifierType;
	}

	/** Returns the token type. */
	Class<?> getTokenType() {
		return tokenType;
	}

	@Override
	public int hashCode() {
		return tokenType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof QueryDescriptor) {
			QueryDescriptor other = (QueryDescriptor) obj;
			return tokenType.equals(other.tokenType);
		}
		return false;
	}

	enum Computer implements Function<Class<?>, QueryDescriptor> {
		INSTANCE;

		public QueryDescriptor apply(Class<?> input) {
			// Look for a suitable constructor
			
			Type superclass = input.getGenericSuperclass();
			checkArgument(!(superclass instanceof Class), "Missing type parameter.");
			Type argType = ((ParameterizedType) superclass).getActualTypeArguments()[0];
			checkArgument(argType instanceof Class, "Invalid type parameter: Not a class.");
			Class<?> argClass = ((Class<?>) argType);
			checkArgument(argClass.isEnum(), "Invalid type parameter: Not an enum.");
			return new QueryDescriptor(input, argClass);
		}
	}

}
