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

public class Relationship {

	private RelationshipType type;
	private CardinalityType cardinality;
	private String sourceFullyQualifiedName;
	private String targetFullyQualifiedName;

	public Relationship(RelationshipType type, CardinalityType cardinality, String sourceFullyQualifiedName,
			String targetFullyQualifiedName) {
		this.type = type;
		this.cardinality = cardinality;
		this.sourceFullyQualifiedName = sourceFullyQualifiedName;
		this.targetFullyQualifiedName = targetFullyQualifiedName;
	}

	public RelationshipType getType() {
		return type;
	}

	public CardinalityType getCardinality() {
		return cardinality;
	}

	public String getSourceFullyQualifiedName() {
		return sourceFullyQualifiedName;
	}

	public String getTargetFullyQualifiedName() {
		return targetFullyQualifiedName;
	}
}
