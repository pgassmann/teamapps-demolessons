package org.teamapps.demolessons.p1_intro.l06_button;

import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

public class ButtonDemo implements DemoLesson {

    private Component rootComponent;

    public ButtonDemo(SessionContext sessionContext) {

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Button Demo");
        rootComponent = panel;

        Button<BaseTemplateRecord> button = Button.create("Do it! & Expand dropdownComponent of Button", new DummyComponent("foo"));
        button.onClicked.addListener(() -> sessionContext.showNotification(MaterialIcon.FLAG, "Button clicked!"));
        panel.setContent(button);

    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() { }


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            DemoLesson demo = new ButtonDemo(sessionContext);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}