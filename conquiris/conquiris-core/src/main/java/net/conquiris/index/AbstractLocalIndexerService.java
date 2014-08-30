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

import static com.google.common.base.Preconditions.checkNotNull;
import static net.conquiris.api.index.IndexerActivationPolicies.alwaysActive;

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import net.conquiris.api.index.Delays;
import net.conquiris.api.index.IndexReport;
import net.conquiris.api.index.IndexReportLevel;
import net.conquiris.api.index.IndexerActivationPolicy;
import net.conquiris.api.index.LocalIndexerService;
import net.derquinse.common.log.ContextLog;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.util.concurrent.Atomics;

/**
 * Abstract base class for a {@link LocalIndexerService}. Includes the convenience methods.
 * @author Andres Rodriguez.
 */
public abstract class AbstractLocalIndexerService implements LocalIndexerService {
	/** Default delay configuration. */
	private static final Delays DEFAULT_DELAYS = Delays.constant(5000);

	/** Delays. */
	private volatile Delays delays = DEFAULT_DELAYS;
	/** Activation policy. */
	private volatile IndexerActivationPolicy activationPolicy = alwaysActive();
	/** Service name. */
	private volatile String name = null;
	/** Logger to use. */
	private final AtomicReference<ContextLog> logRef = Atomics.newReference();
	/** Logger to use for the writer. */
	private final AtomicReference<ContextLog> writerLogRef = Atomics.newReference();

	/** Constructor. */
	protected AbstractLocalIndexerService() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.conquiris.api.index.IndexerService#getIndexReport(net.conquiris.api.index.IndexReportLevel)
	 */
	@Override
	public final IndexReport getIndexReport(IndexReportLevel level) {
		switch (checkNotNull(level, "The index report level must be provided")) {
		case BASIC:
			return IndexReport.basic(isIndexStarted(), isIndexActive(), getIndexStatus());
		case NORMAL:
			return IndexReport.normal(isIndexStarted(), isIndexActive(), getIndexStatus(), getDelays(), getIndexInfo());
		case DETAILED:
			return IndexReport.detailed(isIndexStarted(), isIndexActive(), getIndexStatus(), getDelays(), getIndexInfo());
		default:
			throw new AssertionError(); // should not happen
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.LocalIndexerService#getDelays()
	 */
	@Override
	public final Delays getDelays() {
		return delays;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.IndexerService#setDelays(net.conquiris.api.index.Delays)
	 */
	public final void setDelays(Delays delays) {
		this.delays = checkNotNull(delays, "The delays configuration must be provided");
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.api.index.IndexActiveFlag#isIndexActive()
	 */
	@Override
	public final boolean isIndexActive() {
		try {
			return activationPolicy.isActive() && isIndexStarted();
		} catch (RuntimeException e) {
			log().error(e, "Unable to get current activation state");
		}
		return false;
	}

	/**
	 * Sets the activation policy.
	 * @param activationPolicy Policy to use. If {@code null} an always active policy will be used.
	 */
	public void setActivationPolicy(@Nullable IndexerActivationPolicy activationPolicy) {
		this.activationPolicy = MoreObjects.firstNonNull(activationPolicy, alwaysActive());
	}

	/* Service name. */

	/** Returns the name of the service or the class name if no one was provided. */
	public final String getName() {
		if (name == null) {
			return getClass().getSimpleName();
		}
		return name;
	}

	/** Sets the object name. */
	public final void setName(String name) {
		if (!Objects.equal(name, this.name)) {
			this.name = name;
			logRef.set(null);
			writerLogRef.set(null);
		}
	}

	/* Logging. */

	/**
	 * Returns the format to use for the service log context. Must include exactly one string
	 * parameter for the service name.
	 */
	protected String logContextFormat() {
		return "IndexerService[%s]";
	}

	/**
	 * Returns the base log to use.
	 */
	protected ContextLog baseLog() {
		return ContextLog.of("net.conquiris.index");
	}

	/**
	 * Returns the context log to use for the index service.
	 */
	protected final ContextLog log() {
		while (true) {
			ContextLog log = logRef.get();
			if (log != null) {
				return log;
			}
			log = baseLog().to(String.format(logContextFormat(), getName()));
			logRef.compareAndSet(null, log);
		}
	}

	/**
	 * Returns the context log to use for the index writer.
	 */
	protected final ContextLog writerLog() {
		while (true) {
			ContextLog log = writerLogRef.get();
			if (log != null) {
				return log;
			}
			log = log().to("writer");
			writerLogRef.compareAndSet(null, log);
		}
	}

}
