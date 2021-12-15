package org.teamapps.demolessons.appserver.app;

import org.teamapps.application.api.application.Application;
import org.teamapps.demolessons.common.DemoLessonsApp;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.session.SessionContext;

public class DemoLessonsApplicationBuilder implements Application {

    public DemoLessonsApplicationBuilder() {
    }

    @Override
    public Component getUi() {
        return new DemoLessonsApp(SessionContext.current()).getRootComponent();
    }
}
