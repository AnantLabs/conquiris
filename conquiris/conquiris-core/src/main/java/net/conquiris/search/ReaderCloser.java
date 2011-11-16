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

import org.apache.lucene.index.IndexReader;
import org.slf4j.LoggerFactory;

/**
 * Reader closer task.
 * @author Andres Rodriguez
 */
final class ReaderCloser implements Runnable {
	/** Reader to close. */
	private final IndexReader reader;

	ReaderCloser(IndexReader reader) {
		this.reader = checkNotNull(reader, "The reade to close must be provided");
	}

	private String msg(String format) {
		return String.format(format, reader);
	}

	@Override
	public void run() {
		try {
			this.reader.close();
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(msg("Unable to close reader [%s]"), e);
		}
	}

	@Override
	public String toString() {
		return msg("IndexReader closer task [%s]");
	}
}
