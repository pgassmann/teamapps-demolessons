package org.teamapps.demolessons.basics.p1_intro.l05_label;

import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.Label;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

public class LabelDemo implements DemoLesson {

    private Component rootComponent;
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

        textField1.onTextInput.addListener(text -> textField2.setValue("Your name is: " + text));

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


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            DemoLesson demo = new LabelDemo(sessionContext);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}