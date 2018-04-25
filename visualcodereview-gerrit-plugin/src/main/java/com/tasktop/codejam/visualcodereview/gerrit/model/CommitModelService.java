package com.tasktop.codejam.visualcodereview.gerrit.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class CommitModelService {
	private Repository repository;

	public CommitModelService(Repository repository) {
		this.repository = repository;
	}

	public CommitModel create(String commitId) throws IOException {
		List<FileModel> files = new ArrayList<>();
		ObjectId id = repository.resolve(commitId);
		try (RevWalk walk = new RevWalk(repository)) {
			RevCommit commit = walk.parseCommit(id);
			RevCommit parentCommit = commit.getParent(0);
			parentCommit = walk.parseCommit(parentCommit.getId());

			AbstractTreeIterator commitIterator = createTreeIterator(commit.getTree().getId());
			AbstractTreeIterator parentCommitIterator = createTreeIterator(parentCommit.getTree().getId());

			try (DiffFormatter diffFormatter = new DiffFormatter(new ByteArrayOutputStream())) {
				diffFormatter.setRepository(repository);
				List<DiffEntry> entries = diffFormatter.scan(parentCommitIterator, commitIterator);
				for (DiffEntry entry : entries) {
					createFileModel(entry).ifPresent(files::add);
				}
			}
		}
		return new CommitModel(files);
	}

	private Optional<FileModel> createFileModel(DiffEntry entry) {
		if (DiffEntry.ChangeType.DELETE.equals(entry.getChangeType())) {
			return Optional.empty();
		}
		return Optional.of(new FileModel(entry.getNewPath()));
	}

	private AbstractTreeIterator createTreeIterator(ObjectId treeId) throws IncorrectObjectTypeException, IOException {
		try (ObjectReader reader = repository.newObjectReader()) {
			return new CanonicalTreeParser(null, reader, treeId);
		}
	}
}
