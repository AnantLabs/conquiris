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

import net.derquinse.common.meta.IntegerMetaProperty;
import net.derquinse.common.meta.MetaFlag;
import net.derquinse.common.meta.StringMetaProperty;

/**
 * Conquiris Lucene document schema item base interface. Schema items MUST be immutable.
 * @author Andres Rodriguez
 */
public interface SchemaItem extends IsStoredFlag, IsIndexedFlag {
	/** Name property. */
	StringMetaProperty<SchemaItem> NAME = new StringMetaProperty<SchemaItem>("name", true) {
		@Override
		public String apply(SchemaItem input) {
			return input.getName();
		}
	};

	/** Returns the field name. */
	String getName();

	/** Minimum number of occurrences property. */
	IntegerMetaProperty<SchemaItem> MIN_OCCURS = new IntegerMetaProperty<SchemaItem>("minOccurs", true) {
		@Override
		public Integer apply(SchemaItem input) {
			return input.getMinOccurs();
		}
	};

	/** Returns the minimum number of occurrences. */
	int getMinOccurs();

	/** Maximum number of occurrences property. */
	IntegerMetaProperty<SchemaItem> MAX_OCCURS = new IntegerMetaProperty<SchemaItem>("maxOccurs", true) {
		@Override
		public Integer apply(SchemaItem input) {
			return input.getMaxOccurs();
		}
	};

	/** Returns the maximum number of occurrences. */
	int getMaxOccurs();

	/** Required field flag. */
	MetaFlag<SchemaItem> REQUIRED = new MetaFlag<SchemaItem>("required") {
		@Override
		public boolean apply(SchemaItem input) {
			return input.isRequired();
		}
	};

	/** Whether the field is required, ie, the minimum number of occurrences is greater than zero. */
	boolean isRequired();

}
