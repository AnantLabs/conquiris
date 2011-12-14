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
import net.derquinse.common.test.GsonSerializabilityTests;

import org.testng.annotations.Test;

/**
 * Tests for GsonDelays.
 * @author Andres Rodriguez
 */
public class GsonDelaysTest {
	private static final Delays ZERO = Delays.constant(0L);
	private static final Delays ONE = Delays.constant(1L);
	private static final Delays OTHER = Delays.of(1L, 2L, 3L);

	/** Serializability. */
	@Test
	public void serializability() {
		GsonSerializabilityTests.check(ConquirisGson.get(), ZERO);
		GsonSerializabilityTests.check(ConquirisGson.get(), ONE);
		GsonSerializabilityTests.check(ConquirisGson.get(), OTHER);
	}
}
