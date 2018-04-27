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
package com.tasktop.codejam.visualcodereview.gerrit;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;

public class AbstractRepositoryTest {

	private Git git;
	protected Repository repository;

	@Before
	public void before() throws IOException {
		git = Git.open(new File("target/com.tasktop.example.codereview.git"));
		repository = git.getRepository();
	}

	@After
	public void after() {
		if (repository != null) {
			repository.close();
			repository = null;
		}
		if (git != null) {
			git.close();
			git = null;
		}
	}
}
