package org.teamapps.demo.lessons.l01_panel;

import com.google.common.io.Files;
import org.teamapps.common.format.Color;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.webcontroller.SimpleWebController;

public class PanelExample {

    // First Example, without DemoLesson interface
    public static void main(String[] args) throws Exception {
        SimpleWebController controller = new SimpleWebController(context -> {

            // create new Panel and define it as the DemoLesson rootComponent
            Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "My first Panel");

            // set a panel property
            panel.setHeaderBackgroundColor(Color.GOLD);

            // add DummyComponent as panel Content
            panel.setContent(new DummyComponent());

            // SimpleWebController requires returning a Component which will be displayed.
            // return firstPanel as main Component
            return panel;
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}