package org.teamapps.demo.lessons.l12_toolbar;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.icon.material.MaterialIconStyles;
import org.teamapps.icons.api.CompositeIcon;
import org.teamapps.icons.api.Icon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class ToolbarDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    // Constructor, only set session context instance variable
    public ToolbarDemo(SessionContext context) {
        this.context = context;

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Toolbar Demo");
        rootComponent = panel;
        panel.setPadding(50);

        /* Create Toolbar and set on Panel */
        Toolbar toolbar = new Toolbar();
        panel.setToolbar(toolbar);
        /* ToolbarButtons require a ToolbarButtonGroup first */
        ToolbarButtonGroup buttonGroup1 = new ToolbarButtonGroup();
        toolbar.addButtonGroup(buttonGroup1);

        /* Add ToolbarButtons to buttonGroup */
        buttonGroup1.addButton(ToolbarButton.create(MaterialIcon.SAVE, "Save", "Save changes"));
        buttonGroup1.addButton(ToolbarButton.create(MaterialIcon.CLOUD_UPLOAD, "Load", "Open existing"));

        /* Create custom icon by combining two Icons using CompositeIcon.of() and withStyle() */
        Icon deleteFolderIcon = CompositeIcon.of(MaterialIcon.FOLDER, MaterialIcon.DELETE.withStyle(MaterialIconStyles.PLAIN_RED_500));
        buttonGroup1.addButton(ToolbarButton.create(deleteFolderIcon, "Delete Folder", "Delete current Folder"));
        buttonGroup1.addButton(ToolbarButton.create(MaterialIcon.SEND, "Senden", "send Mail"));

        // Second Group with a Toggle implementation using two Buttons, one visible, the other not
        ToolbarButtonGroup buttonGroup2 = new ToolbarButtonGroup();
        toolbar.addButtonGroup(buttonGroup2);
        ToolbarButton robotButton = ToolbarButton.create(MaterialIcon.ANDROID, "Auto Mode", "Robot is waiting");
        buttonGroup2.addButton(robotButton);
        ToolbarButton robotButton2 = ToolbarButton.create(CompositeIcon.of(MaterialIcon.ANDROID, MaterialIcon.STOP.withStyle(MaterialIconStyles.PLAIN_RED_500)), "Auto Mode Off", "Robot is working").setVisible(false);
        buttonGroup2.addButton(robotButton2);

        /* ToolbarButtons have onClick events */
        robotButton.onClick.addListener(toolbarButtonClickEvent -> {
            toggleRobot(context, robotButton, robotButton2);
        });

        robotButton2.onClick.addListener(toolbarButtonClickEvent -> {
            toggleRobot(context, robotButton, robotButton2);
        });

        // Toolbar belongs to the Panel, it is not part of the panel content.
        // Content can be anything.
        VerticalLayout firstVerticalLayout = new VerticalLayout();
        panel.setContent(firstVerticalLayout);
        TextField textField = new TextField();
        textField.setEmptyText("leerText");
        TextField textField2 = new TextField().setEmptyText("zwei");
        firstVerticalLayout.addComponent(textField);
        firstVerticalLayout.addComponent(textField2);

        /* onTextInput event fires immediately when typing */
        textField.onTextInput.addListener(newValue -> {
            textField2.setValue("Text1: " + newValue);
            /* Manually fire onValueChanged event on textField2 */
            textField2.onValueChanged.fire(textField2.getValue());
        });
        textField2.onValueChanged.addListener(s -> context.showNotification(MaterialIcon.NOTIFICATIONS, s));

    }

    private static void toggleRobot(SessionContext context, ToolbarButton robotButton, ToolbarButton robotButton2) {
        Boolean robotStatus = robotButton.isVisible();
        if (robotStatus) {
            context.showNotification(MaterialIcon.SCHOOL, "Learning!");

        }
        robotButton.setVisible(!robotButton.isVisible());
        robotButton2.setVisible(!robotButton2.isVisible());
    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() { }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            ToolbarDemo textFieldDemo = new ToolbarDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}