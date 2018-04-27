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
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StringProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.MemoryTypeSolver;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class ProgramModelService {
	private Repository repository;

	private ParserConfiguration parserConfiguration;

	private MemoryTypeSolver typeSolver = new MemoryTypeSolver();

	public ProgramModelService(Repository repository) {
		this.repository = repository;
		this.parserConfiguration = createParserConfiguration();
	}

	public ProgramModel create(CommitModel commitModel) {
		List<TypeModel> types = new ArrayList<>();
		List<Relationship> relationships = new ArrayList<>();
		commitModel.getFiles().stream().filter(includedFiles()).forEach(file -> parse(file, types, relationships));
		return new ProgramModel(types, relationships.stream().filter(excludedRelationshipTypes()).collect(toList()));
	}

	private Predicate<FileModel> includedFiles() {
		return f -> f.getPath().endsWith(".java") && !isTest(f);
	}

	private boolean isTest(FileModel f) {
		return f.getPath().contains("src/test/") || f.getPath().contains(".tests/src/");
	}

	private Predicate<Relationship> excludedRelationshipTypes() {
		return r -> !r.getTargetFullyQualifiedName().startsWith("java.");
	}

	private void parse(FileModel file, List<TypeModel> types, List<Relationship> relationships) {
		try {
			ObjectLoader objectLoader = repository.getObjectDatabase().open(file.getId().toObjectId());
			String text = new String(objectLoader.getCachedBytes(), StandardCharsets.UTF_8);
			CompilationUnit cu = new JavaParser(parserConfiguration)
					.parse(ParseStart.COMPILATION_UNIT, new StringProvider(text)).getResult()
					.orElseThrow(() -> new IllegalStateException());
			for (TypeDeclaration<?> type : cu.getTypes()) {
				types.add(createType(cu, type));
			}
			for (TypeDeclaration<?> type : cu.getTypes()) {
				relationships.addAll(createRelationships(cu, type));
			}
		} catch (ParseProblemException e) {
			LoggerFactory.getLogger(ProgramModelService.class).error("Cannot parse source", e);
			types.add(placeholderModel(file));
		} catch (IOException e) {
			LoggerFactory.getLogger(ProgramModelService.class).error("Cannot load object", e);
			types.add(placeholderModel(file));
		}
	}

	private List<Relationship> createRelationships(CompilationUnit cu, TypeDeclaration<?> type) {
		Builder<Relationship> builder = ImmutableList.builder();

		if (type instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration declaration = (ClassOrInterfaceDeclaration) type;
			addAll(builder, cu, type, declaration.getExtendedTypes());
			addAll(builder, cu, type, declaration.getImplementedTypes());
		}
		for (FieldDeclaration declaration : type.getFields()) {
			Type commonType = declaration.getCommonType();
			if (commonType instanceof ClassOrInterfaceType) {
				CardinalityType cardinality = CardinalityType.UNARY;
				ClassOrInterfaceType fieldType = (ClassOrInterfaceType) commonType;
				if (fieldType.getTypeArguments().isPresent()) {
					Type genericType = fieldType.getTypeArguments().get().get(0);
					if (genericType instanceof ClassOrInterfaceType) {
						cardinality = CardinalityType.NARY;
						fieldType = (ClassOrInterfaceType) genericType;
					} else {
						continue;
					}
				}
				String qualifiedName = qualifiedName(cu, fieldType);
				builder.add(new Relationship(RelationshipType.COMPOSITION, cardinality, qualifiedName(cu, type),
						qualifiedName));
			}
		}
		return builder.build();
	}

	private void addAll(Builder<Relationship> builder, CompilationUnit cu, TypeDeclaration<?> type,
			NodeList<ClassOrInterfaceType> extendedTypes) {
		for (ClassOrInterfaceType extendedType : extendedTypes) {
			String qualifiedName = qualifiedName(cu, extendedType);
			builder.add(new Relationship(RelationshipType.EXTENSION, CardinalityType.UNARY, qualifiedName(cu, type),
					qualifiedName));
		}
	}

	private String qualifiedName(CompilationUnit cu, ClassOrInterfaceType type) {
		String typeName = type.getNameAsString();
		if (typeName.contains(".")) {
			return typeName;
		}
		return format("{0}.{1}", resolvePackageName(cu, type.getName()), type.getName());
	}

	private String resolvePackageName(CompilationUnit cu, SimpleName name) {
		for (ImportDeclaration i : cu.getImports()) {
			if (i.getNameAsString().endsWith(name.toString())) {
				return packageNameOfFullyQualifiedName(i.getNameAsString());
			}
		}
		try {
			Class.forName("java.lang." + name.toString(), false, String.class.getClassLoader());
			return "java.lang";
		} catch (ClassNotFoundException e) {
			return packageName(cu);
		}
	}

	private String packageNameOfFullyQualifiedName(String fullyQualifiedName) {
		List<String> components = Splitter.on(".").splitToList(fullyQualifiedName);
		return components.stream().filter(s -> !Character.isUpperCase(s.charAt(0))).collect(Collectors.joining("."));
	}

	private TypeModel createType(CompilationUnit cu, TypeDeclaration<?> type) {
		String packageName = packageName(cu);
		String typeName = type.getNameAsString();
		if (type instanceof EnumDeclaration) {
			return new TypeModel(TypeType.ENUM, packageName, typeName);
		} else if (type instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration declaration = (ClassOrInterfaceDeclaration) type;
			TypeType typeType = declaration.isInterface() ? TypeType.INTERFACE : TypeType.CLASS;
			return new TypeModel(typeType, packageName, typeName);
		} else {
			return new TypeModel(TypeType.CLASS, packageName, typeName);
		}
	}

	private String qualifiedName(CompilationUnit cu, TypeDeclaration<?> type) {
		return format("{0}.{1}", packageName(cu), type.getName());
	}

	private String packageName(CompilationUnit cu) {
		return cu.getPackageDeclaration().map(d -> d.getNameAsString()).orElse("default");
	}

	private TypeModel placeholderModel(FileModel file) {
		return new TypeModel(TypeType.CLASS, "unknown", nameOf(file.getPath()));
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

	private ParserConfiguration createParserConfiguration() {
		ParserConfiguration configuration = new ParserConfiguration();
		configuration.setLanguageLevel(LanguageLevel.CURRENT);
		configuration.setSymbolResolver(new JavaSymbolSolver(typeSolver));
		return configuration;
	}
}
