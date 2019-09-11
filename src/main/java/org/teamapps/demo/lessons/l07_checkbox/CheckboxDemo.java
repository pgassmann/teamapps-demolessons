package org.teamapps.demo.lessons.l07_checkbox;

import com.google.common.io.Files;
import org.teamapps.common.format.Color;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.field.CheckBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

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
    public void handleDemoSelected() { }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            CheckboxDemo textFieldDemo = new CheckboxDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}