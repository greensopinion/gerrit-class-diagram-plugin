package com.tasktop.codejam.visualcodereview.gerrit.client;

import com.google.gerrit.client.GerritUiExtensionPoint;
import com.google.gerrit.client.info.ChangeInfo;
import com.google.gerrit.plugin.client.extension.Panel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
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
		ChangeInfo change = panel.getObject(GerritUiExtensionPoint.Key.CHANGE_INFO).cast();
		String revision = change.currentRevision();
		String projectName = change.project();

		FlowPanel group = new FlowPanel();

		group.add(new Label("Diagram:"));

		Image image = new Image(
				"/plugins/tasktop-visual-code-review/class-diagram/" + projectName + "/" + revision + ".svg");
		group.add(image);

		panel.add(group);
	}
}
