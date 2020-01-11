package org.teamapps.demolessons.p1_intro.l14_responsiveapplication;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.demolessons.p1_intro.l10_responsiveform.ResponsiveFormDemo;
import org.teamapps.demolessons.p1_intro.l11_table.TableDemo;
import org.teamapps.demolessons.p1_intro.l13_tree.TreeDemo;
import org.teamapps.demolessons.p1_intro.l07_checkbox.CheckboxDemo;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.ExtendedLayout;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.CurrentSessionContext;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class ResponsiveApplicationDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    // Constructor, only set session context instance variable
    public ResponsiveApplicationDemo(SessionContext context) {
        this.context = context;

    }

    @Override
    public void handleDemoSelected() {
        this.rootComponent = createRootComponent();
//        this.rootComponent = createRootComponentwithDemos();
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
        perspective.addView(View.createView(StandardLayout.LEFT, MaterialIcon.MESSAGE, "Left panel", new DummyComponent()));

        //create a tabbed center panel
        perspective.addView(View.createView(StandardLayout.CENTER, MaterialIcon.SEARCH, "Center panel", new DummyComponent()));
        perspective.addView(View.createView(StandardLayout.CENTER, MaterialIcon.PEOPLE, "Center panel 2", new DummyComponent()));

        //create a right panel
        perspective.addView(View.createView(StandardLayout.RIGHT, MaterialIcon.FOLDER, "Left panel", new DummyComponent()));

        //create a right bottom panel
        perspective.addView(View.createView(StandardLayout.RIGHT_BOTTOM, MaterialIcon.VIEW_CAROUSEL, "Left bottom panel", null));

        //create toolbar buttons
        ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();
        buttonGroup.addButton(ToolbarButton.create(MaterialIcon.SAVE, "Save", "Save changes")).onClick.addListener(toolbarButtonClickEvent -> {
            CurrentSessionContext.get().showNotification(MaterialIcon.MESSAGE, "Save was clicked!");
        });
        buttonGroup.addButton(ToolbarButton.create(MaterialIcon.DELETE, "Delete", "Delete some items"));

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

        ToolbarButton switchButton = ToolbarButton.create(MaterialIcon.LAYERS, "Switch Perspective","");
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

        leftTreeView.setComponent(new TreeDemo(context).getRootComponent());

        View mainView = View.createView(ExtendedLayout.CENTER, MaterialIcon.HELP, "CENTER", new DummyComponent());
        demoPerspective.addView(mainView);
        mainView.setComponent(new TableDemo(context).getRootComponent());


        View rightView = View.createView(ExtendedLayout.RIGHT, MaterialIcon.HELP, "RIGHT", new DummyComponent());
        demoPerspective.addView(rightView);
        rightView.setComponent(new ResponsiveFormDemo(context).getRootComponent());

        View rightView2 = View.createView(ExtendedLayout.RIGHT, MaterialIcon.HELP, "RIGHT 2", new CheckboxDemo(context).getRootComponent());
        demoPerspective.addView(rightView2);

        View outerRightView = View.createView(ExtendedLayout.OUTER_RIGHT, MaterialIcon.HELP, "OUTER_RIGHT", new DummyComponent());
        demoPerspective.addView(outerRightView);

        View outerRightBottomView = View.createView(ExtendedLayout.OUTER_RIGHT_BOTTOM, MaterialIcon.HELP, "OUTER_RIGHT_BOTTOM", new DummyComponent());
        demoPerspective.addView(outerRightBottomView);
        return demoPerspective;
    }

    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            ResponsiveApplicationDemo responsiveApplicationDemo = new ResponsiveApplicationDemo(context);
            responsiveApplicationDemo.handleDemoSelected();
            return responsiveApplicationDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}
