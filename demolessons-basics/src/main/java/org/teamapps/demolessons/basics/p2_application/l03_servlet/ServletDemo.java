package org.teamapps.demolessons.basics.p2_application.l03_servlet;

import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.DisplayField;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.toolbar.Toolbar;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

public class ServletDemo implements DemoLesson {

    private ServletNotificationManager notificationManager;
    private static ServletNotificationManager staticNotificationManager = new ServletNotificationManager();

    public ServletDemo(SessionContext sessionContext, ServletNotificationManager notificationManager) {
        this.notificationManager = notificationManager;

        notificationManager.onNotificationPosted.addListener(text -> sessionContext.showNotification(MaterialIcon.ALARM, text));
    }
    public ServletDemo(SessionContext sessionContext) {
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
        DisplayField infoMessage = new DisplayField();
        infoMessage.setShowHtml(true);
        infoMessage.setValue("This App has an api. Click here and see a notification in the current window: <a href='/api/foo' target=_blank>/api/foo</a><br>" +
                "The API only works when run standalone from ServletDemo.main()");
        panel.setContent(infoMessage);
        return panel;
    }


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {

        // Create an instance of ServletNotificationManager outside of the Webcontroller (outside of a SessionContext)
        // So one instance of NotificationManager can be shared by all user sessions
        ServletNotificationManager notificationManager = new ServletNotificationManager();

        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            // pass notificationManager as second argument
            DemoLesson demo = new ServletDemo(sessionContext, notificationManager);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };

        // Create server, but don't start directly.
        TeamAppsJettyEmbeddedServer server = new TeamAppsJettyEmbeddedServer(controller);

        // Register Servlet in the TeamAppsJettyEmbeddedServer
        server.addServletContextListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {

                // Add servlet with a name and an Instance of the Servlet.
                // add url mapping to Servlet Registration
                MyServlet servlet = new MyServlet(notificationManager);
                ServletRegistration.Dynamic servletRegistration = sce.getServletContext().addServlet("servletdemo", servlet);
                servletRegistration.addMapping("/api/*");

                // in one line
                sce.getServletContext().addServlet("servletdemo2", new MyServlet(notificationManager)).addMapping("/apiv2/*");
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {  }
        });

        // Start Server after adding the Servlet
        server.start();
    }

}
