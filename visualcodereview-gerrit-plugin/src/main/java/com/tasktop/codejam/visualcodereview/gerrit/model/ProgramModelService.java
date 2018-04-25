package com.tasktop.codejam.visualcodereview.gerrit.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Repository;

public class ProgramModelService {
	private Repository repository;

	public ProgramModelService(Repository repository) {
		this.repository = repository;
	}

	public ProgramModel create(CommitModel commitModel) {
		List<TypeModel> types = new ArrayList<>();
		List<Relationship> relationships = new ArrayList<>();
		commitModel.getFiles().stream().filter(f -> f.getPath().endsWith(".java"))
				.forEach(file -> parse(file, types, relationships));
		return new ProgramModel(types, relationships);
	}

	private void parse(FileModel file, List<TypeModel> types, List<Relationship> relationships) {
		// FIXME
		types.add(new TypeModel(TypeType.CLASS, "unknown", nameOf(file.getPath())));
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
