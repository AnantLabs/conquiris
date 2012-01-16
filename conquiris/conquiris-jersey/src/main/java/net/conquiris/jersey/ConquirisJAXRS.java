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
package net.conquiris.jersey;

import com.google.common.collect.ImmutableSet;

/**
 * Information regarding Conquiris JAX-RS services.
 * @author Andres Rodriguez.
 */
public final class ConquirisJAXRS {
	/** Not instantiable. */
	private ConquirisJAXRS() {
		throw new AssertionError();
	}

	/** Providers. */
	private static final ImmutableSet<Class<?>> PROVIDERS = ImmutableSet.<Class<?>> of(DelaysProvider.class,
			IndexInfoProvider.class, IndexReportProvider.class);

	/** Returns the Conquiris JAX-RS providers. */
	public static ImmutableSet<Class<?>> providers() {
		return PROVIDERS;
	}
}
