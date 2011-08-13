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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Query token constructor.
 * @author Andres Rodriguez
 */
final class QueryConstructor {
	/** Argument kind. */
	private enum Kind {
		SINGLE, ARRAY;
	};

	/** Constructor. */
	private final Constructor<?> constructor;
	/** Token arguments types. */
	private final ImmutableList<Class<? extends Token>> types;
	/** Last argument kind. */
	private final Kind last;

	/**
	 * Returns the descriptor for a query token constructor.
	 * @param constructor Constructor to use.
	 * @return The constructor descriptor.
	 * @throws IllegalArgumentException if the constructor if not valid for a descriptor.
	 */
	static QueryConstructor of(Constructor<?> constructor) {
		checkNotNull(constructor);
		checkArgument(Modifier.isPublic(constructor.getModifiers()), "Not a public constructor");
		Class<?>[] types = constructor.getParameterTypes();
		int n = types.length;
		checkArgument(n > 0, "Constructor with no arguments");
		List<Class<? extends Token>> list = Lists.newArrayListWithCapacity(n);
		Kind k = Kind.SINGLE;
		for (int i = 0; i < n; i++) {
			Class<?> type = types[i];
			if (i < (n - 1) || !(constructor.isVarArgs() && type.isArray())) {
				list.add(checkType(type));
			} else {
				// Last argument is vararg
				list.add(checkType(type.getComponentType()));
				k = Kind.ARRAY;
			}
		}
		// TODO
		return null;
	}

	private static Class<? extends Token> checkType(Class<?> type) {
		// TODO
		return null;
	}

	/**
	 * Constructor.
	 * @param constructor Constructor to use.
	 * @param types Argument types.
	 * @param last Last argument kind.
	 */
	private QueryConstructor(Constructor<?> constructor, Iterable<Class<? extends Token>> types,
			Kind last) {
		this.constructor = constructor;
		this.types = ImmutableList.copyOf(types);
		this.last = last;
	}

	@Override
	public int hashCode() {
		return constructor.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof QueryConstructor) {
			QueryConstructor other = (QueryConstructor) obj;
			return constructor.equals(other.constructor);
		}
		return false;
	}

}
