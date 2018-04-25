package com.tasktop.codejam.visualcodereview.gerrit.diagram;

import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.jgit.lib.Repository;

import com.tasktop.codejam.visualcodereview.gerrit.model.CommitModel;
import com.tasktop.codejam.visualcodereview.gerrit.model.CommitModelService;
import com.tasktop.codejam.visualcodereview.gerrit.model.ProgramModel;
import com.tasktop.codejam.visualcodereview.gerrit.model.ProgramModelService;

public class DotGenerator {

	public String generate(Repository repository, String commitHash) throws IOException {
		CommitModel commitModel = new CommitModelService(repository).create(commitHash);
		ProgramModel programModel = new ProgramModelService(repository).create(commitModel);

		StringWriter writer = new StringWriter();
		UmlStaticClassDiagramBuilder builder = new UmlStaticClassDiagramBuilder(writer);
		builder.beginDiagram();
		programModel.getTypes().forEach(builder::node);
		programModel.getRelationships().forEach(builder::relationship);
		builder.endDiagram();
		return writer.toString();
	}

}
