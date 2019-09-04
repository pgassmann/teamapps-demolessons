package org.teamapps.demo.lessons.l02_textfield;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class TextFieldDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    public Component getRootComponent(){
        return rootComponent;
    }

    public TextFieldDemo(SessionContext context) {
        // create new Panel and define it as the DemoLesson rootComponent
        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Text Field Demo");
        rootComponent = panel;

        // show Notification in session context
        context.showNotification(MaterialIcon.LIGHTBULB_OUTLINE, "first Notification");

        // configure Panel
        panel.setStretchContent(false);
        panel.setPadding(5);

        // define textField
        TextField textField = new TextField();
        textField.setEmptyText("write something and press ENTER or click somewhere else");
        // set Panel Content
        panel.setContent(textField);

        // act on events
        textField.onValueChanged.addListener(newValue -> {

            // showNotification with all Options, use new value of Text Field
            context.showNotification(
                    MaterialIcon.NOTIFICATIONS,
                    "Welcome",
                    "Hello " + newValue,
                    true,
                    10000,
                    true
            );
        });

    }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {
            return new TextFieldDemo(context).getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}