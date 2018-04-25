package com.tasktop.codejam.visualcodereview.gerrit.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommitModelServiceTest {

	private Git git;
	private Repository repository;
	private CommitModelService service;

	@Before
	public void before() throws IOException {
		git = Git.open(new File("../docker/volumes/git/com.tasktop.example.codereview.git"));
		repository = git.getRepository();
		service = new CommitModelService(repository);
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

	@Test
	public void resolve() throws IOException {
		CommitModel commitModel = service.create("ced86368a80bf33aecb467a3a3034a5e796ddd21");
		assertThat(commitModel.getFiles()).extracting(FileModel::getPath).containsExactlyInAnyOrder(".classpath",
				".gitignore", ".project", ".settings/org.eclipse.core.resources.prefs",
				".settings/org.eclipse.jdt.core.prefs", ".settings/org.eclipse.m2e.core.prefs", "pom.xml",
				"src/main/java/com/tasktop/example/codereview/Example.java");
	}
}
