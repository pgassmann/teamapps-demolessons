package org.teamapps.demolessons.p2_application.l02_externalevent;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

public class ExternalEventsDemo implements DemoLesson {

    private SessionContext sessionContext;
    private NotificationManager notificationManager;
    private static NotificationManager staticNotificationManager = new NotificationManager();

    public ExternalEventsDemo(SessionContext sessionContext, NotificationManager notificationManager) {
        this.sessionContext = sessionContext;
        this.notificationManager = notificationManager;

        notificationManager.onNotificationPosted.addListener(text -> {
            sessionContext.showNotification(MaterialIcon.ALARM, text);
        });
    }

    // When created without notification Manager, use the global Notification manager defined as static Class variable
    public ExternalEventsDemo(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
        this.notificationManager = staticNotificationManager;

        notificationManager.onNotificationPosted.addListener(text -> {
            sessionContext.showNotification(MaterialIcon.ALARM, text);
        });
    }

    @Override
    public Component getRootComponent() {
        Panel panel = new Panel();
        Toolbar toolbar = new Toolbar();
        ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();

        ToolbarButton notifyButton = ToolbarButton.create(MaterialIcon.ALARM_ON, "Notify all!", "Will notify all users!");
        notifyButton.onClick.addListener(toolbarButtonClickEvent -> {
            notificationManager.postNotification("Somebody pressed the button!");
        });

        buttonGroup.addButton(notifyButton);
        toolbar.addButtonGroup(buttonGroup);
        panel.setToolbar(toolbar);
        return panel;
    }

    public static void main(String[] args) throws Exception {
        NotificationManager notificationManager = new NotificationManager();

        SimpleWebController controller = new SimpleWebController(sessionContext -> {
            ExternalEventsDemo textFieldDemo = new ExternalEventsDemo(sessionContext, notificationManager);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });

        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }

}
