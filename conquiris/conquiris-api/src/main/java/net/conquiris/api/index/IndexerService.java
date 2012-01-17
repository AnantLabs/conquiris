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

/**
 * Interface for an indexer service.
 * @author Andres Rodriguez.
 */
public interface IndexerService {
	/**
	 * Returns a report from the index.
	 * @param level Desired level.
	 * @return The requested report.
	 */
	IndexReport getIndexReport(IndexReportLevel level);

	/**
	 * Set the delay specification.
	 * @param delays New delay specification.
	 */
	void setDelays(Delays delays);

	/** Starts the service. It is a no-op if the service is already running. */
	void start();

	/** Stops the service. It is a no-op if the service is already stopped. */
	void stop();

	/**
	 * Schedules a tasks that changes the index checkpoint. It is a no-op if the service is stopped or
	 * the activation policy is disabled. This operation is asynchronous.
	 */
	void setCheckpoint(@Nullable String checkpoint);

	/**
	 * Schedules a tasks that changes the index checkpoint. It is a no-op if the service is stopped or
	 * the activation policy is disabled. This operation is asynchronous.
	 */
	void reindex();
}
