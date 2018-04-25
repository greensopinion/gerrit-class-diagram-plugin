package com.tasktop.codejam.visualcodereview.gerrit.diagram;

import static java.text.MessageFormat.format;

import java.io.IOException;

import org.eclipse.jgit.lib.Repository;

import com.tasktop.codejam.visualcodereview.gerrit.model.CommitModel;
import com.tasktop.codejam.visualcodereview.gerrit.model.CommitModelService;
import com.tasktop.codejam.visualcodereview.gerrit.model.FileModel;

public class DotGenerator {

	public String generate(Repository repository, String projectName, String commitHash) throws IOException {
		CommitModel commitModel = new CommitModelService(repository).create(commitHash);
		StringBuilder builder = new StringBuilder();
		builder.append("digraph G {\n");
		commitModel.getFiles().stream().filter(f -> f.getPath().endsWith(".java"))
				.forEach(file -> builder.append(entryOf(file)));
		builder.append("}\n");
		return builder.toString();
	}

	private String entryOf(FileModel file) {
		return format("{0};\n", nameOf(file.getPath()));
	}

	private String nameOf(String path) {
		int lastSlash = path.lastIndexOf('/');
		if (lastSlash != -1) {
			String filename = path.substring(lastSlash + 1, path.length());
			int lastDot = filename.lastIndexOf('.');
			if (lastDot != -1) {
				filename = filename.substring(0, lastDot);
			}
			return filename;
		}
		return path;
	}
}
