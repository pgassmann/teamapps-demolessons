package org.teamapps.demolessons.basics.p1_intro.l14_responsiveapplication;

import org.teamapps.common.format.Color;
import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.demolessons.basics.p1_intro.l04_richtexteditor.RichTextEditorDemo;
import org.teamapps.demolessons.basics.p1_intro.l07_checkbox.CheckboxDemo;
import org.teamapps.demolessons.basics.p1_intro.l10_responsiveform.ResponsiveFormDemo;
import org.teamapps.demolessons.basics.p1_intro.l11_table.TableDemo;
import org.teamapps.demolessons.basics.p1_intro.l13_tree.TreeDemo;
import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.antu.AntuIconStyle;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.ExtendedLayout;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.CurrentSessionContext;
import org.teamapps.webcontroller.WebController;

public class ResponsiveApplicationDemo implements DemoLesson {

    private final Component rootComponent;

    // Constructor, only set session context instance variable
    public ResponsiveApplicationDemo() {
        this.rootComponent = createRootComponent();
    }

    @Override
    public void handleDemoSelected() {
    }

    public Component getRootComponent() {
        return rootComponent;
    }

    private Component createRootComponent() {
        //create a responsive application that will run on desktops as well as on smart phones
        ResponsiveApplication application = ResponsiveApplication.createApplication();

        //create perspective with default layout
        Perspective perspective = Perspective.createPerspective();
        application.addPerspective(perspective);

        //create an empty left panel
        perspective.addView(View.createView(StandardLayout.LEFT, AntuIcon.STATUS_BATTERY_MISSING_64, "Left panel", new DummyComponent()));

        //create a tabbed center panel
        perspective.addView(View.createView(StandardLayout.CENTER, AntuIcon.ACTION_ACROBAT_32, "Center panel", new DummyComponent()));
        perspective.addView(View.createView(StandardLayout.CENTER, AntuIcon.STATUS_MIC_OFF_22.withStyle(AntuIconStyle.DARK), "Center panel 2", new DummyComponent())).getPanel().setHeaderBackgroundColor(Color.BLACK.withAlpha(0.6f));
//        System.out.println(AntuIcon.class.getFields().length);
//        Arrays.stream(AntuIcon.class.getFields()).forEach(System.out::println);

        //create a right panel
        perspective.addView(View.createView(StandardLayout.RIGHT, MaterialIcon.FOLDER, "Left panel", new DummyComponent()));

        //create a right bottom panel
        perspective.addView(View.createView(StandardLayout.RIGHT_BOTTOM, MaterialIcon.VIEW_CAROUSEL, "Left bottom panel", null));

        //create toolbar buttons
        ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();
        ToolbarButton saveButton = buttonGroup.addButton(ToolbarButton.create(MaterialIcon.SAVE, "Save", "Save changes"));
        saveButton.onClick.addListener(toolbarButtonClickEvent -> CurrentSessionContext.get().showNotification(MaterialIcon.MESSAGE, "Save was clicked!"));
        buttonGroup.addButton(ToolbarButton.create(MaterialIcon.DELETE, "Delete", "Delete some items"));
        buttonGroup.addButton(ToolbarButton.create(AntuIcon.ACTION_CONTRAST_24.withStyle(AntuIconStyle.DARK), "DARK Style", "AntuIcon.ACTION_CONTRAST_24").setBackgroundColor(Color.BLACK.withAlpha(0.6f)));
        buttonGroup.addButton(ToolbarButton.create(AntuIcon.ACTION_AUTOCORRECTION_32.withStyle(AntuIconStyle.LIGHT), "LIGHT Style", "AntuIcon.ACTION_AUTOCORRECTION_32").setBackgroundColor(Color.LIGHT_YELLOW.withAlpha(0.8f)));
        buttonGroup.addButton(ToolbarButton.create(MaterialIcon.LAYERS, "MaterialIcon.LAYERS", "Delete some items"));
        //display these buttons only when this perspective is visible
        perspective.addWorkspaceButtonGroup(buttonGroup);
        application.showPerspective(perspective);


        // Add a second perspective
        Perspective demoPerspective = createDemoPerspective(application);
        application.addPerspective(demoPerspective);

        // Switch perspective in Toolbar
        ToolbarButtonGroup switchButtonGroup = new ToolbarButtonGroup();
        perspective.addWorkspaceButtonGroup(switchButtonGroup);
        demoPerspective.addWorkspaceButtonGroup(switchButtonGroup);

        ToolbarButton switchButton = ToolbarButton.create(AntuIcon.ACTION_KT_CHANGE_TRACKER_24, "Perspective","Switch perspective");
        switchButtonGroup.addButton(switchButton);
        switchButton.onClick.addListener(toolbarButtonClickEvent -> {
            if (application.getActivePerspective() == demoPerspective) {
                application.showPerspective(perspective);
            } else {
                application.showPerspective(demoPerspective);
            }
        });
        return application.getUi();
    }

    private Perspective createDemoPerspective(ResponsiveApplication application) {
        //// Second Perspective, filled with demos
        Perspective demoPerspective = Perspective.createPerspective(ExtendedLayout.createLayout());
        application.addPerspective(demoPerspective);

        View leftTreeView = View.createView(ExtendedLayout.LEFT, MaterialIcon.HELP, "LEFT", new DummyComponent());
        demoPerspective.addView(leftTreeView);

        leftTreeView.setComponent(new TreeDemo().getRootComponent());

        View mainView = View.createView(ExtendedLayout.CENTER, MaterialIcon.HELP, "CENTER", new DummyComponent());
        demoPerspective.addView(mainView);
        mainView.setComponent(new TableDemo().getRootComponent());


        View rightView = View.createView(ExtendedLayout.RIGHT, MaterialIcon.HELP, "RIGHT", new DummyComponent());
        demoPerspective.addView(rightView);
        rightView.setComponent(new ResponsiveFormDemo().getRootComponent());

        View rightView2 = View.createView(ExtendedLayout.RIGHT, MaterialIcon.HELP, "RIGHT 2", null);
        demoPerspective.addView(rightView2);

        View RightBottomView = View.createView(ExtendedLayout.RIGHT_BOTTOM, MaterialIcon.HELP, "RIGHT_BOTTOM", new RichTextEditorDemo().getRootComponent());
        demoPerspective.addView(RightBottomView);

        View outerRightView = View.createView(ExtendedLayout.OUTER_RIGHT, MaterialIcon.HELP, "OUTER_RIGHT", new CheckboxDemo().getRootComponent());
        demoPerspective.addView(outerRightView);

        return demoPerspective;
    }

    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            DemoLesson demo = new ResponsiveApplicationDemo();

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
