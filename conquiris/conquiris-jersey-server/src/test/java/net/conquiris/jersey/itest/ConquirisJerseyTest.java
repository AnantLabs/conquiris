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
package net.conquiris.jersey.itest;

import java.util.Set;

import net.conquiris.api.index.Delays;
import net.conquiris.api.index.IndexReport;
import net.conquiris.api.index.IndexReportLevel;
import net.conquiris.api.index.IndexerService;
import net.conquiris.jersey.ConquirisJAXRS;
import net.conquiris.jersey.client.IndexerServiceClientFactory;

import org.junit.Assert;
import org.junit.Test;
import org.testng.internal.annotations.Sets;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;

/**
 * Integration test for Jersey conquiris service.
 * @author Andres Rodriguez
 */
public class ConquirisJerseyTest extends JerseyTest {

	private static LowLevelAppDescriptor descriptor() {
		Set<Class<?>> set = Sets.newHashSet();
		set.add(TestIndexerResource.class);
		set.addAll(ConquirisJAXRS.providers());
		Class<?>[] classes = new Class<?>[set.size()];
		set.toArray(classes);
		return new LowLevelAppDescriptor.Builder(classes).build();
	}

	public ConquirisJerseyTest() {
		super(descriptor());
	}

	@Test
	public void test() {
		IndexerService server = TestIndexerServiceProvider.get();
		IndexerService client = IndexerServiceClientFactory.create().get(getBaseURI());
		IndexReport report = client.getIndexReport(IndexReportLevel.BASIC);
		report = client.getIndexReport(IndexReportLevel.NORMAL);
		IndexReport serverReport = server.getIndexReport(IndexReportLevel.NORMAL);
		Assert.assertEquals(serverReport.getDelays().get(), report.getDelays().get());
		client.setDelays(Delays.constant(30L));
		report = client.getIndexReport(IndexReportLevel.DETAILED);
		Assert.assertEquals(server.getIndexReport(IndexReportLevel.NORMAL).getDelays().get(), report.getDelays().get());
	}
}
