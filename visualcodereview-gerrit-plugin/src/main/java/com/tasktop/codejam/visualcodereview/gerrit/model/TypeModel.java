package com.tasktop.codejam.visualcodereview.gerrit.model;

import static java.text.MessageFormat.format;

public class TypeModel {
	private TypeType type;
	private String packageName;
	private String name;

	public TypeModel(TypeType type, String packageName, String typeName) {
		this.type = type;
		this.name = typeName;
		this.packageName = packageName;
	}

	public TypeType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getFullyQualifiedName() {
		return format("{0}.{1}", packageName, name);
	}
}
