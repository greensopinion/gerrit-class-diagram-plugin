package com.tasktop.codejam.visualcodereview.gerrit;

import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.gerrit.extensions.webui.GwtPlugin;
import com.google.gerrit.extensions.webui.WebUiPlugin;
import com.google.inject.AbstractModule;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		DynamicSet.bind(binder(), WebUiPlugin.class).toInstance(new GwtPlugin("change_diagram_plugin"));
	}
}
