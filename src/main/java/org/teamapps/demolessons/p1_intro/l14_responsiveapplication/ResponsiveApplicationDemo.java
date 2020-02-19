package org.teamapps.demolessons.p1_intro.l14_responsiveapplication;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.demolessons.p1_intro.l04_richtexteditor.RichTextEditorDemo;
import org.teamapps.demolessons.p1_intro.l10_responsiveform.ResponsiveFormDemo;
import org.teamapps.demolessons.p1_intro.l11_table.TableDemo;
import org.teamapps.demolessons.p1_intro.l13_tree.TreeDemo;
import org.teamapps.demolessons.p1_intro.l07_checkbox.CheckboxDemo;
import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.antu.AntuIconProvider;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.icon.material.MaterialIconStyles;
import org.teamapps.icon.materialdesign.MaterialDesignIcon;
import org.teamapps.icon.materialdesign.MaterialDesignIconProvider;
import org.teamapps.icon.materialdesign.MaterialDesignIconStyles;
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
        perspective.addView(View.createView(StandardLayout.LEFT, AntuIcon.STATUS.BATTERY_MISSING_64, "Left panel", new DummyComponent()));

        //create a tabbed center panel
        perspective.addView(View.createView(StandardLayout.CENTER, AntuIcon.ACTION.ACROBAT_32, "Center panel", new DummyComponent()));
        perspective.addView(View.createView(StandardLayout.CENTER, AntuIcon.STATUS.MIC_OFF_22, "Center panel 2", new DummyComponent()));

        //create a right panel
        perspective.addView(View.createView(StandardLayout.RIGHT, MaterialIcon.FOLDER, "Left panel", new DummyComponent()));

        //create a right bottom panel
        perspective.addView(View.createView(StandardLayout.RIGHT_BOTTOM, MaterialIcon.VIEW_CAROUSEL, "Left bottom panel", null));

        //create toolbar buttons
        ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();
        buttonGroup.addButton(ToolbarButton.create(AntuIcon.ACTION.CHRONOMETER_RESET_24, "AntuIcon.ACTION.CHRONOMETER_RESET_24", "Save changes")).onClick.addListener(toolbarButtonClickEvent -> {
            CurrentSessionContext.get().showNotification(MaterialIcon.MESSAGE, "Save was clicked!");
        });
        buttonGroup.addButton(ToolbarButton.create(AntuIcon.PLACES.DISTRIBUTOR_LOGO_UBUNTU_64, "AntuIcon.PLACES.DISTRIBUTOR_LOGO_UBUNTU_64", "Delete some items"));
        buttonGroup.addButton(ToolbarButton.create(MaterialDesignIcon.DICE._4.withStyle(MaterialDesignIconStyles.LIGHT_BLUE_900), "MaterialDesignIcon.DICE._4", "Delete some items"));
        buttonGroup.addButton(ToolbarButton.create(AntuIcon.ACTION.CALL_STOP_32, "AntuIcon.ACTION.CALL_STOP_32", "Delete some items"));
        buttonGroup.addButton(ToolbarButton.create(MaterialIcon.LAYERS, "MaterialIcon.LAYERS", "Delete some items"));
        buttonGroup.addButton(ToolbarButton.create(AntuIcon.STATUS.SECURITY_HIGH_64, "AntuIcon.STATUS.SECURITY_HIGH_64", "Delete some items"));

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

        ToolbarButton switchButton = ToolbarButton.create(AntuIcon.ACTION.SHOW_MENU_24, "AntuIcon.ACTION.SHOW_MENU_24","");
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

        View rightView2 = View.createView(ExtendedLayout.RIGHT, MaterialIcon.HELP, "RIGHT 2", null);
        demoPerspective.addView(rightView2);

        View RightBottomView = View.createView(ExtendedLayout.RIGHT_BOTTOM, MaterialIcon.HELP, "RIGHT_BOTTOM", new RichTextEditorDemo(context).getRootComponent());
        demoPerspective.addView(RightBottomView);

        View outerRightView = View.createView(ExtendedLayout.OUTER_RIGHT, MaterialIcon.HELP, "OUTER_RIGHT", new CheckboxDemo(context).getRootComponent());
        demoPerspective.addView(outerRightView);

        return demoPerspective;
    }

    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            ResponsiveApplicationDemo responsiveApplicationDemo = new ResponsiveApplicationDemo(context);
            responsiveApplicationDemo.handleDemoSelected();
            return responsiveApplicationDemo.getRootComponent();
        });
        controller.addAdditionalIconProvider(new MaterialDesignIconProvider());
        controller.addAdditionalIconProvider(new AntuIconProvider());
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}
