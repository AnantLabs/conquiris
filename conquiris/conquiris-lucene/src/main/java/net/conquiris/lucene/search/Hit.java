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
package net.conquiris.lucene.search;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import org.apache.lucene.document.Document;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Object representing a search hit.
 * @author Andres Rodriguez
 */
public final class Hit implements Supplier<Document> {
	/** Doc Id. */
	private final int docId;
	/** Hit score. */
	private final float score;
	/** Document. */
	private final Document document;
	/** Highlight fragments. */
	private final ImmutableMultimap<String, String> fragments;

	/**
	 * Creates a new hit.
	 * @param docId Document id.
	 * @param score Score.
	 * @param document Document.
	 * @param fragments Highlight fragments.
	 * @return
	 */
	public static Hit of(int docId, float score, Document document, @Nullable Multimap<String, String> fragments) {
		return new Hit(docId, score, document, fragments);
	}

	/**
	 * Constructor.
	 */
	private Hit(int docId, float score, Document document, @Nullable Multimap<String, String> fragments) {
		this.docId = docId;
		this.score = score;
		this.document = checkNotNull(document, "The document must be provided");
		if (fragments == null) {
			this.fragments = ImmutableMultimap.of();
		} else {
			this.fragments = ImmutableMultimap.copyOf(fragments);
		}
	}

	/** Returns the document id. */
	public int getDocId() {
		return docId;
	}

	/** Returns the score. */
	public float getScore() {
		return score;
	}

	/** Returns the document. */
	@Override
	public Document get() {
		return document;
	}

	/** Returns the highlight fragments (never {@code null}). */
	public Multimap<String, String> getFragments() {
		return fragments;
	}
}
