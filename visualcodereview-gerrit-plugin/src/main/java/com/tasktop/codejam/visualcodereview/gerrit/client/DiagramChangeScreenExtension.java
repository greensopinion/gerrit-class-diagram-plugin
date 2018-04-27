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
package com.tasktop.codejam.visualcodereview.gerrit.client;

import com.google.gerrit.client.GerritUiExtensionPoint;
import com.google.gerrit.client.info.ChangeInfo;
import com.google.gerrit.plugin.client.extension.Panel;
import com.google.gwt.user.client.ui.Anchor;
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

		String imageUri = "/plugins/tasktop-visual-code-review/class-diagram/" + projectName + "/" + revision + ".svg";

		Anchor hyperlink = new Anchor();
		hyperlink.setTarget("_blank");
		hyperlink.setHref(imageUri);

		Image image = new Image(imageUri);
		image.getElement().setAttribute("style", "max-width: 1024px");
		hyperlink.getElement().appendChild(image.getElement());

		group.add(hyperlink);

		panel.add(group);
	}
}
