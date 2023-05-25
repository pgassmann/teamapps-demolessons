package org.teamapps.demolessons.basics.p1_intro.l15_customizedcomponent;

import org.apache.poi.ss.formula.functions.T;
import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.format.Spacing;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

public class TernaryCheckboxDemo implements DemoLesson {

    private final Component rootComponent;

    public TernaryCheckboxDemo() {
        VerticalLayout verticalLayout = new VerticalLayout();
        // use Custom three-state checkbox component
        TernaryCheckBox ternaryCheckBox = new TernaryCheckBox("yes/no/maybe");
        verticalLayout.addComponent(ternaryCheckBox);
        ternaryCheckBox.onValueChanged.addListener(ternaryValue -> {
            SessionContext.current().showNotification(MaterialIcon.MESSAGE, "Current selection: " + ternaryValue);
        });
        ternaryCheckBox.setValue(TernaryValue.INDETERMINATE);
        TextField textField = new TextField();
        textField.onValueChanged.addListener(ternaryCheckBox::setCaption);
        verticalLayout.addComponent(textField);
        verticalLayout.setMargin(Spacing.px(20));
        this.rootComponent = verticalLayout;
    }

    @Override
    public Component getRootComponent() {
        return rootComponent;
    }

    @Override
    public void handleDemoSelected() {
        DemoLesson.super.handleDemoSelected();
    }
    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {

            // RootPanel is the global container of every element
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            var demoInstance = new TernaryCheckboxDemo();

            // call the method defined in the DemoLesson Interface
            demoInstance.handleDemoSelected();

            // get the rootComponent of the Demo and set it as the content of the global container (rootPanel)
            rootPanel.setContent(demoInstance.getRootComponent());

        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
