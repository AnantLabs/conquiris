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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Conquiris Lucene document implementation.
 * @author Andres Rodriguez
 */
final class SchemaImpl extends ForwardingMap<String, SchemaItem> implements Schema {
	private final ImmutableBiMap<String, SchemaItem> map;

	/**
	 * Constructor.
	 * @param map Schema items.
	 */
	SchemaImpl(Map<String, SchemaItem> map) {
		this.map = ImmutableBiMap.copyOf(map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.common.collect.ForwardingMap#delegate()
	 */
	@Override
	protected BiMap<String, SchemaItem> delegate() {
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.common.collect.BiMap#forcePut(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SchemaItem forcePut(String key, SchemaItem value) {
		return delegate().forcePut(key, value);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.common.collect.BiMap#inverse()
	 */
	@Override
	public BiMap<SchemaItem, String> inverse() {
		return delegate().inverse();
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.common.collect.BiMap#values()
	 */
	@Override
	public Set<SchemaItem> values() {
		return delegate().values();
	}

	private Schema extendWithMap(Map<String, SchemaItem> items) {
		if (items.isEmpty()) {
			return this;
		}
		if (isEmpty()) {
			return new SchemaImpl(items);
		}
		Set<String> intersect = Sets.intersection(map.keySet(), items.keySet());
		checkArgument(intersect.isEmpty(), "Extending with existing keys %s", intersect);
		return putItems(items);
	}

	private Schema putItems(Map<String, SchemaItem> items) {
		final Map<String, SchemaItem> newMap = Maps.newHashMap(map);
		newMap.putAll(items);
		return new SchemaImpl(newMap);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.Schema#extend(java.lang.Iterable)
	 */
	@Override
	public Schema extend(Iterable<? extends SchemaItem> items) {
		// Never written.
		@SuppressWarnings("unchecked")
		Iterable<SchemaItem> i = (Iterable<SchemaItem>) items;
		Map<String, SchemaItem> others = Maps.uniqueIndex(i, SchemaItem.NAME);
		if (others.isEmpty()) {
			return this;
		}
		return extendWithMap(others);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.Schema#extend(net.conquiris.schema.SchemaItem[])
	 */
	@Override
	public Schema extend(SchemaItem... items) {
		return extend(Arrays.asList(items));
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.Schema#extend(net.conquiris.schema.Schema)
	 */
	@Override
	public Schema extend(Schema schema) {
		checkNotNull(schema, "The schema must be provided");
		if (isEmpty()) {
			return schema;
		}
		return extendWithMap(schema);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.Schema#override(java.lang.Iterable)
	 */
	@Override
	public Schema override(Iterable<? extends SchemaItem> items) {
		// Never written.
		@SuppressWarnings("unchecked")
		Iterable<SchemaItem> i = (Iterable<SchemaItem>) items;
		Map<String, SchemaItem> others = Maps.uniqueIndex(i, SchemaItem.NAME);
		if (others.isEmpty()) {
			return this;
		}
		return putItems(others);
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.Schema#override(net.conquiris.schema.SchemaItem[])
	 */
	@Override
	public Schema override(SchemaItem... items) {
		return override(Arrays.asList(items));
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.Schema#override(net.conquiris.schema.Schema)
	 */
	@Override
	public Schema override(Schema schema) {
		checkNotNull(schema, "The schema must be provided");
		if (isEmpty()) {
			return schema;
		}
		if (schema.isEmpty()) {
			return this;
		}
		return putItems(schema);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.common.collect.ForwardingMap#hashCode()
	 */
	@Override
	public int hashCode() {
		return map.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.common.collect.ForwardingMap#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof Schema) {
			return map.equals(object);
		}
		return false;
	}

}
