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
package net.conquiris.lucene;

import net.derquinse.common.base.NotInstantiable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;

import com.google.common.base.Supplier;

/**
 * Support class the default values related to the lucene version for this conquiris version.
 * @author Andres Rodriguez
 */
public final class Conquiris extends NotInstantiable {
	/** Not instantiable. */
	private Conquiris() {
	}

	/** Supplier. */
	private static final IndexWriterConfigSupplier SUPPLIER = new IndexWriterConfigSupplier();

	/** Returns the used lucene version. */
	public static Version version() {
		return Version.LUCENE_36;
	}

	/** Returns the default standard analyzer. */
	public static Analyzer standardAnalyzer() {
		return new StandardAnalyzer(version());
	}

	/** Returns a new instance of the default writer configuration. */
	public static IndexWriterConfig writerConfig() {
		return new IndexWriterConfig(version(), standardAnalyzer());
	}

	/** Returns the default writer configuration supplier. */
	public static Supplier<IndexWriterConfig> writerConfigSupplier() {
		return SUPPLIER;
	}

	/** Default IndexWriterConfig provider. */
	private static final class IndexWriterConfigSupplier implements Supplier<IndexWriterConfig> {
		IndexWriterConfigSupplier() {
		}

		public IndexWriterConfig get() {
			return Conquiris.writerConfig();
		}

		@Override
		public String toString() {
			return "Default IndexWriterConfig provider";
		}
	}

}
