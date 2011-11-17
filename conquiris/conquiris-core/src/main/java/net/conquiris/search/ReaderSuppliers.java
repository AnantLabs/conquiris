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
package net.conquiris.search;

import net.conquiris.api.search.ManagedReaderSupplier;
import net.conquiris.api.search.ReaderSupplier;

import org.apache.lucene.store.Directory;

/**
 * Reader suppliers support class.
 * @author Andres Rodriguez
 */
public final class ReaderSuppliers {
	/** Not instantiable. */
	private ReaderSuppliers() {
		throw new AssertionError();
	}

	/** Returns a non-reopenable reader supplier using an empty index. */
	public static ReaderSupplier empty() {
		return EmptyIndex.getInstance();
	}

	/**
	 * Returns an unmanaged reader supplier using a directory.
	 * @param directory Directory to use.
	 */
	public static ReaderSupplier directory(Directory directory) {
		return new DirectoryReaderSupplier(directory);
	}

	/**
	 * Returns a managed reader supplier based on another one.
	 * @param supplier Source reader supplier.
	 * @param holdTime Reader hold time (ms). If negative, zero will be used.
	 */
	public static ManagedReaderSupplier managed(ReaderSupplier supplier, long holdTime) {
		return new DefaultManagedReaderSupplier(supplier, holdTime);
	}

	/**
	 * Returns a managed reader supplier based on another one with no hold time.
	 * @param supplier Source reader supplier.
	 */
	public static ManagedReaderSupplier managed(ReaderSupplier supplier) {
		return managed(supplier, 0);
	}

	/**
	 * Returns a managed reader supplier based on a directory.
	 * @param directory Directory to use.
	 * @param holdTime Reader hold time (ms). If negative, zero will be used.
	 */
	public static ReaderSupplier managed(Directory directory, long holdTime) {
		return managed(directory(directory), holdTime);
	}

	/**
	 * Returns a managed reader supplier based on a directory with no hold time.
	 * @param directory Directory to use.
	 * @param holdTime Reader hold time (ms). If negative, zero will be used.
	 */
	public static ReaderSupplier managed(Directory directory) {
		return managed(directory(directory), 0);
	}

}
