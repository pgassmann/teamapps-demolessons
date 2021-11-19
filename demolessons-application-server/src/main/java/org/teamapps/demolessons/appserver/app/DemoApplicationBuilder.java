package org.teamapps.demolessons.appserver.app;

import org.teamapps.application.api.application.AbstractApplicationBuilder;
import org.teamapps.application.api.application.perspective.PerspectiveBuilder;
import org.teamapps.application.api.config.ApplicationConfig;
import org.teamapps.application.api.localization.LocalizationData;
import org.teamapps.application.api.privilege.ApplicationPrivilegeProvider;
import org.teamapps.application.api.privilege.ApplicationRole;
import org.teamapps.application.api.privilege.PrivilegeGroup;
import org.teamapps.application.api.theme.ApplicationIcons;
import org.teamapps.application.api.versioning.ApplicationVersion;
import org.teamapps.demolessons.appserver.app.builder.OverviewPerspectiveBuilder;
import org.teamapps.icons.Icon;
import org.teamapps.universaldb.schema.SchemaInfoProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DemoApplicationBuilder extends AbstractApplicationBuilder {

    public DemoApplicationBuilder() {
        super("DemoLessons", ApplicationIcons.LANTERN_ON, "DemoLessons", "Demo Lessons in Application Server");
    }

    @Override
    public List<PerspectiveBuilder> getPerspectiveBuilders() {
        return Arrays.asList(
                new OverviewPerspectiveBuilder()
        );
    }

    @Override
    public ApplicationVersion getApplicationVersion() {
        return ApplicationVersion.create(0, 0, 1);
    }

    @Override
    public List<ApplicationRole> getApplicationRoles() {
        return Collections.emptyList();
    }

    @Override
    public List<PrivilegeGroup> getPrivilegeGroups() {
        return Collections.emptyList();
    }

    @Override
    public LocalizationData getLocalizationData() {
        return LocalizationData.createFromPropertyFiles("org.teamapps.demolessons.i18n.captions", getClass().getClassLoader(), Locale.ENGLISH);
    }

    @Override
    public SchemaInfoProvider getDatabaseModel() {
        return null;
    }

    @Override
    public ApplicationConfig getApplicationConfig() {
        return null;
    }

    @Override
    public boolean useToolbarApplicationMenu() {
        return false;
    }

    @Override
    public boolean isApplicationAccessible(ApplicationPrivilegeProvider applicationPrivilegeProvider) {
        return true;
    }
}
