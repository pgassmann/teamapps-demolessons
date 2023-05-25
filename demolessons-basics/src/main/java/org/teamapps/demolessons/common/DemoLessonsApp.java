package org.teamapps.demolessons.common;

import org.teamapps.demolessons.basics.p1_intro.l01_panel.PanelDemo;
import org.teamapps.demolessons.basics.p1_intro.l02_textfield.TextFieldDemo;
import org.teamapps.demolessons.basics.p1_intro.l03_verticallayout.VerticalLayoutDemo;
import org.teamapps.demolessons.basics.p1_intro.l04_richtexteditor.RichTextEditorDemo;
import org.teamapps.demolessons.basics.p1_intro.l05_label.LabelDemo;
import org.teamapps.demolessons.basics.p1_intro.l06_button.ButtonDemo;
import org.teamapps.demolessons.basics.p1_intro.l07_checkbox.CheckboxDemo;
import org.teamapps.demolessons.basics.p1_intro.l08_combobox.ComboBoxDemo;
import org.teamapps.demolessons.basics.p1_intro.l09_propertyextractor.PropertyExtractorDemo;
import org.teamapps.demolessons.basics.p1_intro.l10_responsiveform.ResponsiveFormDemo;
import org.teamapps.demolessons.basics.p1_intro.l11_table.TableDemo;
import org.teamapps.demolessons.basics.p1_intro.l12_toolbar.ToolbarDemo;
import org.teamapps.demolessons.basics.p1_intro.l13_tree.TreeDemo;
import org.teamapps.demolessons.basics.p1_intro.l14_responsiveapplication.ResponsiveApplicationDemo;
import org.teamapps.demolessons.basics.p1_intro.l15_customizedcomponent.TernaryCheckboxDemo;
import org.teamapps.demolessons.basics.p2_application.l01_backgroundtasks.BackgroundTasksDemo;
import org.teamapps.demolessons.basics.p2_application.l02_externalevent.ExternalEventsDemo;
import org.teamapps.demolessons.basics.p2_application.l03_servlet.ServletDemo;
import org.teamapps.demolessons.basics.p2_application.l04_mustachetemplates.MustacheTemplateDemo;
import org.teamapps.demolessons.basics.p2_application.l05_iconviewer.IconBrowser;
import org.teamapps.demolessons.basics.p2_application.l05_iconviewer.IconViewerDemo;
import org.teamapps.demolessons.basics.p4_issuetracker.IssueTrackerApp;
import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.emoji.EmojiIcon;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.ExtendedLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateTreeNode;
import org.teamapps.ux.component.tree.SimpleTree;
import org.teamapps.webcontroller.WebController;

public class DemoLessonsApp implements DemoLesson {

    private Component rootComponent;
    private View demoView;
    private final ResponsiveApplication responsiveApplication;

    public DemoLessonsApp(ResponsiveApplication responsiveApplication) {
        this.responsiveApplication = responsiveApplication;
//        this.rootComponent = createRootComponent();
        // handleDemoSelected();
    }
    public DemoLessonsApp() {
        this(ResponsiveApplication.createApplication());
    }

    @Override
    public void handleDemoSelected() {
        this.rootComponent = createRootComponent();
    }

    public Component getRootComponent() {
        return rootComponent;
    }

    private Component createRootComponent() {
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

    private SimpleTree<DemoLesson> createLessonsTree() {
        SimpleTree<DemoLesson> lessonsTree = new SimpleTree<>();
        lessonsTree.setTemplatesByDepth(BaseTemplate.LIST_ITEM_LARGE_ICON_TWO_LINES, BaseTemplate.LIST_ITEM_LARGE_ICON_TWO_LINES);
        lessonsTree.setOpenOnSelection(true);
        lessonsTree.setEnforceSingleExpandedPath(true);
        lessonsTree.setShowExpanders(true);
        lessonsTree.setIndentation(0);

        // Register all DemoLessons in lessonsTree

        // Intro Lessons
        BaseTemplateTreeNode<DemoLesson> introLessons = new BaseTemplateTreeNode<>(MaterialIcon.WEB_ASSET, "Introduction", "First steps with TeamApps");
        lessonsTree.addNode(introLessons);
        introLessons.setExpanded(true);

        BaseTemplateTreeNode<DemoLesson> l01_Panel = new BaseTemplateTreeNode<>(MaterialIcon.WEB_ASSET, null ,"Panel", "First lesson (PanelDemo)","1", new PanelDemo());
        l01_Panel.setParent(introLessons);
        lessonsTree.addNode(l01_Panel);

        BaseTemplateTreeNode<DemoLesson> l02_TextField = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ," TextField", "with event handling","2", new TextFieldDemo());
        l02_TextField.setParent(introLessons);
        lessonsTree.addNode(l02_TextField);

        BaseTemplateTreeNode<DemoLesson> l03_verticallayout = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Vertical Layout", "Multiple components","3", new VerticalLayoutDemo());
        l03_verticallayout.setParent(introLessons);
        lessonsTree.addNode(l03_verticallayout);

        BaseTemplateTreeNode<DemoLesson> l04_richtexteditor = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Rich Text Editor", "Formatted text","4", new RichTextEditorDemo());
        l04_richtexteditor.setParent(introLessons);
        lessonsTree.addNode(l04_richtexteditor);

        BaseTemplateTreeNode<DemoLesson> l05_label = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Labels", "TextField labels","5", new LabelDemo());
        l05_label.setParent(introLessons);
        lessonsTree.addNode(l05_label);

        BaseTemplateTreeNode<DemoLesson> l06_button = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Button", "Button","6", new ButtonDemo());
        l06_button.setParent(introLessons);
        lessonsTree.addNode(l06_button);

        BaseTemplateTreeNode<DemoLesson> l07_checkbox = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"CheckBox", "Switches","7", new CheckboxDemo());
        l07_checkbox.setParent(introLessons);
        lessonsTree.addNode(l07_checkbox);

        BaseTemplateTreeNode<DemoLesson> l08_combobox = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"ComboBox", "with BeanPropertyExtractor","8", new ComboBoxDemo());
        l08_combobox.setParent(introLessons);
        lessonsTree.addNode(l08_combobox);

        BaseTemplateTreeNode<DemoLesson> l09_propertyextractor = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"PropertyExtractor", "with TagComboBox and Property Mapping","9", new PropertyExtractorDemo());
        l09_propertyextractor.setParent(introLessons);
        lessonsTree.addNode(l09_propertyextractor);

        BaseTemplateTreeNode<DemoLesson> l10_responsiveform = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"ResponsiveForm", "Complete Form with Data saving","10", new ResponsiveFormDemo());
        l10_responsiveform.setParent(introLessons);
        lessonsTree.addNode(l10_responsiveform);

        BaseTemplateTreeNode<DemoLesson> l11_table = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Table", "Table with various Fields","11", new TableDemo());
        l11_table.setParent(introLessons);
        lessonsTree.addNode(l11_table);

        BaseTemplateTreeNode<DemoLesson> l12_toolbar = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Toolbar", "Toolbar and Custom Icons","12", new ToolbarDemo());
        l12_toolbar.setParent(introLessons);
        lessonsTree.addNode(l12_toolbar);

        BaseTemplateTreeNode<DemoLesson> l13_tree = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Tree", "SimpleTree","13", new TreeDemo());
        l13_tree.setParent(introLessons);
        lessonsTree.addNode(l13_tree);

        BaseTemplateTreeNode<DemoLesson> l14_responsiveapplication = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Responsive Application", "Layout Design","14", new ResponsiveApplicationDemo());
        l14_responsiveapplication.setParent(introLessons);
        lessonsTree.addNode(l14_responsiveapplication);

        BaseTemplateTreeNode<DemoLesson> l15_customizedcomponent = new BaseTemplateTreeNode<>(MaterialIcon.INDETERMINATE_CHECK_BOX, null ,"Customized Component", "build a TernaryCheckbox component","15", new TernaryCheckboxDemo());
        l15_customizedcomponent.setParent(introLessons);
        lessonsTree.addNode(l15_customizedcomponent);

        // Application Lessons

        BaseTemplateTreeNode<DemoLesson> applicationLessons = new BaseTemplateTreeNode<>(MaterialIcon.APPS, "Applications", "More than UI");
        lessonsTree.addNode(applicationLessons);

        BaseTemplateTreeNode<DemoLesson> l01_backgroundtasks = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Background Tasks", "Background calculations...","01", new BackgroundTasksDemo());
        l01_backgroundtasks.setParent(applicationLessons);
        lessonsTree.addNode(l01_backgroundtasks);

        BaseTemplateTreeNode<DemoLesson> l02_externalevent = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"External Event", "Events between sessions","02", new ExternalEventsDemo());
        l02_externalevent.setParent(applicationLessons);
        lessonsTree.addNode(l02_externalevent);

        BaseTemplateTreeNode<DemoLesson> l03_servlet = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"Servlet", "Create Events from an additional Endpoint","03", new ServletDemo());
        l03_servlet.setParent(applicationLessons);
        lessonsTree.addNode(l03_servlet);

        BaseTemplateTreeNode<DemoLesson> l04_mustachetemplate = new BaseTemplateTreeNode<>(MaterialIcon.INPUT, null ,"MustacheTemplate", "Versatile Custom Elements","03", new MustacheTemplateDemo());
        l04_mustachetemplate.setParent(applicationLessons);
        lessonsTree.addNode(l04_mustachetemplate);


        // Experimental Lessons
        BaseTemplateTreeNode<DemoLesson> experimentalLessons = new BaseTemplateTreeNode<>(MaterialIcon.FLASH_ON, "Experiments", "Just for Fun");
        lessonsTree.addNode(experimentalLessons);

        BaseTemplateTreeNode<DemoLesson> l99DemoLessonsApp = new BaseTemplateTreeNode<>(MaterialIcon.WEB, null ,"DemoLessonsApp", "The DemoLessonsApp itself",null, new DemoLessonsApp());
        l99DemoLessonsApp.setParent(experimentalLessons);
        lessonsTree.addNode(l99DemoLessonsApp);

        BaseTemplateTreeNode<DemoLesson> l05_iconViewer = new BaseTemplateTreeNode<>(AntuIcon.APP_GCSTAR_48, null ,"IconViewer", "IconViewer using InfiniteItemView",null, new IconViewerDemo());
        l05_iconViewer.setParent(experimentalLessons);
        lessonsTree.addNode(l05_iconViewer);

        // Issue Tracker

        BaseTemplateTreeNode<DemoLesson> issueTracker = new BaseTemplateTreeNode<>(MaterialIcon.BUG_REPORT, null,"IssueTracker", "Real Application with Database", null, new IssueTrackerApp());
        lessonsTree.addNode(issueTracker);

        // Hibernate SQL

//        BaseTemplateTreeNode<DemoLesson> hibernateSQL = new BaseTemplateTreeNode<>(MaterialIcon.STORAGE, null,"Hibernate SQL", "Integration of other databases", null, new HibernateSQLApp());
//        lessonsTree.addNode(hibernateSQL);

        BaseTemplateTreeNode<DemoLesson> l05_iconBrowser = new BaseTemplateTreeNode<>(EmojiIcon.OPEN_FILE_FOLDER, null ,"Icon Browser", "IconBrowser for Antu and Emoji Icons",null , new IconBrowser());
        // l05_iconBrowser.setParent(experimentalLessons);
        lessonsTree.addNode(l05_iconBrowser);

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
        WebController controller = sessionContext -> {

            // Start UniversalDB for IssueTrackerApp
            IssueTrackerApp.startDb();

            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);
            DemoLessonsApp demoLessonsApp = new DemoLessonsApp();
            demoLessonsApp.handleDemoSelected();
            rootPanel.setContent(demoLessonsApp.getRootComponent());

            // show Background Image
            String defaultBackground = "/resources/backgrounds/default-bl.jpg";
            sessionContext.registerBackgroundImage("default", defaultBackground, defaultBackground);
            sessionContext.setBackgroundImage("default", 0);
        };
        new TeamAppsJettyEmbeddedServer(controller, 8081).start();
    }
}
