package org.teamapps.demolessons.p1_intro.l03_verticallayout;

import org.teamapps.common.format.Color;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.field.FieldEditingMode;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

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

        VerticalLayout verticalLayout = new VerticalLayout();
        panel.setContent(verticalLayout);

        TextField textField1 = new TextField();
        verticalLayout.addComponent(textField1);
        textField1.setEmptyText("Enter some text here...");

        TextField textField2 = new TextField();
        textField2.setValue("Multiple Components can be added to VerticalLayout. Click the Button to dynamically add more");
        verticalLayout.addComponent(textField2);

        // onTextInput immediately fires when something is written. onValueChanged only fires when the user moves focus from the Field.
        textField1.onTextInput.addListener(text -> textField2.setValue("New Content: " + text));

        //noinspection rawtypes
        Button<BaseTemplateRecord> button = Button.create(AntuIcon.ACTION_JOURNAL_NEW_24, "Add new TextField with value of first TextField").setColor(Color.BEIGE);
        button.onClicked.addListener(() -> {
            // Create new TextField when Button is clicked and add it to verticalLayout
            TextField newTextField = new TextField();
            newTextField.setValue(textField1.getValue());
            newTextField.setEditingMode(FieldEditingMode.DISABLED);
            verticalLayout.addComponent(newTextField);
        });
        verticalLayout.addComponent(button);

    }


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {

            // RootPanel is the global container of every element
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            VerticalLayoutDemo demoInstance = new VerticalLayoutDemo(sessionContext);

            // call the method defined in the DemoLesson Interface
            demoInstance.handleDemoSelected();

            // get the rootComponent of the Demo and set it as the content of the global container (rootPanel)
            rootPanel.setContent(demoInstance.getRootComponent());

        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}