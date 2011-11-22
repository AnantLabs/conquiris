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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for indexing related exceptions.
 * @author Andres Rodriguez
 */
public class IndexException extends RuntimeException {
	/** Serial UID. */
	private static final long serialVersionUID = 8007298606805370822L;
	/** Suggested status. */
	private final IndexStatus status;

	public IndexException(IndexStatus status) {
		super();
		this.status = checkNotNull(status);
	}

	public IndexException(IndexStatus status, String message, Throwable cause) {
		super(message, cause);
		this.status = checkNotNull(status);
	}

	public IndexException(IndexStatus status, String message) {
		super(message);
		this.status = checkNotNull(status);
	}

	public IndexException(IndexStatus status, Throwable cause) {
		super(cause);
		this.status = checkNotNull(status);
	}

	public IndexException() {
		this(IndexStatus.IOERROR);
	}

	public IndexException(String message, Throwable cause) {
		this(IndexStatus.IOERROR, message, cause);
	}

	public IndexException(String message) {
		this(IndexStatus.IOERROR, message);
	}

	public IndexException(Throwable cause) {
		this(IndexStatus.IOERROR, cause);
	}

	/** Returns the suggested status. */
	public final IndexStatus getStatus() {
		return status;
	}
}
