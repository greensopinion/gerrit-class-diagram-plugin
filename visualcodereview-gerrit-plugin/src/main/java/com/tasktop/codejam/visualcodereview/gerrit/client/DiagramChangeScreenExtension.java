package com.tasktop.codejam.visualcodereview.gerrit.client;

import com.google.gerrit.plugin.client.extension.Panel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DiagramChangeScreenExtension extends VerticalPanel {

	public static class Factory implements Panel.EntryPoint {

		@Override
		public void onLoad(Panel panel) {
			panel.add(new DiagramChangeScreenExtension(panel));
		}
	}

	public DiagramChangeScreenExtension(Panel panel) {
		// ChangeInfo change =
		// panel.getObject(GerritUiExtensionPoint.Key.CHANGE_INFO).cast();
		// RevisionInfo rev =
		// panel.getObject(GerritUiExtensionPoint.Key.REVISION_INFO).cast();
		add(new Label("Hello!"));
	}
}
