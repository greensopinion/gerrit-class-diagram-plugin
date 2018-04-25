package com.tasktop.codejam.visualcodereview.gerrit;

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
