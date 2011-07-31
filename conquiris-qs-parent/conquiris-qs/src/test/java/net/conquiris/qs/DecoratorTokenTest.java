/*
 * Copyright 2008-2011 the original author or authors.
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
package net.conquiris.qs;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

/**
 * Tests for DecoratorToken.
 * @author Andres Rodriguez
 */
public class DecoratorTokenTest {
	/** Constructor. */
	public DecoratorTokenTest() {
	}

	/** Generics test. */
	@Test
	public void generic() {
		TimeUnitToken t = new TimeUnitToken(TimeUnit.SECONDS, MatchAll.get());
		assertEquals(t.getArgument(), MatchAll.get());
		assertEquals(t.getQualifier(), TimeUnit.SECONDS);
	}
}
