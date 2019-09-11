package org.teamapps.demo.lessons.l04_richtexteditor;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.absolutelayout.Length;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.richtext.RichTextEditor;
import org.teamapps.ux.component.flexcontainer.HorizontalLayout;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class RichTextEditorDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    // Constructor, only set session context instance variable
    public RichTextEditorDemo(SessionContext context) {
        this.context = context;

        // create new Panel and define it as the DemoLesson rootComponent
        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Rich Text Editor Demo");
        rootComponent = panel;
        RichTextEditor textField1 = new RichTextEditor();

        RichTextEditor textField2 = new RichTextEditor();

        textField1.onValueChanged.addListener(text -> {
            textField2.setValue("New Content: " + text);
        });

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


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            RichTextEditorDemo textFieldDemo = new RichTextEditorDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}