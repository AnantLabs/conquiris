/*
 * Copyright (C) the original author or authors.
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
package net.conquiris.search;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicLong;

import net.conquiris.api.search.IndexNotAvailableException;
import net.conquiris.api.search.Reader;
import net.conquiris.api.search.ReaderSupplier;
import net.conquiris.api.search.SearchException;

/**
 * Abstract class for reader supplier implementations. This class takes care of the requests count
 * and exception transformation.
 * @author Andres Rodriguez
 */
abstract class AbstractReaderSupplier implements ReaderSupplier {
	/** Request count. */
	private final AtomicLong requests = new AtomicLong();

	/**
	 * Constructor.
	 */
	AbstractReaderSupplier() {
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.ReaderSupplier#get()
	 */
	@Override
	public final Reader get() {
		try {
			Reader r = checkNotNull(doGet(), "Null reader supplied");
			requests.incrementAndGet();
			return r;
		} catch (SearchException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexNotAvailableException(e);
		}
	}

	abstract Reader doGet() throws Exception;

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.search.ReaderSupplier#getRequested()
	 */
	@Override
	public final long getRequested() {
		return requests.get();
	}
}
