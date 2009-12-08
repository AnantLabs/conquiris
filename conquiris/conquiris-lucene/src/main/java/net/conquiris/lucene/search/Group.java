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

import java.util.List;

import org.apache.lucene.document.Document;

/**
 * Interface for group payload calculators.
 * @author Andres Rodriguez
 * @param <T> Payload type.
 */
public interface Group<T> {
	/**
	 * Performs the payload calculation.
	 * @param path Group values.
	 * @param document Current document.
	 * @param hits Hits so far (including this).
	 * @param previous Current payload value ({@code null} for the first hit).
	 * @return New payload value.
	 */
	T perform(List<String> path, Document document, int hits, T previous);
}
