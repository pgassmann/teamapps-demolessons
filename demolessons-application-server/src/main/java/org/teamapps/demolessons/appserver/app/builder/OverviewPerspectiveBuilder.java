package org.teamapps.demolessons.appserver.app.builder;

import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.application.perspective.AbstractPerspectiveBuilder;
import org.teamapps.application.api.application.perspective.ApplicationPerspective;
import org.teamapps.application.api.privilege.ApplicationPrivilegeProvider;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.databinding.MutableValue;

public class OverviewPerspectiveBuilder extends AbstractPerspectiveBuilder {

    public OverviewPerspectiveBuilder() {
        super("demoLessonsBuilder", ApplicationIcons.WINDOW_EARTH, "DemoLessons builder", "Demo");
    }

    @Override
    public boolean isPerspectiveAccessible(ApplicationPrivilegeProvider privilegeProvider) {
        return true;
    }

    @Override
    public boolean autoProvisionPerspective() {
        return true;
    }

    @Override
    public ApplicationPerspective build(ApplicationInstanceData applicationInstanceData, MutableValue<String> perspectiveInfoBadgeValue) {
        return new OverviewPerspective(applicationInstanceData, perspectiveInfoBadgeValue);
    }
}
