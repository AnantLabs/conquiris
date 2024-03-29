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

import java.net.URI;

import net.conquiris.api.index.IndexerService;
import net.conquiris.jersey.ConquirisJAXRS;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Factory for indexer service clients based on Jersey (JAX-RS).
 * @author Andres Rodriguez.
 */
public final class IndexerServiceClientFactory {
	/** Jersey client. */
	private final Client client;

	/** Creates a new factory. */
	public static IndexerServiceClientFactory create() {
		return new IndexerServiceClientFactory();
	}

	/** Constructor. */
	private IndexerServiceClientFactory() {
		ClientConfig config = new DefaultClientConfig();
		config.getClasses().addAll(ConquirisJAXRS.providers());
		client = Client.create(config);
	}

	/**
	 * Creates a new client.
	 * @param uri Service URI.
	 * @return The requested service client.
	 */
	public IndexerService get(URI uri) {
		WebResource resource = client.resource(checkNotNull(uri, "The indexer service URI must be provided"));
		return new IndexerServiceClient(resource);
	}

}
