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

import java.io.IOException;

import net.conquiris.api.Result;

/**
 * Interface for inquirers.
 * @author Andres Rodriguez
 */
public interface Inquirer {
	/**
	 * Performs an inquiry.
	 * @param <T> Result type.
	 * @param inquiry Inquiry to perform.
	 * @return The inquiry result.
	 * @throws IOException if an error occurs.
	 */
	<T extends Result> T perform(Inquiry<T> inquiry) throws IOException;
}
