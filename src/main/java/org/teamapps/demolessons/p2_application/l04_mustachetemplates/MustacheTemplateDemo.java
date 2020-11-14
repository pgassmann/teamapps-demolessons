package org.teamapps.demolessons.p2_application.l04_mustachetemplates;

import org.apache.commons.io.IOUtils;
import org.teamapps.data.extract.BeanPropertyExtractor;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.infiniteitemview.InfiniteItemView;
import org.teamapps.ux.component.infiniteitemview.ListInfiniteItemViewModel;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.htmltemplate.MustacheTemplate;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class MustacheTemplateDemo implements DemoLesson {

	private SessionContext sessionContext;

	public MustacheTemplateDemo(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	@Override
	public Component getRootComponent() {
		Panel panel = new Panel();
		InfiniteItemView<StoreItem> itemView = new InfiniteItemView<>();
        itemView.setItemTemplate(new MustacheTemplate(loadResourceString("/org/teamapps/demolessons/p2_application/l04_mustachetemplates/store-item.html")));
        ListInfiniteItemViewModel<StoreItem> model = new ListInfiniteItemViewModel<>();
        model.addRecord(new StoreItem("My first book", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/BLW_Manuscript_Book_of_Hours%2C_about_1480-90.jpg/640px-BLW_Manuscript_Book_of_Hours%2C_about_1480-90.jpg", 27.5, LocalDate.of(1990, 2, 23)));
        model.addRecord(new StoreItem("Coconut", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Coconut_on_white_background.jpg/640px-Coconut_on_white_background.jpg", 2.95, LocalDate.now().minusDays(2)));
        itemView.setModel(model);
        itemView.setItemWidth(0);
        itemView.setRowHeight(100);

        // add custom property isNew to (default) BeanPropertyExtractor
        itemView.setItemPropertyExtractor(new BeanPropertyExtractor<StoreItem>().addProperty("isNew", storeItem -> LocalDate.now().minusDays(30).compareTo(storeItem.getReleaseDate()) < 0));

        panel.setContent(itemView);
        panel.setTitle("My Shop");
        panel.setIcon(MaterialIcon.SHOP);
		return panel;
	}

    private static String loadResourceString(String resourceName) {
        try {
            return IOUtils.resourceToString(resourceName, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Resource not found.", e);
        }
    }


	// main method to launch the Demo standalone
	public static void main(String[] args) throws Exception {
		WebController controller = sessionContext -> {
			RootPanel rootPanel = new RootPanel();
			sessionContext.addRootPanel(null, rootPanel);

			// create new instance of the Demo Class
			DemoLesson demo = new MustacheTemplateDemo(sessionContext);

			// call the method defined in the DemoLesson Interface
			demo.handleDemoSelected();

			rootPanel.setContent(demo.getRootComponent());
		};
		new TeamAppsJettyEmbeddedServer(controller).start();
	}
}
