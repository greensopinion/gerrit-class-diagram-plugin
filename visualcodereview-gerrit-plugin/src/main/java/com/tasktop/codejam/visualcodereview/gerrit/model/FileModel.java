package com.tasktop.codejam.visualcodereview.gerrit.model;

import org.eclipse.jgit.lib.AbbreviatedObjectId;

public class FileModel {

	private AbbreviatedObjectId id;
	private String path;

	public FileModel(AbbreviatedObjectId id, String path) {
		this.id = id;
		this.path = path;
	}

	public AbbreviatedObjectId getId() {
		return id;
	}

	public String getPath() {
		return path;
	}
}
