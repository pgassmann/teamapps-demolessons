package org.teamapps.demolessons.basics.p2_application.l06_sessioncontext;

import org.teamapps.config.TeamAppsConfiguration;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.undertow.embedded.TeamAppsUndertowEmbeddedServer;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.rootpanel.RootPanel;

public class SessionContextLifecycleDemo {

//     A session is started when the browser loads the page
//     A session can be ended for multiple reasons:
    // 1. Browser closes or reloads Page/Tab/Window
    // 2. Exception occurs within SessionContext
    // 3. Timeout
    // 4. Commandbuffer Overflow
    // 5. Server is restarted -> Client Session unknown

    public static void main(String[] args) throws Exception {
        TeamAppsConfiguration config = new TeamAppsConfiguration();
        // /* Simulate Timeout */
        // config.setUiSessionTimeoutMillis(10_000);
        // config.setKeepaliveMessageIntervalMillis(25_000);

        new TeamAppsUndertowEmbeddedServer(sessionContext -> {
            System.out.println("new session has started: " + sessionContext.getSessionId());

            sessionContext.onDestroyed.addListener(() -> System.out.println("session has ended: " + sessionContext.getSessionId()));

            RootPanel rootPanel = sessionContext.addRootPanel();
            Button<?> button = Button.create("Click me!");
            button.onClicked.addListener(() -> {
                /* Throw Exception within SessionContext */
                // throw new RuntimeException("Alles kaputt!");
                // sessionContext.destroy();

                /* Provoke Command Buffer Overflow*/
                for (int i = 0; i < 20_000; i++) {
                    sessionContext.showNotification(MaterialIcon.ALARM_ON, "!!!!");
                }
            });
            rootPanel.setContent(button);
        }, config, 8089).start();
    }

}
