package org.teamapps.demolessons.appserver.app.builder;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.application.perspective.AbstractApplicationPerspective;
import org.teamapps.application.api.application.perspective.ApplicationPerspective;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.server.system.session.PerspectiveSessionData;
import org.teamapps.common.format.Color;
import org.teamapps.databinding.MutableValue;
import org.teamapps.demolessons.common.DemoLessonsApp;
import org.teamapps.ux.application.layout.ExtendedLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.session.CurrentSessionContext;

public class OverviewPerspective extends AbstractApplicationPerspective {
    public OverviewPerspective(ApplicationInstanceData applicationInstanceData, MutableValue<String> perspectiveInfoBadgeValue) {
        super(applicationInstanceData, perspectiveInfoBadgeValue);
        PerspectiveSessionData perspectiveSessionData = (PerspectiveSessionData) getApplicationInstanceData();
        createUi();
    }

    private void createUi() {

        Perspective perspective = getPerspective();
        View websiteView = perspective.addView(View.createView(ExtendedLayout.LEFT, ApplicationIcons.WINDOW_EARTH, "Lessons", null));
        View pageView = perspective.addView(View.createView(ExtendedLayout.LEFT_BOTTOM, ApplicationIcons.DOCUMENT_TEXT, "Section", null));
        View contentView = perspective.addView(View.createView(ExtendedLayout.CENTER, ApplicationIcons.FORM, "Content", null));
        // View previewView = perspective.addView(View.createView(ExtendedLayout.RIGHT, ApplicationIcons.VIEW_1_1, "Vorschau", null));

        DemoLessonsApp demoLessonsApp = new DemoLessonsApp(CurrentSessionContext.get());
        demoLessonsApp.handleDemoSelected();
        contentView.setComponent(demoLessonsApp.getRootComponent());

        websiteView.getPanel().setBodyBackgroundColor(Color.WHITE.withAlpha(0.84f));
        pageView.getPanel().setBodyBackgroundColor(Color.WHITE.withAlpha(0.84f));
        //formView.getPanel().setBodyBackgroundColor(Color.WHITE.withAlpha(0.94f));
    }
}
