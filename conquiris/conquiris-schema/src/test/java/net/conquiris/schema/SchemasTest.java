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
package net.conquiris.schema;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import net.derquinse.common.test.EqualityTests;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

/**
 * Tests for Schemas.
 */
public final class SchemasTest {
	private static final String T = "T";
	private static final String I = "I";
	private static final String B = "B";

	@Test
	public void empty() {
		Schema s = Schemas.empty();
		assertNotNull(s);
		assertTrue(s.isEmpty());
		EqualityTests.one(s);
	}

	@Test
	public void regular() {
		Schema s = Schemas.of(SchemaItems.text(T, true, true, true, true), SchemaItems.intValue(I, true, true));
		assertNotNull(s);
		assertFalse(s.isEmpty());
		EqualityTests.one(s);
		SchemaItem i = SchemaItems.binary(B);
		Schema s2 = s.extend(ImmutableList.of(i));
		Schema s3 = s.override(ImmutableList.of(i));
		Schema s4 = Schemas.of(i, SchemaItems.text(T, true, true, true, true), SchemaItems.intValue(I, true, true));
		EqualityTests.many(s2, s3, s4);
	}

}
