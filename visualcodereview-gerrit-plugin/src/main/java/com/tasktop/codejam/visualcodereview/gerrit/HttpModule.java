package com.tasktop.codejam.visualcodereview.gerrit;

import com.google.gerrit.httpd.plugins.HttpPluginModule;
import com.google.inject.Scopes;

public class HttpModule extends HttpPluginModule {
	@Override
	protected void configureServlets() {
		bind(ClassDiagramServlet.class).in(Scopes.SINGLETON);
		serve("/class-diagram/*").with(ClassDiagramServlet.class);
	}
}
