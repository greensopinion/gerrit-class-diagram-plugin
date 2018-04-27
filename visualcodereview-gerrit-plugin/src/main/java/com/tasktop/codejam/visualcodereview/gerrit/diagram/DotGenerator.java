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
		builder.model(programModel);
		builder.endDiagram();
		return writer.toString();
	}
}
