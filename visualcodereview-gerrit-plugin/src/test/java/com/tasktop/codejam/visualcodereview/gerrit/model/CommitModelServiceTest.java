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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.tasktop.codejam.visualcodereview.gerrit.AbstractRepositoryTest;

public class CommitModelServiceTest extends AbstractRepositoryTest {

	private CommitModelService service;

	@Before
	public void before() throws IOException {
		super.before();
		service = new CommitModelService(repository);
	}

	@Test
	public void resolve() throws IOException {
		CommitModel commitModel = service.create("ced86368a80bf33aecb467a3a3034a5e796ddd21");
		assertThat(commitModel.getFiles()).extracting(FileModel::getPath).containsExactlyInAnyOrder(".classpath",
				".gitignore", ".project", ".settings/org.eclipse.core.resources.prefs",
				".settings/org.eclipse.jdt.core.prefs", ".settings/org.eclipse.m2e.core.prefs", "pom.xml",
				"src/main/java/com/tasktop/example/codereview/Example.java");
	}
}
