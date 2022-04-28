package org.teamapps.demolessons.appserver.app;

import org.teamapps.application.api.application.AbstractBaseApplicationBuilder;
import org.teamapps.application.api.application.ApplicationInstanceData;
import org.teamapps.application.api.config.ApplicationConfig;
import org.teamapps.application.api.localization.LocalizationData;
import org.teamapps.application.api.privilege.ApplicationPrivilegeProvider;
import org.teamapps.application.api.privilege.ApplicationRole;
import org.teamapps.application.api.privilege.PrivilegeGroup;
import org.teamapps.application.api.versioning.ApplicationVersion;
import org.teamapps.demolessons.common.DemoLessonsApp;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.universaldb.schema.SchemaInfoProvider;
import org.teamapps.ux.application.ResponsiveApplication;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class DemoLessonsApplicationBuilder extends AbstractBaseApplicationBuilder {
    public DemoLessonsApplicationBuilder() {
        super("demoLessons", MaterialIcon.HELP, "Demo Lessons App", "Learn TeamApps");
    }

    @Override
    public ApplicationVersion getApplicationVersion() {
        return ApplicationVersion.create(0,2);
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
        // return new IssueTrackerSchema();
        return null;
    }

    @Override
    public ApplicationConfig getApplicationConfig() {
        return null;
    }

    @Override
    public boolean isApplicationAccessible(ApplicationPrivilegeProvider applicationPrivilegeProvider) {
        return true;
    }

    @Override
    public void build(ResponsiveApplication responsiveApplication, ApplicationInstanceData applicationInstanceData) {
        new DemoLessonsApp(responsiveApplication).handleDemoSelected();
    }
}
