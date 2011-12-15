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
package net.conquiris.gson;

import net.conquiris.api.index.Delays;
import net.conquiris.api.index.IndexInfo;
import net.conquiris.api.index.IndexReport;
import net.conquiris.api.index.IndexStatus;
import net.derquinse.common.test.GsonSerializabilityTests;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Tests for IndexBasicReport.
 * @author Andres Rodriguez
 */
public class GsonIndexReportTest {
	private static final IndexInfo INFO = IndexInfo.fromMap(ImmutableMap.of(IndexInfo.CHECKPOINT, "test", "key",
			"value"));
	private static final IndexReport BASIC = IndexReport.basic(true, false, IndexStatus.CORRUPT);
	private static final IndexReport NORMAL = IndexReport.normal(true, true, IndexStatus.IOERROR, Delays.constant(4L), INFO);
	private static final IndexReport DETAILED = IndexReport.detailed(true, true, IndexStatus.OK, Delays.constant(10L), INFO);

	/** Serializability. */
	@Test
	public void serializability() {
		GsonSerializabilityTests.check(ConquirisGson.get(), BASIC);
		GsonSerializabilityTests.check(ConquirisGson.get(), NORMAL);
		GsonSerializabilityTests.check(ConquirisGson.get(), DETAILED);
	}
}
