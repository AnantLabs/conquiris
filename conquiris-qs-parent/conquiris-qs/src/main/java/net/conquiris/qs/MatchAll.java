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

import com.google.common.collect.ImmutableList;

/**
 * Special match all documents query QS tokens.
 * @author Andres Rodriguez
 */
public final class MatchAll extends QueryToken {
	private static final MatchAll INSTANCE = new MatchAll();

	/**
	 * Constructor.
	 */
	private MatchAll() {
		super(ImmutableList.<Token> of());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this == INSTANCE;
	}

}
