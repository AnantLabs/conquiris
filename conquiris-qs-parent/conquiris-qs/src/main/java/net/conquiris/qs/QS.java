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

/**
 * QS main parsing and serializing class.
 * @author Andres Rodriguez
 */
public final class QS {
	private QueryToken checkQuery(QueryToken query) {
		return checkNotNull(query, "Null query");
	}

	/** Default constructor. */
	QS() {
	}

	public void write(QueryToken query, Appendable a) throws IOException {
		doWrite(checkNotNull(query), a);
	}

	public void doWrite(QueryToken query, Appendable a) throws IOException {
		query.write(true, null, a);
		// TODO
	}

	public String write(QueryToken query) {
		checkQuery(query);
		StringBuilder b = new StringBuilder();
		try {
			write(query, b);
		} catch (IOException e) {
			// Should not happen
		}
		return b.toString();
	}

	public QueryToken parse(String string) {
		// TODO;
		return MatchAll.get();
	}

}
