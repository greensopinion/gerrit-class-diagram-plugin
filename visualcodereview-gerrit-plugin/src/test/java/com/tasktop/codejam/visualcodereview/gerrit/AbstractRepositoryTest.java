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
		git = Git.open(new File("../docker/volumes/git/com.tasktop.example.codereview.git"));
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
