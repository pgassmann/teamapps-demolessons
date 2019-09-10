package org.teamapps.demo.lessons;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.l01_panel.PanelDemo;
import org.teamapps.demo.lessons.l02_textfield.TextFieldDemo;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.ExtendedLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateTreeNode;
import org.teamapps.ux.component.tree.SimpleTree;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class DemoLessonsApp implements DemoLesson {

    private final SessionContext sessionContext;

    private Component rootComponent;
    private View demoView;

    public DemoLessonsApp(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
//        this.rootComponent = createRootComponent();
    }

    @Override
    public void handleDemoSelected() {
        this.rootComponent = createRootComponent();
    }

    public Component getRootComponent() {
        return rootComponent;
    }

    private Component createRootComponent() {
        ResponsiveApplication responsiveApplication = ResponsiveApplication.createApplication();
        Perspective perspective = Perspective.createPerspective();
        responsiveApplication.addPerspective(perspective);

        View lessonsTreeView = View.createView(ExtendedLayout.LEFT, MaterialIcon.HELP, "Lessons", createLessonsTree());
        perspective.addView(lessonsTreeView);

        demoView = View.createView(ExtendedLayout.CENTER, MaterialIcon.HELP, "Demo Content", null);
        demoView.getPanel().setHideTitleBar(true);
        perspective.addView(demoView);

        responsiveApplication.showPerspective(perspective);
        return responsiveApplication.createUi();
    }

    @SuppressWarnings("unchecked")
    private SimpleTree<DemoLesson> createLessonsTree() {
        SimpleTree<DemoLesson> lessonsTree = new SimpleTree<>();
        lessonsTree.setTemplatesByDepth(BaseTemplate.LIST_ITEM_LARGE_ICON_TWO_LINES, BaseTemplate.LIST_ITEM_LARGE_ICON_TWO_LINES);
        lessonsTree.setOpenOnSelection(true);
        lessonsTree.setEnforceSingleExpandedPath(true);
        lessonsTree.setShowExpanders(true);
        lessonsTree.setIndentation(0);

        // Register all DemoLessons in lessonsTree

        // Intro Lessons
        BaseTemplateTreeNode introLessons = new BaseTemplateTreeNode(MaterialIcon.WEB_ASSET, "Introduction", "First Steps with TeamApps");
        lessonsTree.addNode(introLessons);

        BaseTemplateTreeNode<DemoLesson> l01Panel = new BaseTemplateTreeNode(MaterialIcon.WEB_ASSET, null ,"Lesson 1 - Panel", "First Lesson (PanelDemo)","1", new PanelDemo(sessionContext));
        l01Panel.setParent(introLessons);
        lessonsTree.addNode(l01Panel);

        BaseTemplateTreeNode<DemoLesson> l02TextField = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Lesson 2 - TextField", "TextField Lesson","2", new TextFieldDemo(sessionContext));
        l02TextField.setParent(introLessons);
        lessonsTree.addNode(l02TextField);

        // Experimental Lessons
        BaseTemplateTreeNode experimentalLessons = new BaseTemplateTreeNode(MaterialIcon.FLASH_ON, "Experiments", "Just for Fun");
        lessonsTree.addNode(experimentalLessons);

        BaseTemplateTreeNode<DemoLesson> l99DemoLessonsApp = new BaseTemplateTreeNode(MaterialIcon.WEB, null ,"DemoLessonsApp", "The DemoLessonsApp itself",null, new DemoLessonsApp(sessionContext));
        l99DemoLessonsApp.setParent(experimentalLessons);
        lessonsTree.addNode(l99DemoLessonsApp);

        lessonsTree.onNodeSelected.addListener(node -> {
            if (node.getPayload() instanceof DemoLesson) {
                DemoLesson lesson = node.getPayload();

                // call handleDemoSelected method on selected DemoLesson
                lesson.handleDemoSelected();
                // display rootComponent of selected DemoLesson
                demoView.setComponent(lesson.getRootComponent());
            }
        });
        return lessonsTree;
    }

    public static void main(String[] args) throws Exception {
        SimpleWebController controller = new SimpleWebController(context -> {
            DemoLessonsApp demoLessonsApp = new DemoLessonsApp(context);
            demoLessonsApp.handleDemoSelected();
            return demoLessonsApp.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir(), 8081).start();
    }
}
