package org.teamapps.demolessons.p1_intro.l14_responsiveapplication;

import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.webcontroller.WebController;

// Example of teamapps project README.md https://github.com/teamapps-org/teamapps
public class TeamAppsDemo {

    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            //create a responsive application that will run on desktops as well as on smart phones
            ResponsiveApplication application = ResponsiveApplication.createApplication();

            //create perspective with default layout
            Perspective perspective = Perspective.createPerspective();
            application.addPerspective(perspective);

            //create an empty left panel
            perspective.addView(View.createView(StandardLayout.LEFT, MaterialIcon.MESSAGE, "Left panel", null));

            //create a tabbed center panel
            perspective.addView(View.createView(StandardLayout.CENTER, MaterialIcon.SEARCH, "Center panel", null));
            perspective.addView(View.createView(StandardLayout.CENTER, MaterialIcon.PEOPLE, "Center panel 2", null));

            //create a right panel
            perspective.addView(View.createView(StandardLayout.RIGHT, MaterialIcon.FOLDER, "Left panel", null));

            //create a right bottom panel
            perspective.addView(View.createView(StandardLayout.RIGHT_BOTTOM, MaterialIcon.VIEW_CAROUSEL, "Left bottom panel", null));

            //create toolbar buttons
            ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();
            buttonGroup.addButton(ToolbarButton.create(MaterialIcon.SAVE, "Save", "Save changes")).onClick.addListener(toolbarButtonClickEvent -> {
                sessionContext.showNotification(MaterialIcon.MESSAGE, "Save was clicked!");
            });
            buttonGroup.addButton(ToolbarButton.create(MaterialIcon.DELETE, "Delete", "Delete some items"));

            //display these buttons only when this perspective is visible
            perspective.addWorkspaceButtonGroup(buttonGroup);

            application.showPerspective(perspective);
            rootPanel.setContent(application.getUi());

            // set Background Image
            String defaultBackground = "/resources/backgrounds/default-bl.jpg";
            sessionContext.registerBackgroundImage("default", defaultBackground, defaultBackground);
            sessionContext.setBackgroundImage("default", 0);
        };

        new TeamAppsJettyEmbeddedServer(controller, 8080).start();
    }
}
