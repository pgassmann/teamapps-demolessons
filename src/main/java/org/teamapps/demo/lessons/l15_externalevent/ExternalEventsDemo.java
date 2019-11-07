package org.teamapps.demo.lessons.l15_externalevent;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.demo.lessons.l13_tree.TreeDemo;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.progress.ProgressDisplay;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.ux.task.ObservableProgress;
import org.teamapps.ux.task.ProgressCompletableFuture;
import org.teamapps.ux.task.ProgressMonitor;
import org.teamapps.webcontroller.SimpleWebController;

import javax.servlet.ServletException;
import javax.swing.ButtonGroup;
import java.util.concurrent.ThreadLocalRandom;

public class ExternalEventsDemo implements DemoLesson {

    private SessionContext sessionContext;
    private NotificationManager notificationManager;

    public ExternalEventsDemo(SessionContext sessionContext, NotificationManager notificationManager) {
        this.sessionContext = sessionContext;
        this.notificationManager = notificationManager;

        notificationManager.onNotificationPosted.addListener(text -> {
            sessionContext.showNotification(MaterialIcon.ALARM, text);
        });
    }

    @Override
    public Component getRootComponent() {
        Panel panel = new Panel();
        Toolbar toolbar = new Toolbar();
        ToolbarButtonGroup buttonGroup = new ToolbarButtonGroup();

        ToolbarButton notifyButton = ToolbarButton.create(MaterialIcon.ALARM_ON, "Notify all!", "Will notify all!");
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
