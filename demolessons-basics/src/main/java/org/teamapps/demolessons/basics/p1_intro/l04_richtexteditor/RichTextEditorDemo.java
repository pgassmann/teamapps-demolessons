package org.teamapps.demolessons.basics.p1_intro.l04_richtexteditor;

import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.richtext.RichTextEditor;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.webcontroller.WebController;

public class RichTextEditorDemo implements DemoLesson {

    private final Component rootComponent;

    public RichTextEditorDemo() {

        // create new Panel and define it as the DemoLesson rootComponent
        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Rich Text Editor Demo");
        rootComponent = panel;
        RichTextEditor textField1 = new RichTextEditor();

        RichTextEditor textField2 = new RichTextEditor();

        textField1.onValueChanged.addListener(text -> textField2.setValue("New Content: " + text));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(textField1);
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
            DemoLesson demo = new RichTextEditorDemo();

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
