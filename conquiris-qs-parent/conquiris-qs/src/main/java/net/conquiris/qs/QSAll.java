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

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.ImmutableList;

/**
 * Special match all documents query tokens.
 * @author Andres Rodriguez
 */
@ThreadSafe
public final class QSAll extends QueryToken {
	private static final QSAll INSTANCE = new QSAll();

	/** Returns the single instance. */
	public static QSAll get() {
		return INSTANCE;
	}

	/**
	 * Constructor.
	 */
	private QSAll() {
		super(ImmutableList.<Token> of());
	}

	@Override
	void write(QS qs, Appendable a) throws IOException {
		a.append("()");
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
