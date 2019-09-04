package org.teamapps.demo.lessons.l01_panel;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class PanelDemo implements DemoLesson {

    // DemoLesson interface implementation
    private Component rootComponent = new DummyComponent();
    public Component getRootComponent(){
        return rootComponent;
    }

    // Constructor
    public PanelDemo(SessionContext context) {

        // create new Panel and define it as the DemoLesson rootComponent
        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "firstPanel");
        rootComponent = panel;
    }

    public static void main(String[] args) throws Exception {
        SimpleWebController controller = new SimpleWebController(context -> {

            // SimpleWebController requires a Component
            return new PanelDemo(context).getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}