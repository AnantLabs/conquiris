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

import static net.conquiris.api.search.CountResult.empty;
import static net.conquiris.api.search.CountResult.of;
import net.derquinse.common.test.EqualityTests;
import net.derquinse.common.test.HessianSerializabilityTests;
import net.derquinse.common.test.SerializabilityTests;

import org.testng.annotations.Test;

/**
 * Tests for Result.
 * @author Andres Rodriguez
 */
public class CountResultTest {

	/** Equality. */
	@Test
	public void equality() {
		EqualityTests.many(empty(), empty(), empty());
		EqualityTests.many(of(1, 0.0f, 10), of(1, 0.0f, 10), of(1, 0.0f, 10));
	}

	/** Serializability. */
	@Test
	public void serializability() {
		SerializabilityTests.check(empty());
		HessianSerializabilityTests.both(empty());
		SerializabilityTests.check(of(1, 0.0f, 10));
		HessianSerializabilityTests.both(of(1, 0.0f, 10));
	}
}
