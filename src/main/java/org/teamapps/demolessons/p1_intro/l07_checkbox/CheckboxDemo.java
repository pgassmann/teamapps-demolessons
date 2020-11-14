package org.teamapps.demolessons.p1_intro.l07_checkbox;

import org.teamapps.common.format.Color;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.CheckBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

public class CheckboxDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    // additional instance variables
    private static Panel panel;
    private static CheckBox lightCheckBox1;
    private static CheckBox lightCheckBox2;
    private static boolean lightOn;

    // Constructor, only set session context instance variable
    public CheckboxDemo(SessionContext context) {
        this.context = context;
    }

    private static void handleLightSwitchPressed(Boolean checked) {
        lightOn = checked;
        updateUi();
    }

    private static void updateUi() {
        lightCheckBox1.setValue(lightOn);
        lightCheckBox2.setValue(lightOn);
        if(lightOn) {
            panel.setHeaderBackgroundColor(Color.GOLD);
        } else {
            panel.setHeaderBackgroundColor(Color.MATERIAL_BLUE_GREY_400);
        }
    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() {
        panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Checkbox Demo");
        rootComponent = panel;
        panel.setPadding(50);

        lightCheckBox1 = new CheckBox("Lights off");

        lightCheckBox1.onValueChanged.addListener(checked -> {
            if (checked) {
                context.showNotification(MaterialIcon.WB_SUNNY, "Good Morning");
                lightCheckBox1.setCaption("Lights on");
            } else {
                context.showNotification(MaterialIcon.STAR, "Good Night!");
                lightCheckBox1.setCaption("Lights off");
            }
        });

        // second checkbox and listener lambda in separate method
        lightCheckBox2 = new CheckBox("Light switch");
        lightCheckBox2.onValueChanged.addListener(CheckboxDemo::handleLightSwitchPressed);
        lightCheckBox1.onValueChanged.addListener(CheckboxDemo::handleLightSwitchPressed);

        updateUi();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(lightCheckBox1);
        verticalLayout.addComponent(lightCheckBox2);
        panel.setContent(verticalLayout);
    }


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            DemoLesson demo = new CheckboxDemo(sessionContext);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}