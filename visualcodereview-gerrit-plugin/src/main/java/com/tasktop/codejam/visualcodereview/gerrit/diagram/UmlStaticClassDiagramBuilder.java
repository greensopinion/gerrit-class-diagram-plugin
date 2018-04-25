package com.tasktop.codejam.visualcodereview.gerrit.diagram;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toSet;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

import com.tasktop.codejam.visualcodereview.gerrit.model.CardinalityType;
import com.tasktop.codejam.visualcodereview.gerrit.model.ProgramModel;
import com.tasktop.codejam.visualcodereview.gerrit.model.Relationship;
import com.tasktop.codejam.visualcodereview.gerrit.model.TypeModel;

public class UmlStaticClassDiagramBuilder {

	private PrintWriter writer;

	public UmlStaticClassDiagramBuilder(Writer writer) {
		this.writer = new PrintWriter(checkNotNull(writer));
	}

	public void beginDiagram() {
		writer.println("digraph G {");
		generateDefaultSettings();
	}

	public void model(ProgramModel programModel) {

		programModel.getTypes().forEach(this::node);

		Set<String> typeNames = programModel.getTypes().stream().map(TypeModel::getFullyQualifiedName).collect(toSet());
		programModel.getRelationships().stream().map(Relationship::getTargetFullyQualifiedName)
				.filter(t -> !typeNames.contains(t)).forEach(this::addType);

		programModel.getRelationships().forEach(this::relationship);
	}

	private void addType(String fullyQualifiedName) {
		String simpleName = fullyQualifiedName;
		if (simpleName.lastIndexOf('.') != -1) {
			simpleName = simpleName.substring(simpleName.lastIndexOf('.') + 1);
		}
		writer.println(format("\t\"{0}\"  [label=\"'{'{1}|'}'\" color=gray];", fullyQualifiedName, simpleName));
	}

	public void node(TypeModel type) {
		writer.println(format("\t\"{0}\"  [label=\"'{'{1}|'}'\"];", type.getFullyQualifiedName(), type.getName()));
	}

	public void relationship(Relationship relationship) {
		switch (relationship.getType()) {
		case COMPOSITION:
			if (relationship.getCardinality() == CardinalityType.NARY) {
				writer.println(format("\t\"{0}\" -> \"{1}\" [arrowtail=odiamond dir=both headlabel=\"0..n\"]",
						relationship.getSourceFullyQualifiedName(), relationship.getTargetFullyQualifiedName()));

			} else {
				writer.println(format("\t\"{0}\" -> \"{1}\" [arrowtail=odiamond dir=both]",
						relationship.getSourceFullyQualifiedName(), relationship.getTargetFullyQualifiedName()));
			}
			break;
		case EXTENSION:
			writer.println(format("\t\"{0}\" -> \"{1}\" [arrowhead=onormal dir=forward]",
					relationship.getSourceFullyQualifiedName(), relationship.getTargetFullyQualifiedName()));
			break;
		}
	}

	public void endDiagram() {
		writer.println("}");
		writer.flush();
	}

	private void generateDefaultSettings() {
		writer.println("\tedge [ arrowhead=none color=dimgray dir=both];");
		writer.println("\tnode [ shape=record];");
	}
}
