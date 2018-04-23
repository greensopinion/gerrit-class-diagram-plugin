package com.tasktop.codejam.visualcodereview.gerrit.client;

import com.google.gerrit.client.GerritUiExtensionPoint;
import com.google.gerrit.client.Resources;
import com.google.gerrit.plugin.client.Plugin;
import com.google.gerrit.plugin.client.PluginEntryPoint;
import com.google.gwt.core.client.GWT;

public class DiagramPlugin extends PluginEntryPoint {
	public static final Resources RESOURCES = GWT.create(Resources.class);

	@Override
	public void onPluginLoad() {
		Plugin.get().panel(GerritUiExtensionPoint.CHANGE_SCREEN_BELOW_COMMIT_INFO_BLOCK,
				new DiagramChangeScreenExtension.Factory(), "Change Diagram");
	}
}
