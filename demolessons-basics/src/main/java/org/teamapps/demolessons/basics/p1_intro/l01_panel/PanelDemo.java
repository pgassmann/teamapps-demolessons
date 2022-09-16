package org.teamapps.demolessons.basics.p1_intro.l01_panel;

import org.teamapps.common.format.Color;
import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.webcontroller.WebController;

public class PanelDemo implements DemoLesson {

    // Field rootComponent is the Content of this Demo
    private final Component rootComponent;

    // Constructor
    public PanelDemo() {

        // create new Panel and define it as the DemoLesson rootComponent
        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "My first Panel");
        rootComponent = panel;

        // set a panel property
        panel.setHeaderBackgroundColor(Color.GOLD);

        // add DummyComponent as panel Content
        panel.setContent(new DummyComponent());
    }

    // DemoLesson Interface method
    // returns the content of the Demo
    public Component getRootComponent(){
        return rootComponent;
    }

    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {

            // RootPanel is the global container of every element
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // get the rootComponent of the Demo and set it as the content of the global container (rootPanel)
            Component rootComponent = new PanelDemo().getRootComponent();
            rootPanel.setContent(rootComponent);

        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
