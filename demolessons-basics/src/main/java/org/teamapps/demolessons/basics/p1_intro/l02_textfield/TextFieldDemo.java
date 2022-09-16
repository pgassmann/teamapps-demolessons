package org.teamapps.demolessons.basics.p1_intro.l02_textfield;

import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

public class TextFieldDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private final SessionContext context;

    // Constructor, only set session context instance variable
    public TextFieldDemo() {
        this.context = SessionContext.current();
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


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {

            // RootPanel is the global container of every element
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            TextFieldDemo textFieldDemo = new TextFieldDemo();

            // call the method defined in the DemoLesson Interface
            textFieldDemo.handleDemoSelected();

            // get the rootComponent of the Demo and set it as the content of the global container (rootPanel)
            rootPanel.setContent(textFieldDemo.getRootComponent());

        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
