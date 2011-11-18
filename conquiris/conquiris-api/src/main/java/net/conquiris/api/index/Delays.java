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

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Indexing delays specification. The delays are expressed in ms and must be >= 0, otherwise the
 * methods throw {@link IllegalArgumentException}.
 * <ul>
 * <li>The normal delay is applied when the indexer performed any work.</li>
 * <li>The idle delay is applied when the indexer performed to work.</li>
 * <li>The error delay is applied when the indexer threw an exception.</li>
 * </ul>
 * @author Andres Rodriguez
 */
public final class Delays implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 1969811896715857676L;
	/** Normal delay. */
	private final long normal;
	/** Idle delay. */
	private final long idle;
	/** Error delay. */
	private final long error;

	private static long checkNormal(long normal) {
		checkArgument(normal >= 0, "The normal delay must be >= 0");
		return normal;
	}

	private static long checkIdle(long idle) {
		checkArgument(idle >= 0, "The idle delay must be >= 0");
		return idle;
	}

	private static long checkError(long error) {
		checkArgument(error >= 0, "The error delay must be >= 0");
		return error;
	}

	/** Constructor. */
	private Delays(long normal, long idle, long error) {
		this.normal = checkNormal(normal);
		this.idle = checkIdle(idle);
		this.error = checkError(error);
	}

	/** Returns a constant delay specification. */
	public static Delays constant(long delay) {
		return new Delays(delay, delay, delay);
	}

	/**
	 * Returns a delay specification.
	 * @param normal Normal delay.
	 * @param idle Idle delay.
	 * @param error Error delay.
	 * @return The requested specification.
	 */
	public static Delays of(long normal, long idle, long error) {
		return new Delays(normal, idle, error);
	}

	/** Returns the normal delay. */
	public long getNormal() {
		return normal;
	}

	/** Returns a delay specification equal to this one but with the provided normal delay. */
	public Delays setNormal(long normal) {
		if (normal == this.normal) {
			return this;
		}
		return of(checkNormal(normal), this.idle, this.error);
	}

	/** Returns a delay specification equal to this one but with the provided idle delay. */
	public Delays setIdle(long idle) {
		if (idle == this.idle) {
			return this;
		}
		return of(this.normal, checkIdle(idle), this.error);
	}

	/** Returns a delay specification equal to this one but with the provided error delay. */
	public Delays setError(long error) {
		if (error == this.error) {
			return this;
		}
		return of(this.normal, this.idle, checkError(error));
	}

	/** Returns the idle delay. */
	public long getIdle() {
		return idle;
	}

	/** Returns the error delay. */
	public long getError() {
		return error;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(normal, idle, error);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Delays) {
			Delays other = (Delays) obj;
			return this.normal == other.normal && this.idle == other.idle && this.error == other.error;
		}
		return false;
	}

	// =================================================================
	// Serialization proxy

	private static class SerializationProxy implements Serializable {
		/** Serial UID. */
		private static final long serialVersionUID = -7402734122441533946L;
		/** Normal delay. */
		private final long normal;
		/** Idle delay. */
		private final long idle;
		/** Error delay. */
		private final long error;

		public SerializationProxy(Delays d) {
			this.normal = d.getNormal();
			this.idle = d.getIdle();
			this.error = d.getError();
		}

		private Object readResolve() {
			return Delays.of(normal, idle, error);
		}
	}

	private Object writeReplace() {
		return new SerializationProxy(this);
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required");
	}

}
