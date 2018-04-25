package com.tasktop.codejam.visualcodereview.gerrit.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ProgramModel {
	private List<TypeModel> types;
	private List<Relationship> relationships;

	public ProgramModel(List<TypeModel> types, List<Relationship> relationships) {
		this.types = ImmutableList.copyOf(types);
		this.relationships = ImmutableList.copyOf(relationships);
	}

	public List<TypeModel> getTypes() {
		return types;
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}
}
