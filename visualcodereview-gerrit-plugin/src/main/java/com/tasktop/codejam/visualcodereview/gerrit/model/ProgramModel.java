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
