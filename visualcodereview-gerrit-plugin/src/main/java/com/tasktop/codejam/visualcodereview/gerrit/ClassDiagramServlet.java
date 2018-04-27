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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.git.GitRepositoryManager;
import com.google.gerrit.server.permissions.PermissionBackend;
import com.google.gerrit.server.permissions.PermissionBackendException;
import com.google.gerrit.server.permissions.ProjectPermission;
import com.google.gerrit.server.project.ProjectCache;
import com.tasktop.codejam.visualcodereview.gerrit.diagram.DotClient;
import com.tasktop.codejam.visualcodereview.gerrit.diagram.DotGenerator;

public class ClassDiagramServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(ClassDiagramServlet.class);
	private GitRepositoryManager repoManager;
	private ProjectCache projectCache;
	private PermissionBackend permissionBackend;

	private Provider<CurrentUser> currentUser;

	@Inject
	public ClassDiagramServlet(GitRepositoryManager repoManager, ProjectCache projectCache,
			PermissionBackend permissionBackend, Provider<CurrentUser> currentUser) {
		this.repoManager = repoManager;
		this.projectCache = projectCache;
		this.permissionBackend = permissionBackend;
		this.currentUser = currentUser;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<String> commitHash = filename(request).map(stripExtension());
		Optional<String> projectName = projectname(request);
		if (commitHash.isPresent() && projectName.isPresent()) {
			if (checkPermissions(response, projectName.get())) {
				generateDiagram(response, projectName.get(), commitHash.get());
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private boolean checkPermissions(HttpServletResponse response, String projectName) throws IOException {
		Project.NameKey nameKey = new Project.NameKey(projectName);
		try {
			if (projectCache.checkedGet(nameKey) == null) {
				logger.info("Project not found: {}", projectName);
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return false;
			}
			permissionBackend.user(currentUser.get()).project(nameKey).check(ProjectPermission.ACCESS);
			return true;
		} catch (AuthException e) {
			logger.info("Permisison denied to {} on {}", currentUser.get().getUserName(), projectName);
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		} catch (IOException | PermissionBackendException e) {
			logger.error("Cannot load " + projectName, e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}
	}

	private void generateDiagram(HttpServletResponse response, String projectName, String commitHash)
			throws IOException {
		Project.NameKey nameKey = new Project.NameKey(projectName);
		try (Repository repository = repoManager.openRepository(nameKey)) {
			String content = new DotClient().retrieveSvg(new DotGenerator().generate(repository, commitHash));
			response.setContentType("image/svg+xml;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			try (OutputStream output = response.getOutputStream()) {
				output.write(content.getBytes(StandardCharsets.UTF_8));
			}
		} catch (RepositoryNotFoundException e) {
			logger.info("Project not found: {}", projectName);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			logger.error("Cannot open " + projectName, e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private Function<String, String> stripExtension() {
		return s -> {
			int lastIndex = s.lastIndexOf('.');
			if (lastIndex != -1) {
				return s.substring(0, lastIndex);
			}
			return s;
		};
	}

	private Optional<String> projectname(HttpServletRequest request) {
		String uri = request.getRequestURI();
		List<String> segments = Splitter.on('/').splitToList(uri);
		if (segments.size() > 1) {
			return Optional.of(segments.get(segments.size() - 2));
		}
		return Optional.empty();
	}

	private Optional<String> filename(HttpServletRequest request) {
		String uri = request.getRequestURI();
		int lastIndex = uri.lastIndexOf('/');
		if (lastIndex != -1) {
			return Optional.of(uri.substring(lastIndex + 1));
		}
		return Optional.empty();
	}
}
