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
package net.conquiris.jersey.server;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.conquiris.jersey.IndexerServiceResources.CHECKPOINT;
import static net.conquiris.jersey.IndexerServiceResources.DELAYS;
import static net.conquiris.jersey.IndexerServiceResources.QP_LEVEL;
import static net.conquiris.jersey.IndexerServiceResources.REINDEX;
import static net.conquiris.jersey.IndexerServiceResources.START;
import static net.conquiris.jersey.IndexerServiceResources.STOP;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.conquiris.api.index.Delays;
import net.conquiris.api.index.IndexReport;
import net.conquiris.api.index.IndexReportLevel;
import net.conquiris.api.index.IndexerService;

/**
 * Indexer service resource. This class can be used as a sub-resource or subclassed to become a root
 * resource.
 * @author Andres Rodriguez.
 */
public class IndexerServiceResource {
	/** Indexer service. */
	private final IndexerService service;

	/**
	 * Constructor.
	 * @param service Real service.
	 */
	public IndexerServiceResource(IndexerService service) {
		this.service = checkNotNull(service, "The indexer service must be provided");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public final IndexReport getIndexReport(@PathParam(QP_LEVEL) String level) {
		IndexReportLevel reportLevel = IndexReportLevel.BASIC;
		try {
			reportLevel = Enum.valueOf(IndexReportLevel.class, level);
		} catch (Exception e) {
			// We keep BASIC
		}
		return service.getIndexReport(reportLevel);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(DELAYS)
	public void setDelays(Delays delays) {
		service.setDelays(delays);
	}

	@POST
	@Path(START)
	public void start() {
		service.start();
	}

	@POST
	@Path(STOP)
	public void stop() {
		service.stop();
	}

	@PUT
	@Path(CHECKPOINT)
	public void setCheckpoint(String checkpoint) {
		service.setCheckpoint(checkpoint);
	}

	@GET
	@Path(CHECKPOINT)
	public String getCheckpoint() {
		return service.getIndexReport(IndexReportLevel.NORMAL).getInfo().get().getCheckpoint();
	}

	@POST
	@Path(REINDEX)
	public void reindex() {
		service.stop();
	}

}
