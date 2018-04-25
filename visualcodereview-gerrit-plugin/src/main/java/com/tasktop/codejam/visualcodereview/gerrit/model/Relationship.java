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
