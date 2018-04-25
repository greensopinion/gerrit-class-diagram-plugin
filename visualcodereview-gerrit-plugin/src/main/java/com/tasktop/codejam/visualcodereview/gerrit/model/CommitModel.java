package com.tasktop.codejam.visualcodereview.gerrit.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class CommitModel {

	private List<FileModel> files;

	public CommitModel(List<FileModel> files) {
		this.files = ImmutableList.copyOf(files);
	}

	public List<FileModel> getFiles() {
		return files;
	}
}
