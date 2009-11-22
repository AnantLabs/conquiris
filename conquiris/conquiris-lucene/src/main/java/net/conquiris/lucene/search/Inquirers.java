/*
 * Copyright 2009 the original author or authors.
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
package net.conquiris.lucene.search;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import net.conquiris.api.Result;

import org.apache.lucene.search.Searcher;

/**
 * Basic and support methods for inquirers.
 * @author Andres Rodriguez
 */
public final class Inquirers {
	/** Not instantiable. */
	private Inquirers() {
		throw new AssertionError();
	}

	/**
	 * Performs an inquiry.
	 * @param <T> Result type.
	 * @param inquiry Inquiry to perform.
	 * @return The inquiry result.
	 * @throws IOException if an error occurs.
	 * @throws NullPointerException if any of the arguments is {@code null}.
	 */
	public static <T extends Result> T perform(Inquirable inquirable, Inquiry<T> inquiry) throws IOException {
		checkNotNull(inquirable, "An inquirable must be provided.");
		checkNotNull(inquiry, "An inquiry must be provided.");
		final long t0 = System.currentTimeMillis();
		final ResultBuilder<T> builder = inquiry.perform(inquirable);
		final long t1 = System.currentTimeMillis();
		checkNotNull(builder, "The inquiry returned a null ResultBuilder");
		final T result = builder.build(t1 - t0);
		checkNotNull(builder, "The ResultBuilder returned a null Result");
		return result;
	}

	/**
	 * Performs an inquiry.
	 * @param <T> Result type.
	 * @param inquiry Inquiry to perform.
	 * @return The inquiry result.
	 * @throws IOException if an error occurs.
	 * @throws NullPointerException if any of the arguments is {@code null}.
	 */
	public static <T extends Result> T perform(Searcher searcher, Inquiry<T> inquiry) throws IOException {
		return perform(InquirableSearcher.of(searcher), inquiry);
	}

}
