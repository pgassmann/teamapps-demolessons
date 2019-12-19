package org.teamapps.demo.lessons;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.l01_panel.PanelDemo;
import org.teamapps.demo.lessons.l02_textfield.TextFieldDemo;
import org.teamapps.demo.lessons.l03_verticallayout.VerticalLayoutDemo;
import org.teamapps.demo.lessons.l04_richtexteditor.RichTextEditorDemo;
import org.teamapps.demo.lessons.l05_label.LabelDemo;
import org.teamapps.demo.lessons.l06_button.ButtonDemo;
import org.teamapps.demo.lessons.l07_checkbox.CheckboxDemo;
import org.teamapps.demo.lessons.l08_combobox.ComboBoxDemo;
import org.teamapps.demo.lessons.l09_propertyextractor.PropertyExtractorDemo;
import org.teamapps.demo.lessons.l10_responsiveform.ResponsiveFormDemo;
import org.teamapps.demo.lessons.l11_table.TableDemo;
import org.teamapps.demo.lessons.l12_toolbar.ToolbarDemo;
import org.teamapps.demo.lessons.l13_tree.TreeDemo;
import org.teamapps.demo.lessons.l14_backgroundtasks.BackgroundTasksDemo;
import org.teamapps.demo.lessons.l15_externalevent.ExternalEventsDemo;
import org.teamapps.demo.lessons.l15_externalevent.NotificationManager;
import org.teamapps.demo.lessons.l16_servlet.ServletDemo;
import org.teamapps.demo.lessons.l16_servlet.ServletNotificationManager;
import org.teamapps.demo.lessons.l17_mustachetemplates.MustacheTemplateDemo;
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
        return responsiveApplication.getUi();
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
        BaseTemplateTreeNode introLessons = new BaseTemplateTreeNode(MaterialIcon.WEB_ASSET, "Introduction", "First steps with TeamApps");
        lessonsTree.addNode(introLessons);
        introLessons.setExpanded(true);

        BaseTemplateTreeNode<DemoLesson> l01_Panel = new BaseTemplateTreeNode(MaterialIcon.WEB_ASSET, null ,"Panel", "First lesson (PanelDemo)","1", new PanelDemo(sessionContext));
        l01_Panel.setParent(introLessons);
        lessonsTree.addNode(l01_Panel);

        BaseTemplateTreeNode<DemoLesson> l02_TextField = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ," TextField", "with event handling","2", new TextFieldDemo(sessionContext));
        l02_TextField.setParent(introLessons);
        lessonsTree.addNode(l02_TextField);

        BaseTemplateTreeNode<DemoLesson> l03_verticallayout = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Vertical Layout", "Multiple components","3", new VerticalLayoutDemo(sessionContext));
        l03_verticallayout.setParent(introLessons);
        lessonsTree.addNode(l03_verticallayout);

        BaseTemplateTreeNode<DemoLesson> l04_richtexteditor = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Rich Text Editor", "Formatted text","4", new RichTextEditorDemo(sessionContext));
        l04_richtexteditor.setParent(introLessons);
        lessonsTree.addNode(l04_richtexteditor);

        BaseTemplateTreeNode<DemoLesson> l05_label = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Labels", "TextField labels","5", new LabelDemo(sessionContext));
        l05_label.setParent(introLessons);
        lessonsTree.addNode(l05_label);

        BaseTemplateTreeNode<DemoLesson> l06_button = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Button", "Button","6", new ButtonDemo(sessionContext));
        l06_button.setParent(introLessons);
        lessonsTree.addNode(l06_button);

        BaseTemplateTreeNode<DemoLesson> l07_checkbox = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"CheckBox", "Switches","7", new CheckboxDemo(sessionContext));
        l07_checkbox.setParent(introLessons);
        lessonsTree.addNode(l07_checkbox);

        BaseTemplateTreeNode<DemoLesson> l08_combobox = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"ComboBox", "with BeanPropertyExtractor","8", new ComboBoxDemo(sessionContext));
        l08_combobox.setParent(introLessons);
        lessonsTree.addNode(l08_combobox);

        BaseTemplateTreeNode<DemoLesson> l09_propertyextractor = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"PropertyExtractor", "with TagComboBox and Property Mapping","9", new PropertyExtractorDemo(sessionContext));
        l09_propertyextractor.setParent(introLessons);
        lessonsTree.addNode(l09_propertyextractor);

        BaseTemplateTreeNode<DemoLesson> l10_responsiveform = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"ResponsiveForm", "Complete Form with Data saving","10", new ResponsiveFormDemo(sessionContext));
        l10_responsiveform.setParent(introLessons);
        lessonsTree.addNode(l10_responsiveform);

        BaseTemplateTreeNode<DemoLesson> l11_table = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Table", "Table with various Fields","11", new TableDemo(sessionContext));
        l11_table.setParent(introLessons);
        lessonsTree.addNode(l11_table);

        BaseTemplateTreeNode<DemoLesson> l12_toolbar = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Toolbar", "Toolbar and Custom Icons","12", new ToolbarDemo(sessionContext));
        l12_toolbar.setParent(introLessons);
        lessonsTree.addNode(l12_toolbar);

        BaseTemplateTreeNode<DemoLesson> l13_tree = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Tree", "SimpleTree","13", new TreeDemo(sessionContext));
        l13_tree.setParent(introLessons);
        lessonsTree.addNode(l13_tree);

        BaseTemplateTreeNode<DemoLesson> l14_tree = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Background Tasks", "Background calculations...","14", new BackgroundTasksDemo(sessionContext));
        l14_tree.setParent(introLessons);
        lessonsTree.addNode(l14_tree);

        BaseTemplateTreeNode<DemoLesson> l15_externalevent = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"External Event", "Events between sessions","15", new ExternalEventsDemo(sessionContext));
        l15_externalevent.setParent(introLessons);
        lessonsTree.addNode(l15_externalevent);

        BaseTemplateTreeNode<DemoLesson> l16_servlet = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"Servlet", "Create Events from an additional Endpoint","16", new ServletDemo(sessionContext));
        l16_servlet.setParent(introLessons);
        lessonsTree.addNode(l16_servlet);

        BaseTemplateTreeNode<DemoLesson> l17_mustachetemplate = new BaseTemplateTreeNode(MaterialIcon.INPUT, null ,"MustacheTemplate", "Versatile Custom Elements","17", new MustacheTemplateDemo(sessionContext));
        l17_mustachetemplate.setParent(introLessons);
        lessonsTree.addNode(l17_mustachetemplate);

        // Experimental Lessons
        BaseTemplateTreeNode experimentalLessons = new BaseTemplateTreeNode(MaterialIcon.FLASH_ON, "Experiments", "Just for Fun");
        lessonsTree.addNode(experimentalLessons);

        BaseTemplateTreeNode<DemoLesson> l99DemoLessonsApp = new BaseTemplateTreeNode(MaterialIcon.WEB, null ,"DemoLessonsApp", "The DemoLessonsApp itself",null, new DemoLessonsApp(sessionContext));
        l99DemoLessonsApp.setParent(experimentalLessons);
        lessonsTree.addNode(l99DemoLessonsApp);

        lessonsTree.onNodeSelected.addListener(node -> {
            if (node.getPayload() != null) {
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
