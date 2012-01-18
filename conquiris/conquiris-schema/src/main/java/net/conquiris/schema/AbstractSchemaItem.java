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
 * Base class for schema item implementations.
 * @author Andres Rodriguez
 */
abstract class AbstractSchemaItem implements SchemaItem {
	/** Field name. */
	private final String name;
	/** Minimum number of occurrences. */
	private final int minOccurs;
	/** Maximum number of occurrences. */
	private final int maxOccurs;
	/** Whether the field is stored. */
	private boolean stored;
	/** Whether the field is indexed. */
	private boolean indexed;
	
	AbstractSchemaItem(String name, int minOccurs, int maxOccurs, boolean stored, boolean indexed) {
		this.name = NAME.checkValue(name);
		this.minOccurs = minOccurs;
		this.maxOccurs = maxOccurs;
		this.stored = stored;
		this.indexed = indexed;
	}

	/*
	 * (non-Javadoc)
	 * @see net.conquiris.schema.SchemaItem#getName()
	 */
	@Override
	public final String getName() {
		return name;
	}
	
	public int getMinOccurs() {
		
	}


	/** Maximum number of occurrences property. */
	IntegerMetaProperty<AbstractSchemaItem> MAX_OCCURS = new IntegerMetaProperty<AbstractSchemaItem>("maxOccurs", true) {
		@Override
		public Integer apply(AbstractSchemaItem input) {
			return input.getMaxOccurs();
		}
	};

	/** Returns the maximum number of occurrences. */
	int getMaxOccurs();

	/** Stored field flag. */
	MetaFlag<AbstractSchemaItem> STORED = new MetaFlag<AbstractSchemaItem>("stored") {
		@Override
		public boolean apply(AbstractSchemaItem input) {
			return input.isStored();
		}
	};

	/** Whether the field is stored. */
	boolean isStored();

	/** Indexed field flag. */
	MetaFlag<AbstractSchemaItem> INDEXED = new MetaFlag<AbstractSchemaItem>("indexed") {
		@Override
		public boolean apply(AbstractSchemaItem input) {
			return input.isIndexed();
		}
	};

	/** Whether the field is indexed. */
	boolean isIndexed();

	/** Required field flag. */
	MetaFlag<AbstractSchemaItem> REQUIRED = new MetaFlag<AbstractSchemaItem>("required") {
		@Override
		public boolean apply(AbstractSchemaItem input) {
			return input.isRequired();
		}
	};

	/** Whether the field is required, ie, the minimum number of occurrences is greater than zero. */
	boolean isRequired();

}
