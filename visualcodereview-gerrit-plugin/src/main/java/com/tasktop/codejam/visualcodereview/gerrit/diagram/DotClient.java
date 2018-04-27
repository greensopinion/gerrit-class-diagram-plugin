/**
 * Copyright 2018 Tasktop Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tasktop.codejam.visualcodereview.gerrit.diagram;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.common.io.CharStreams;

public class DotClient {
	private String host = "dot";

	public String retrieveSvg(String dot) {
		try {
			URL url = new URL(String.format("http://%s:8080/svg", host));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			try (Writer out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
				out.write(dot);
			}
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				throw new RuntimeException(String.format("HTTP %s", responseCode));
			}
			try (Reader reader = new InputStreamReader(connection.getInputStream())) {
				return CharStreams.toString(reader);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
