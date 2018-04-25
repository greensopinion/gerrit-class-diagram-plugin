package com.tasktop.codejam.visualcodereview.gerrit.diagram;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.MessageFormat.format;

import java.io.PrintWriter;
import java.io.Writer;

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

	public void node(TypeModel type) {
		writer.println(format("\t\"{0}\"  [label=\"'{'{1}|'}'\"];", type.getFullyQualifiedName(), type.getName()));
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
