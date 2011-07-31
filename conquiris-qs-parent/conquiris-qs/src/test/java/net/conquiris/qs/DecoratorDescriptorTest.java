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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

/**
 * Tests for DecoratorDescriptor.
 * @author Andres Rodriguez
 */
public class DecoratorDescriptorTest {
	/** Constructor. */
	public DecoratorDescriptorTest() {
	}

	/** Get method. */
	@Test
	public void get() {
		DecoratorDescriptor d1 = DecoratorDescriptor.get(TimeUnitToken.class);
		assertNotNull(d1);
		DecoratorDescriptor d2 = DecoratorDescriptor.get(TimeUnitToken.class);
		assertNotNull(d2);
		assertTrue(d1 == d2);
	}

	/** Check method. */
	@Test(dependsOnMethods = "get")
	public void check() {
		assertEquals(DecoratorDescriptor.check(TimeUnitToken.class, TimeUnit.SECONDS), TimeUnit.SECONDS);
	}

}
