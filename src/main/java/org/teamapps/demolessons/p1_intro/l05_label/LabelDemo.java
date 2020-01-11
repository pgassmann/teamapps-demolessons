package org.teamapps.demolessons.p1_intro.l05_label;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.Label;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class LabelDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    public LabelDemo(SessionContext context) {
        this.context = context;

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Label Demo");
        rootComponent = panel;

        TextField textField1 = new TextField();
        textField1.setEmptyText("John Doe");

        Label label1 = new Label("Name", MaterialIcon.PERSON);
        label1.setTargetComponent(textField1);

        TextField textField2 = new TextField();

        textField1.onTextInput.addListener(text -> {
            textField2.setValue("Your name is: " + text);
        });

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(label1);
        verticalLayout.addComponent(textField1);
        verticalLayout.addComponent(new Label("new Label"));
        verticalLayout.addComponent(textField2);
        panel.setContent(verticalLayout);

    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() { }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            LabelDemo textFieldDemo = new LabelDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}