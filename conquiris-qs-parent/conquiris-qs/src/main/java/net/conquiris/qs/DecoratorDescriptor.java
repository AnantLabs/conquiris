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
import com.google.common.collect.MapMaker;

/**
 * Decorator token descriptor.
 * @author Andres Rodriguez
 */
final class DecoratorDescriptor {
	/** Descriptor map. */
	private static final Map<Class<?>, DecoratorDescriptor> MAP = new MapMaker().makeComputingMap(Computer.INSTANCE);

	/** Token type. */
	private final Class<?> tokenType;
	/** Qualifier type. */
	private final Class<?> qualifierType;

	/**
	 * Returns the descriptor for a token class.
	 * @param tokenType Token type.
	 * @return The decorator descriptos.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if the token type is not well-defined of the qualifier is of
	 *           an incorrect type.
	 */
	static DecoratorDescriptor get(Class<? extends DecoratorToken<?>> tokenType) {
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
		DecoratorDescriptor d = get(tokenType);
		checkArgument(d.qualifierType.isAssignableFrom(qualifier.getClass()),
				"Invalid type parameter: Not same class as qualifier.");
		return qualifier;
	}

	/**
	 * Constructor.
	 * @param tokenType Token type.
	 * @param qualifierType Qualifier type.
	 */
	private DecoratorDescriptor(Class<?> tokenType, Class<?> qualifierType) {
		this.tokenType = tokenType;
		this.qualifierType = qualifierType;
	}

	/** Returns the token type. */
	Class<?> getTokenType() {
		return tokenType;
	}

	/** Returns the qualifier type. */
	Class<?> getQualifierType() {
		return qualifierType;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(tokenType, qualifierType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof DecoratorDescriptor) {
			DecoratorDescriptor other = (DecoratorDescriptor) obj;
			return tokenType.equals(other.tokenType) && qualifierType.equals(other.qualifierType);
		}
		return false;
	}

	enum Computer implements Function<Class<?>, DecoratorDescriptor> {
		INSTANCE;

		public DecoratorDescriptor apply(Class<?> input) {
			Type superclass = input.getGenericSuperclass();
			checkArgument(!(superclass instanceof Class), "Missing type parameter.");
			Type argType = ((ParameterizedType) superclass).getActualTypeArguments()[0];
			checkArgument(argType instanceof Class, "Invalid type parameter: Not a class.");
			Class<?> argClass = ((Class<?>) argType);
			checkArgument(argClass.isEnum(), "Invalid type parameter: Not an enum.");
			return new DecoratorDescriptor(input, argClass);
		}
	}

}
