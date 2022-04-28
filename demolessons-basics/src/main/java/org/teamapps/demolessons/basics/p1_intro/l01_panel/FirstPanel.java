package org.teamapps.demolessons.basics.p1_intro.l01_panel;

import org.teamapps.common.format.Color;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.webcontroller.WebController;

public class FirstPanel {

    // First Example, without DemoLesson interface

    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {

            // RootPanel is the global container of every element
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new Panel with Icon and Title
            Panel firstPanel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "My first Panel");

            // set a panel property
            firstPanel.setHeaderBackgroundColor(Color.GOLD);

            // add DummyComponent as panel Content
            firstPanel.setContent(new DummyComponent());

            // add the new panel to the global container (rootPanel)
            rootPanel.setContent(firstPanel);

        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
