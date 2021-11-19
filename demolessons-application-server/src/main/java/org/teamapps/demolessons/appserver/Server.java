package org.teamapps.demolessons.appserver;

import org.teamapps.application.api.password.SecurePasswordHash;
import org.teamapps.application.server.system.bootstrap.BootstrapSessionHandler;
import org.teamapps.application.server.system.server.ApplicationServer;
import org.teamapps.application.server.system.server.SessionRegistryHandler;
import org.teamapps.application.server.system.session.UserSessionData;
import org.teamapps.application.server.system.utils.ValueConverterUtils;
import org.teamapps.demolessons.appserver.app.DemoApplicationBuilder;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.icon.material.MaterialIconStyles;
import org.teamapps.model.controlcenter.User;
import org.teamapps.model.controlcenter.UserAccountStatus;
import org.teamapps.ux.session.SessionContext;

import java.io.File;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) throws Exception {
        File basePath = new File("./data");
        ApplicationServer applicationServer = new ApplicationServer(basePath);
        BootstrapSessionHandler bootstrapSessionHandler = new BootstrapSessionHandler(new SessionRegistryHandler() {
            @Override
            public void handleNewSession(SessionContext context) {
                context.getIconProvider().setDefaultStyleForIconClass(MaterialIcon.class, MaterialIconStyles.PLAIN_LIGHT_BLUE_700);
                // context.getIconProvider().setDefaultStyleForIconClass(StandardIcon.class, StandardIconStyles.VIVID_STANDARD_SHADOW_1);

            }

            @Override
            public void handleAuthenticatedUser(UserSessionData userSessionData, SessionContext sessionContext) {

            }
        });
        applicationServer.setSessionHandler(bootstrapSessionHandler);
        applicationServer.start();

        bootstrapSessionHandler.getSystemRegistry().installAndLoadApplication(new DemoApplicationBuilder());

        if (User.getCount() == 0) {
            User user = User.create()
                    .setFirstName("Super")
                    .setLastName("Admin")
                    .setLogin("admin")
                    .setPassword(SecurePasswordHash.createDefault().createSecureHash("teamapps!"))
                    .setUserAccountStatus(UserAccountStatus.SUPER_ADMIN)
                    .setLanguages(ValueConverterUtils.compressStringList(Arrays.asList("de", "en", "fr")))
                    .save();
        }


    }
}
