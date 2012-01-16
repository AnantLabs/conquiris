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
package net.conquiris.jersey.client;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.conquiris.jersey.IndexerServiceResources.CHECKPOINT;
import static net.conquiris.jersey.IndexerServiceResources.DELAYS;
import static net.conquiris.jersey.IndexerServiceResources.QP_LEVEL;
import static net.conquiris.jersey.IndexerServiceResources.REINDEX;
import static net.conquiris.jersey.IndexerServiceResources.START;
import static net.conquiris.jersey.IndexerServiceResources.STOP;
import net.conquiris.api.index.Delays;
import net.conquiris.api.index.IndexReport;
import net.conquiris.api.index.IndexReportLevel;
import net.conquiris.api.index.IndexerService;

import com.sun.jersey.api.client.WebResource;

/**
 * Indexer service clients based on Jersey (JAX-RS).
 * @author Andres Rodriguez.
 */
final class IndexerServiceClient implements IndexerService {
	/** Root resource. */
	private final WebResource resource;

	IndexerServiceClient(WebResource resource) {
		this.resource = checkNotNull(resource, "The root resource must be provided");
	}

	@Override
	public IndexReport getIndexReport(IndexReportLevel level) {
		checkNotNull(level, "The index report level must be provided");
		return resource.queryParam(QP_LEVEL, level.toString()).get(IndexReport.class);
	}

	@Override
	public void setDelays(Delays delays) {
		checkNotNull(delays, "The delays configuration must be provided");
		resource.path(DELAYS).put(delays);
	}

	@Override
	public void start() {
		resource.path(START).post();
	}

	@Override
	public void stop() {
		resource.path(STOP).post();
	}

	@Override
	public void setCheckpoint(String checkpoint) {
		resource.path(CHECKPOINT).put(checkpoint);
	}

	@Override
	public void reindex() {
		resource.path(REINDEX).post();
	}

}
