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

import static net.conquiris.api.search.PageResult.empty;
import static net.conquiris.api.search.PageResult.found;
import static net.conquiris.api.search.PageResult.notFound;
import net.derquinse.common.test.EqualityTests;
import net.derquinse.common.test.HessianSerializabilityTests;
import net.derquinse.common.test.SerializabilityTests;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

/**
 * Tests for PageResult.
 * @author Andres Rodriguez
 */
public class PageResultTest {
	private static final ImmutableList<Integer> LIST = ImmutableList.of(1, 2, 3, 4, 5);

	/** Equality. */
	@Test
	public void equality() {
		EqualityTests.many(empty(), empty(), empty());
		EqualityTests.many(found(51, 0.0f, 10, 42, LIST), found(51, 0.0f, 10, 42, LIST), found(51, 0.0f, 10, 42, LIST));
		EqualityTests.many(notFound(14, 0), notFound(14, 0), notFound(14, 0));
	}

	/** Serializability. */
	@Test
	public void serializability() {
		SerializabilityTests.check(empty());
		HessianSerializabilityTests.both(empty());
		SerializabilityTests.check(found(51, 0.0f, 10, 42, LIST));
		HessianSerializabilityTests.both(found(51, 0.0f, 10, 42, LIST));
		SerializabilityTests.check(notFound(14, 0));
		HessianSerializabilityTests.both(notFound(14, 0));
	}
}
