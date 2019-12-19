package org.teamapps.demo.lessons.l16_servlet;

import com.google.common.io.Files;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.ServletRegistration;
import org.teamapps.server.UxServerContext;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.DisplayField;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;
import org.teamapps.webcontroller.WebController;

import java.util.Arrays;
import java.util.Collection;

public class ServletDemo implements DemoLesson {

    private SessionContext sessionContext;
    private ServletNotificationManager notificationManager;
    private static ServletNotificationManager staticNotificationManager = new ServletNotificationManager();

    public ServletDemo(SessionContext sessionContext, ServletNotificationManager notificationManager) {
        this.sessionContext = sessionContext;
        this.notificationManager = notificationManager;

        notificationManager.onNotificationPosted.addListener(text -> {
            sessionContext.showNotification(MaterialIcon.ALARM, text);
        });
    }
    public ServletDemo(SessionContext sessionContext) {
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
        DisplayField infoMessage = new DisplayField();
        infoMessage.setShowHtml(true);
        infoMessage.setValue("This App has an api. Click here and see a notification in the current window: <a href='/api/foo' target=_blank>/api/foo</a><br>" +
                "The API only works when run standalone from ServletDemo.main()");
        panel.setContent(infoMessage);
        return panel;
    }

    public static void main1(String[] args) throws Exception {
        ServletNotificationManager notificationManager = new ServletNotificationManager();

        SimpleWebController controller = new SimpleWebController(sessionContext -> {
            ServletDemo textFieldDemo = new ServletDemo(sessionContext, notificationManager);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });

        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }

    public static void main(String[] args) throws Exception {

        ServletNotificationManager notificationManager = new ServletNotificationManager();

        // Additional Servlets require the use of Webcontroller instead of SimpleWebcontroller.
        // WebController requires a onSession Start method that creates a RootPanel
        WebController controller = new WebController() {
            @Override
            public void onSessionStart(SessionContext sessionContext) {
                RootPanel rootPanel = new RootPanel();
                sessionContext.addRootComponent(null, rootPanel);

                ServletDemo textFieldDemo = new ServletDemo(sessionContext, notificationManager);
                textFieldDemo.handleDemoSelected();
                rootPanel.setContent(textFieldDemo.getRootComponent());
            }

            @Override
            public Collection<ServletRegistration> getServletRegistrations(UxServerContext serverContext) {
                return Arrays.asList(
                        new ServletRegistration(new MyServlet(notificationManager), "/api/*")
                );
            }
        };
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }

}
