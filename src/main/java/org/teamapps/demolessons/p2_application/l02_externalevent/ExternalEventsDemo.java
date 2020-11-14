package org.teamapps.demolessons.p2_application.l02_externalevent;

import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

public class ExternalEventsDemo implements DemoLesson {

    private SessionContext sessionContext;
    private NotificationManager notificationManager;
    private static NotificationManager staticNotificationManager = new NotificationManager();

    public ExternalEventsDemo(SessionContext sessionContext, NotificationManager notificationManager) {
        this.sessionContext = sessionContext;
        this.notificationManager = notificationManager;

        notificationManager.onNotificationPosted.addListener(text -> sessionContext.showNotification(MaterialIcon.ALARM, text));
    }

    // When created without notification Manager, use the global Notification manager defined as static Class variable
    public ExternalEventsDemo(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
        this.notificationManager = staticNotificationManager;

        notificationManager.onNotificationPosted.addListener(text -> sessionContext.showNotification(MaterialIcon.ALARM, text));
    }

    @Override
    public Component getRootComponent() {
        Panel panel = new Panel();
        Toolbar toolbar = new Toolbar();
        ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();

        ToolbarButton notifyButton = ToolbarButton.create(MaterialIcon.ALARM_ON, "Notify all!", "Will notify all users!");
        notifyButton.onClick.addListener(toolbarButtonClickEvent -> notificationManager.postNotification("Somebody pressed the button!"));

        buttonGroup.addButton(notifyButton);
        toolbar.addButtonGroup(buttonGroup);
        panel.setToolbar(toolbar);
        return panel;
    }

    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {

        // Create an instance of NotificationManager outside of the Webcontroller (outside of a SessionContext)
        // So one instance of NotificationManager can be shared by all user sessions
        NotificationManager notificationManager = new NotificationManager();

        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            // pass notificationManager as second argument
            DemoLesson demo = new ExternalEventsDemo(sessionContext, notificationManager);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
