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
package net.conquiris.jersey;

/**
 * Resource constants for IndexerService RESTful resources.
 * @author Andres Rodriguez.
 */
public final class IndexerServiceResources {
	/** Not instantiable. */
	private IndexerServiceResources() {
		throw new AssertionError();
	}

	/** Report level query parameter. */
	public static final String QP_LEVEL = "l";

	/** Delays resource. */
	public static final String DELAYS = "delays";

	/** Start resource. */
	public static final String START = "delays";

	/** Stop resource. */
	public static final String STOP = "stop";

	/** Checkpoint resource. */
	public static final String CHECKPOINT = "checkpoint";

	/** Reindex resource. */
	public static final String REINDEX = "reindex";
}
