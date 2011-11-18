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
package net.conquiris.api.search;

import net.conquiris.api.index.Delays;
import net.derquinse.common.test.EqualityTests;
import net.derquinse.common.test.HessianSerializabilityTests;
import net.derquinse.common.test.SerializabilityTests;

import org.testng.annotations.Test;

/**
 * Tests for Delays.
 * @author Andres Rodriguez
 */
public class DelaysTest {
	private static final Delays ZERO = Delays.constant(0L);
	private static final Delays ONE = Delays.constant(1L);
	private static final Delays OTHER = Delays.of(1L, 2L, 3L);

	/** Equality. */
	@Test
	public void equality() {
		EqualityTests.many(ZERO, ZERO.setNormal(0L), ZERO.setIdle(0L), ZERO.setError(0L), Delays.of(0, 0, 0));
		EqualityTests.many(ONE, ONE.setNormal(1L), ONE.setIdle(1L), ONE.setError(1L), Delays.of(1, 1, 1));
		EqualityTests.many(OTHER, ZERO.setNormal(1L).setIdle(2L).setError(3L), Delays.of(1, 2, 3));
	}

	/** Serializability. */
	@Test
	public void serializability() {
		SerializabilityTests.check(ZERO);
		HessianSerializabilityTests.both(ZERO);
		SerializabilityTests.check(ONE);
		HessianSerializabilityTests.both(ONE);
		SerializabilityTests.check(OTHER);
		HessianSerializabilityTests.both(OTHER);
	}
}
