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
package net.conquiris.index;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;

import net.conquiris.api.index.IndexException;
import net.conquiris.api.index.Writer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

import com.google.common.base.Predicate;

/**
 * Abstract writer implementation. Includes the convenience methods.
 * @author Andres Rodriguez.
 */
abstract class AbstractWriter implements Writer {
	/** Checkpoint property. */
	static final String CHECKPOINT = RESERVED_PREFIX + "checkpoint";
	/** Timestamp property. */
	static final String TIMESTAMP = RESERVED_PREFIX + "timestamp";
	/** Sequence property. */
	static final String SEQUENCE = RESERVED_PREFIX + "sequence";
	/** Reserved property filter. */
	static final Predicate<String> IS_RESERVED = new Predicate<String>() {
		@Override
		public boolean apply(String input) {
			return input != null && input.startsWith(RESERVED_PREFIX);
		}
	};
	/** User property entry filter. */
	static final Predicate<Entry<String, String>> IS_USER_PROPERTY = new Predicate<Entry<String, String>>() {
		@Override
		public boolean apply(Entry<String, String> input) {
			String k = input.getKey();
			return k != null && input.getValue() != null && !IS_RESERVED.apply(k);
		}
	};

	/** Checks whether a term or any of its properties is null. */
	static boolean isTermNull(Term term) {
		return term == null || term.field() == null || term.text() == null;
	}

	/** Check a property key is valid. */
	static String checkKey(String key) {
		checkNotNull("key", "Null user property keys not allowed");
		checkArgument(!IS_RESERVED.apply(key), "Reserved key");
		return key;
	}

	/** Constructor. */
	AbstractWriter() {
	}

	/**
	 * Ensures operations can be performed on the writer.
	 * @return True if the operations performed may be considered. That is, the writer has not been
	 *         cancelled and no previous error has been registered.
	 * @throws InterruptedException If the writer task has been interrupted.
	 * @throws IllegalStateException if the writer is no longer available.
	 */
	abstract boolean ensureAvailable() throws InterruptedException;

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#add(org.apache.lucene.document.Document)
	 */
	@Override
	public final Writer add(Document document) throws InterruptedException, IndexException {
		if (ensureAvailable() && document != null) {
			add(document, null);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public final Writer delete(String field, String text) throws InterruptedException, IndexException {
		if (ensureAvailable() && field != null && text != null) {
			delete(new Term(field, text));
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#update(java.lang.String, java.lang.String,
	 * org.apache.lucene.document.Document)
	 */
	@Override
	public final Writer update(String field, String text, Document document) throws InterruptedException, IndexException {
		if (ensureAvailable() && field != null && text != null && document != null) {
			update(new Term(field, text), document, null);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.Writer#update(java.lang.String, java.lang.String,
	 * org.apache.lucene.document.Document, org.apache.lucene.analysis.Analyzer)
	 */
	@Override
	public final Writer update(String field, String text, Document document, Analyzer analyzer)
			throws InterruptedException, IndexException {
		if (ensureAvailable() && field != null && text != null && document != null) {
			update(new Term(field, text), document, analyzer);
		}
		return this;
	}

	@Override
	public final Writer update(Term term, Document document) throws InterruptedException, IndexException {
		if (ensureAvailable() && !isTermNull(term) && document != null) {
			update(term, document, null);
		}
		return this;
	}

}
