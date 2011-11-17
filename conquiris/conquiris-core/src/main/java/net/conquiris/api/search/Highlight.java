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
package net.conquiris.api.search;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;

import com.google.common.base.Preconditions;

/**
 * Highlighter configuration.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class Highlight {
	private final Map<String, Integer> fields;
	private final Analyzer analyzer;
	private final Formatter formatter;

	private Highlight(Analyzer analyzer, Formatter formatter, Map<String, Integer> fields) {
		this.analyzer = analyzer;
		this.formatter = formatter;
		this.fields = fields;
	}

	public static Highlight of(Analyzer analyzer, Formatter formatter, Map<String, Integer> fields) {
		Preconditions.checkNotNull(analyzer);
		Preconditions.checkNotNull(formatter);
		Preconditions.checkNotNull(fields);
		return new Highlight(analyzer, formatter, fields);
	}

	public static Highlight of(Map<String, Integer> fields) {
		Preconditions.checkNotNull(fields);
		return new Highlight(new StandardAnalyzer(Version.LUCENE_34), new SimpleHTMLFormatter(), fields);
	}

	public static Highlight no() {
		return new Highlight(new StandardAnalyzer(Version.LUCENE_34), new SimpleHTMLFormatter(),
				new HashMap<String, Integer>());
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public Map<String, Integer> getFields() {
		return fields;
	}

}
