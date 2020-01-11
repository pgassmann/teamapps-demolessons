package org.teamapps.demolessons.p1_intro.l03_verticallayout;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class VerticalLayoutDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    // Constructor, only set session context instance variable
    public VerticalLayoutDemo(SessionContext context) {
        this.context = context;
    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() {

        // create new Panel and define it as the DemoLesson rootComponent
        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Vertical Layout Demo");
        rootComponent = panel;

        panel.setStretchContent(false);
        panel.setPadding(10);


        TextField textField1 = new TextField();
        textField1.setEmptyText("Enter some text here...");

        TextField textField2 = new TextField();

        textField1.onTextInput.addListener(text -> {
            textField2.setValue("New Content: " + text);
        });


        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(textField1);
        verticalLayout.addComponent(textField2);
        panel.setContent(verticalLayout);
    }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            VerticalLayoutDemo textFieldDemo = new VerticalLayoutDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}