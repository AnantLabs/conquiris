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

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.Type;

import net.conquiris.api.index.Delays;
import net.conquiris.api.index.IndexInfo;
import net.conquiris.api.index.IndexReport;
import net.conquiris.api.index.IndexReportLevel;
import net.conquiris.api.index.IndexStatus;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson representation for {@link IndexReport}.
 * @author Andres Rodriguez
 */
public final class GsonIndexReport implements JsonSerializer<IndexReport>, JsonDeserializer<IndexReport> {
	/** Report level. */
	private static final String LEVEL = "level";
	/** Whether the index is started. */
	private static final String STARTED = "started";
	/** Whether the index is active (must be started). */
	private static final String ACTIVE = "active";
	/** Last known index status. */
	private static final String STATUS = "status";
	/** Index delays. Not included in basic reports. */
	private static final String DELAYS = "delays";
	/** Index info. Not included in basic reports. */
	private static final String INFO = "info";

	/** Constructor. */
	public GsonIndexReport() {
	}

	@Override
	public JsonElement serialize(IndexReport src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject object = new JsonObject();
		object.addProperty(LEVEL, src.getLevel().toString());
		object.addProperty(STARTED, src.isIndexStarted());
		object.addProperty(ACTIVE, src.isIndexActive());
		object.addProperty(STATUS, src.getIndexStatus().toString());
		if (src.getLevel() != IndexReportLevel.BASIC) {
			object.add(DELAYS, context.serialize(src.getDelays().get()));
			object.add(INFO, context.serialize(src.getInfo().get()));
		}
		return object;
	}

	@Override
	public IndexReport deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		checkArgument(json.isJsonObject(), "Expected JSON Object to deserialize IndexReport.");
		final JsonObject object = json.getAsJsonObject();
		final IndexReportLevel level = IndexReportLevel.valueOf(object.getAsJsonPrimitive(LEVEL).getAsString());
		final boolean started = object.getAsJsonPrimitive(STARTED).getAsBoolean();
		final boolean active = object.getAsJsonPrimitive(ACTIVE).getAsBoolean();
		final IndexStatus status = IndexStatus.valueOf(object.getAsJsonPrimitive(STATUS).getAsString());
		if (level == IndexReportLevel.BASIC) {
			return IndexReport.basic(started, active, status);
		}
		final Delays delays = context.deserialize(object.get(DELAYS), Delays.class);
		final IndexInfo info = context.deserialize(object.get(INFO), IndexInfo.class);
		if (level == IndexReportLevel.NORMAL) {
			return IndexReport.normal(started, active, status, delays, info);
		}
		return IndexReport.detailed(started, active, status, delays, info);
	}
}
