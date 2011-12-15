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

import static com.google.common.base.Preconditions.checkNotNull;
import net.conquiris.api.index.Delays;
import net.conquiris.api.index.IndexInfo;
import net.conquiris.api.index.IndexReport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Conquiris Gson support class.
 * @author Andres Rodriguez
 */
public final class ConquirisGson {
	/** Not instantiable. */
	private ConquirisGson() {
		throw new AssertionError();
	}

	/** Shared instance. */
	private static volatile Gson instance = null;

	/**
	 * Decorates a {@link GsonBuilder} with conquiris clases.
	 * @param builder Builder to decorate.
	 * @return The provided builder for method chaining.
	 */
	public static GsonBuilder decorate(GsonBuilder builder) {
		checkNotNull(builder, "The builder to decorate must be provided");
		builder.registerTypeAdapter(Delays.class, new GsonDelays());
		builder.registerTypeAdapter(IndexInfo.class, new GsonIndexInfo());
		builder.registerTypeAdapter(IndexReport.class, new GsonIndexReport());
		return builder;
	}

	/** Creates the shared decorated instance. */
	private static synchronized void create() {
		if (instance == null) {
			instance = decorate(new GsonBuilder()).create();
		}
	}

	/** Returns a shared decorated instance. */
	public static Gson get() {
		if (instance == null) {
			create();
		}
		return instance;
	}
}
