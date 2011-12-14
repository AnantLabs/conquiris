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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson representation for {@link Delays}.
 * @author Andres Rodriguez
 */
public final class GsonDelays implements JsonSerializer<Delays>, JsonDeserializer<Delays> {
	/** Constructor. */
	public GsonDelays() {
	}

	@Override
	public JsonElement serialize(Delays src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		array.add(new JsonPrimitive(src.getNormal()));
		array.add(new JsonPrimitive(src.getIdle()));
		array.add(new JsonPrimitive(src.getError()));
		return array;
	}

	@Override
	public Delays deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		checkArgument(json.isJsonArray(), "Expected JSON Array to deserialize Delays object.");
		final JsonArray array = json.getAsJsonArray();
		checkArgument(array.size() >= 3, "Expected JSON Array with at least 3 arguments to deserialize Delays object.");
		return Delays.of(array.get(0).getAsLong(), array.get(1).getAsLong(), array.get(2).getAsLong());
	}
}
