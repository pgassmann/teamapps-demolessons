package org.teamapps.demolessons.appserver;

import org.teamapps.application.server.EmbeddedApplicationServer;
import org.teamapps.demolessons.appserver.app.DemoApplicationBuilder;
import org.teamapps.demolessons.appserver.app.DemoLessonsApplicationBuilder;
import org.teamapps.demolessons.appserver.app.IssueTrackerApplicationBuilder;

import java.io.File;

public class TestServer {

    public static void main(String[] args) {
        EmbeddedApplicationServer.build()
                .setLogin("admin")
                .setPassword("admin")
                .setPort(8082)
                .setLanguage("de")
                .setDarkTheme(false)
                .setBasePath(new File("./server-data"))
                .setApplicationBuilders(
                        new DemoApplicationBuilder(),
                        new DemoLessonsApplicationBuilder(),
                        new IssueTrackerApplicationBuilder()
                )
                .startServer();
    }
}
