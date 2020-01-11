package org.teamapps.demolessons.p1_intro.l02_textfield;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
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
    private SessionContext context;

    // Constructor, only set session context instance variable
    public TextFieldDemo(SessionContext context) {
        this.context = context;
    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() {
        // show Notification in session context
        context.showNotification(MaterialIcon.LIGHTBULB_OUTLINE, "My first Notification");


        // create new Panel and define it as the DemoLesson rootComponent
        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Text Field Demo");
        rootComponent = panel;

        // configure Panel
        panel.setStretchContent(false);
        panel.setPadding(5);

        // define textField
        TextField textField = new TextField();
        textField.setEmptyText("write something and press ENTER or click somewhere else");
        // set Panel Content
        panel.setContent(textField);

        // Handle Events with lambda expression
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

            TextFieldDemo textFieldDemo = new TextFieldDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}