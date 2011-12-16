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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

/**
 * Conquiris index writer interface. Commit properties starting with the 'cq:' prefix are reserved.
 * If no state modifiers methods are called nothing will be commited to the index. Implementations
 * MUST BE thread-safe. All methods throw {@link IllegalStateException} if the writer can no longer
 * be used, {@link InterruptedException} if the thread they are running is interrupted and
 * {@link IndexException} if there's a problem with the index.
 * @author Andres Rodriguez
 */
public interface Writer {
	/**
	 * Cancels the operation of this writer. Indexer can continue but all operations will be ignored
	 * and the index writer will be rolled back.
	 */
	void cancel() throws InterruptedException;

	/**
	 * Returns the last commit index info.
	 */
	IndexInfo getIndexInfo() throws InterruptedException;

	/**
	 * Returns the commit checkpoint, or {@code null} if there was no checkpoint. Includes the
	 * modifications performed in this writer.
	 */
	String getCheckpoint() throws InterruptedException;

	/**
	 * Returns the last known target checkpoint, or {@code null} if there was no checkpoint. Includes
	 * the modifications performed in this writer.
	 */
	String getTargetCheckpoint() throws InterruptedException;

	/**
	 * Returns a commit property. Includes the modifications performed in this writer.
	 * @param key Property key.
	 * @return The property value or {@code null} if the property does no exist.
	 * @throws IllegalArgumentException if a reserved property is requested.
	 */
	String getProperty(String key) throws InterruptedException;

	/**
	 * Returns the existing property keys. Includes the modifications performed in this writer.
	 * @return The (unmodifiable) set of existing property keys.
	 */
	Set<String> getPropertyKeys() throws InterruptedException;

	/**
	 * Sets the new checkpoint. It will only be considered a modifying operation if the checkpoint is
	 * different.
	 * @param checkpoint New checkpoint.
	 * @return This writer for method chaining.
	 */
	Writer setCheckpoint(@Nullable String checkpoint) throws InterruptedException;

	/**
	 * Sets the new target checkpoint. It will only be considered a modifying operation if the
	 * checkpoint is different.
	 * @param targetCheckpoint New target checkpoint.
	 * @return This writer for method chaining.
	 */
	Writer setTargetCheckpoint(@Nullable String targetCheckpoint) throws InterruptedException;

	/**
	 * Sets a commit property value.
	 * @param key Property key.
	 * @param value Value to set. If {@code null} the property is removed.
	 * @return This writer for method chaining.
	 * @throws IllegalArgumentException if a provided key is reserverd.
	 */
	Writer setProperty(String key, String value) throws InterruptedException;

	/**
	 * Sets a collection of commit property value.
	 * @param values Values to set. For those properties which value is {@code null} the property will
	 *          be removed.
	 * @return This writer for method chaining.
	 * @throws IllegalArgumentException if any of the provided key is reserved.
	 */
	Writer setProperties(Map<String, String> values) throws InterruptedException;

	/**
	 * Adds a document. The service default analyzer will be used.
	 * @param document Document to add. If {@code null} the method is a no-op.
	 * @return This writer for method chaining.
	 */
	Writer add(@Nullable Document document) throws InterruptedException, IndexException;

	/**
	 * Adds a document.
	 * @param document Document to add. If {@code null} the method is a no-op.
	 * @param analyzer Analyzer to use. If {@code null} the service default analyzer will be used.
	 * @return This writer for method chaining.
	 */
	Writer add(@Nullable Document document, @Nullable Analyzer analyzer) throws InterruptedException, IndexException;

	/**
	 * Deletes all documents.
	 * @return This writer for method chaining.
	 */
	Writer deleteAll() throws InterruptedException, IndexException;

	/**
	 * Deletes documents matching a certain term.
	 * @param field Field to match. If {@code null} the method is a no-op.
	 * @param text Term text to match. If {@code null} the method is a no-op.
	 * @return This writer for method chaining.
	 */
	Writer delete(@Nullable String field, @Nullable String text) throws InterruptedException, IndexException;

	/**
	 * Deletes documents matching a certain term.
	 * @param term Term to match. If {@code null} the method is a no-op.
	 * @return This writer for method chaining.
	 */
	Writer delete(@Nullable Term term) throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param field Field to match. If {@code null} no deletion will be performed.
	 * @param text Term text to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @return This writer for method chaining.
	 */
	Writer update(@Nullable String field, @Nullable String text, @Nullable Document document)
			throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param term Term to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @return This writer for method chaining.
	 */
	Writer update(@Nullable Term term, @Nullable Document document) throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param field Field to match. If {@code null} no deletion will be performed.
	 * @param text Term text to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @param analyzer Analyzer to use. If {@code null} the service default analyzer will be used.
	 * @return This writer for method chaining.
	 */
	Writer update(@Nullable String field, @Nullable String text, @Nullable Document document, @Nullable Analyzer analyzer)
			throws InterruptedException, IndexException;

	/**
	 * Atomically (with respect to index flushing) deletes the documents matching a certain term and
	 * adds a document.
	 * @param term Term to match. If {@code null} no deletion will be performed.
	 * @param document Document to add. If {@code null} no document will be added.
	 * @param analyzer Analyzer to use. If {@code null} the service default analyzer will be used.
	 * @return This writer for method chaining.
	 */
	Writer update(@Nullable Term term, @Nullable Document document, @Nullable Analyzer analyzer)
			throws InterruptedException, IndexException;

	/**
	 * Runs a collection of subindexers using the provided executor, waiting for them to finish.
	 * @param executor Executor to use.
	 * @param subindexers Indexers to run.
	 * @return This writer for method chaining.
	 */
	Writer runSubindexers(Executor executor, Iterable<? extends Indexer> subindexers) throws InterruptedException,
			IndexException;

}
