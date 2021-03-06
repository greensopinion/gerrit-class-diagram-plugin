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
