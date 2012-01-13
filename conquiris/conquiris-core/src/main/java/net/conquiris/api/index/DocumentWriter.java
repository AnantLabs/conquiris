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
package net.conquiris.api.index;

import javax.annotation.Nullable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

/**
 * Conquiris document writing interface. Implementations MUST BE thread-safe. All methods throw
 * {@link IllegalStateException} if the writer can no longer be used, {@link InterruptedException}
 * if the thread they are running is interrupted and {@link IndexException} if there's a problem
 * with the index.
 * @author Andres Rodriguez
 */
public interface DocumentWriter {
	/**
	 * Adds a document. The service default analyzer will be used.
	 * @param document Document to add. If {@code null} the method is a no-op.
	 * @return This writer for method chaining.
	 */
	DocumentWriter add(@Nullable Document document) throws InterruptedException, IndexException;

	/**
	 * Adds a document.
	 * @param document Document to add. If {@code null} the method is a no-op.
	 * @param analyzer Analyzer to use. If {@code null} the service default analyzer will be used.
	 * @return This writer for method chaining.
	 */
	DocumentWriter add(@Nullable Document document, @Nullable Analyzer analyzer) throws InterruptedException,
			IndexException;

	/**
	 * Deletes all documents.
	 * @return This writer for method chaining.
	 */
	DocumentWriter deleteAll() throws InterruptedException, IndexException;

	/**
	 * Deletes documents matching a certain term.
	 * @param field Field to match. If {@code null} the method is a no-op.
	 * @param text Term text to match. If {@code null} the method is a no-op.
	 * @return This writer for method chaining.
	 */
	DocumentWriter delete(@Nullable String field, @Nullable String text) throws InterruptedException, IndexException;

	/**
	 * Deletes documents matching a certain term.
	 * @param term Term to match. If {@code null} the method is a no-op.
	 * @return This writer for method chaining.
	 */
	DocumentWriter delete(@Nullable Term term) throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param field Field to match. If {@code null} no deletion will be performed.
	 * @param text Term text to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @return This writer for method chaining.
	 */
	DocumentWriter update(@Nullable String field, @Nullable String text, @Nullable Document document)
			throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param term Term to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @return This writer for method chaining.
	 */
	DocumentWriter update(@Nullable Term term, @Nullable Document document) throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param field Field to match. If {@code null} no deletion will be performed.
	 * @param text Term text to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @param analyzer Analyzer to use. If {@code null} the service default analyzer will be used.
	 * @return This writer for method chaining.
	 */
	DocumentWriter update(@Nullable String field, @Nullable String text, @Nullable Document document,
			@Nullable Analyzer analyzer) throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param term Term to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @param analyzer Analyzer to use. If {@code null} the service default analyzer will be used.
	 * @return This writer for method chaining.
	 */
	DocumentWriter update(@Nullable Term term, @Nullable Document document, @Nullable Analyzer analyzer)
			throws InterruptedException, IndexException;
}
