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

import net.derquinse.common.meta.MetaFlag;

/**
 * Marker interface for textual schema items.
 * @author Andres Rodriguez
 */
public interface TextSchemaItem extends SchemaItem {
	/** Tokenized field flag. */
	MetaFlag<TextSchemaItem> TOKENIZED = new MetaFlag<TextSchemaItem>("tokenized") {
		@Override
		public boolean apply(TextSchemaItem input) {
			return input.isTokenized();
		}
	};

	/** Whether the field is tokenized. */
	boolean isTokenized();

	/** Norms stored flag. */
	MetaFlag<TextSchemaItem> NORMS = new MetaFlag<TextSchemaItem>("norms") {
		@Override
		public boolean apply(TextSchemaItem input) {
			return input.isNorms();
		}
	};

	/** Whether norms are stored. */
	boolean isNorms();

	/** Term vectors stored flag. */
	MetaFlag<TextSchemaItem> VECTORS = new MetaFlag<TextSchemaItem>("vectors") {
		@Override
		public boolean apply(TextSchemaItem input) {
			return input.isVectors();
		}
	};

	/** Whether term vectors are stored. */
	boolean isVectors();

	/** Term vectors positions stored flag. */
	MetaFlag<TextSchemaItem> POSITIONS = new MetaFlag<TextSchemaItem>("positions") {
		@Override
		public boolean apply(TextSchemaItem input) {
			return input.isPositions();
		}
	};

	/** Whether term vectors are stored with positions. */
	boolean isPositions();

	/** Term vectors positions stored flag. */
	MetaFlag<TextSchemaItem> OFFSETS = new MetaFlag<TextSchemaItem>("offsets") {
		@Override
		public boolean apply(TextSchemaItem input) {
			return input.isOffsets();
		}
	};

	/** Whether term vectors are stored with offsets. */
	boolean isOffsets();
}
