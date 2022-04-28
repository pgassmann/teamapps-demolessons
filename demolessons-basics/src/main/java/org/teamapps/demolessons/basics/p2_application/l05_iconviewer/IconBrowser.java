package org.teamapps.demolessons.basics.p2_application.l05_iconviewer;

import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.antu.AntuIconBrowser;
import org.teamapps.icon.emoji.EmojiIconBrowser;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.tabpanel.Tab;
import org.teamapps.ux.component.tabpanel.TabPanel;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

public class IconBrowser implements DemoLesson {

    private final TabPanel tabPanel;

    public IconBrowser() {
        SessionContext sessionContext = SessionContext.current();
        tabPanel = new TabPanel();
        Component emojiIconBrowser = new EmojiIconBrowser(sessionContext).getUI();
        Tab emojiTab = new Tab().setContent(emojiIconBrowser).setTitle("EmojiIcon");
        Component antuIconBrowser = new AntuIconBrowser(sessionContext).getUI();
        Tab antuTab = new Tab().setContent(antuIconBrowser).setTitle("AntuIcon");
        tabPanel.addTab(emojiTab);
        tabPanel.addTab(antuTab);
    }

    @Override
    public Component getRootComponent() {
        return tabPanel;
    }

    @Override
    public void handleDemoSelected() {
        DemoLesson.super.handleDemoSelected();
    }

    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            IconBrowser iconBrowser = new IconBrowser();

            // call the method defined in the DemoLesson Interface

            // rootPanel.setContent(demo.getRootComponent());
            rootPanel.setContent(iconBrowser.getRootComponent());


        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
