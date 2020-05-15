package org.teamapps.demolessons.p2_application.l05_iconviewer;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.antu.AntuIconProvider;
import org.teamapps.icons.api.IconStyle;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.TemplateField;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.combobox.TagComboBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.infiniteitemview.InfiniteItemView;
import org.teamapps.ux.component.notification.Notification;
import org.teamapps.ux.component.notification.NotificationPosition;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.component.template.Template;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.util.Arrays;
import java.util.List;

public class IconViewerDemo implements DemoLesson {

	private SessionContext sessionContext;
	private Template itemTemplate = BaseTemplate.ITEM_VIEW_ITEM;
	private CategorizedIconsModel model;

	public IconViewerDemo(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	@Override
	public Component getRootComponent() {
		Panel panel = new Panel();
//		InfiniteItemView<StoreItem> itemView = new InfiniteItemView<>();
//        ListInfiniteItemViewModel<StoreItem> model = new ListInfiniteItemViewModel<>();
//        model.addRecord(new StoreItem("My first book", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/BLW_Manuscript_Book_of_Hours%2C_about_1480-90.jpg/640px-BLW_Manuscript_Book_of_Hours%2C_about_1480-90.jpg", 27.5, LocalDate.of(1990, 2, 23)));
//        model.addRecord(new StoreItem("Coconut", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Coconut_on_white_background.jpg/640px-Coconut_on_white_background.jpg", 2.95, LocalDate.now().minusDays(2)));
//        itemView.setModel(model);
//        itemView.setItemWidth(0);
//        itemView.setRowHeight(100);
//
//        itemView.setItemPropertyExtractor(new BeanPropertyExtractor<StoreItem>().addProperty("isNew", storeItem -> {
//			return LocalDate.now().minusDays(30).compareTo(storeItem.getReleaseDate()) < 0;
//		}));

        panel.setContent(createIconFinder(createIconViewComponent()));
        panel.setTitle("Icon Viewer");
        panel.setIcon(AntuIcon.APPS.GCSTAR_48);
		return panel;
	}

	private CategorizedIconsModel createModel() {
		return  new CategorizedIconsModel(Arrays.asList(AntuIconCategory.values()));
	}

	public InfiniteItemView<CategorizedIcon> createIconViewComponent() {
		InfiniteItemView<CategorizedIcon> itemView = new InfiniteItemView<>(this.itemTemplate, 100, 100);
		itemView.setItemPropertyExtractor((icon, prop) -> {
			if (prop.equals("icon")) {
				return icon.getIcon();
			} else if (prop.equals("caption")) {
				return icon.getIcon().toString();
			} else if (prop.equals("description")) {
				return icon.getCategory();
			} else {
				return null;
			}
		});
		model = createModel();
		itemView.setModel(model);
		itemView.setCssStyle("background-color", "#999999");

		// Print Icon ID in Notification

		itemView.onItemClicked.addListener(iconItemClickedEventData -> {
			String iconId = iconItemClickedEventData.getRecord().toString();

			// Custom Notification with VERY LARGE ICON
			TemplateField<BaseTemplateRecord<Void>> templateField = new TemplateField(BaseTemplate.LIST_ITEM_EXTRA_VERY_LARGE_ICON_TWO_LINES);
			templateField.setValue(new BaseTemplateRecord(iconItemClickedEventData.getRecord().getIcon(), iconId, ""));
			Notification iconNotification = new Notification();
			iconNotification.setContent(templateField);
			iconNotification.setShowProgressBar(false);
			iconNotification.setDisplayTimeInMillis(5000);
			sessionContext.showNotification(iconNotification, NotificationPosition.TOP_RIGHT);
		});
		return itemView;
	}

	protected Component createIconFinder(InfiniteItemView<CategorizedIcon> component) {
		VerticalLayout verticalLayout = new VerticalLayout();
		// New Component: ResponsiveForm
		ResponsiveForm responsiveForm = new ResponsiveForm<>(100,200,0);
		verticalLayout.addComponent(responsiveForm);

		ResponsiveFormLayout layout = responsiveForm.addResponsiveFormLayout(400);

		// Icon Search
		layout.addSection(AntuIcon.ACTION.VIEW_FILTER_24,"Filter Icons");
		TextField searchField = new TextField();
		layout.addLabelAndField(AntuIcon.ACTION.SEARCH_24, "Icon Name", searchField);
		searchField.setEmptyText("Search...");
		searchField.onTextInput.addListener(s -> model.setFilterString(s));
		verticalLayout.addComponent(searchField);

        // Category Filter
		TagComboBox<AntuIconCategory> categoryComboBox = new TagComboBox<>(Arrays.asList(AntuIconCategory.values()));
		categoryComboBox.onValueChanged.addListener(iconCategories -> {
			if (iconCategories.isEmpty()) {
				model.setIconCategories(Arrays.asList(AntuIconCategory.values()));
			} else {
				model.setIconCategories(iconCategories);
			}
		});
		categoryComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE);
		categoryComboBox.setShowClearButton(true);
		categoryComboBox.setPropertyExtractor((iconCategory, propertyName) -> {
			switch (propertyName) {
				case BaseTemplate.PROPERTY_ICON:
					return iconCategory.getCategoryIcon();
				case BaseTemplate.PROPERTY_CAPTION:
					return iconCategory.getCategoryName();
			}
			return null;
		});
		layout.addLabelAndField(AntuIcon.ACTION.VIEW_CATEGORIES_24, "Icon Category", categoryComboBox);

		// Style Selector
		List<IconStyle> iconStyleList = Arrays.asList(AntuIconProvider.DARK, AntuIconProvider.STANDARD);
		ComboBox<IconStyle> styleSelector = new ComboBox<>(iconStyleList);
		styleSelector.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE);
		styleSelector.setPropertyExtractor((style, propertyName) -> {
				switch (propertyName) {
					case BaseTemplate.PROPERTY_ICON:
						return AntuIcon.ACTION.DRAW_BRUSH_24;
					case BaseTemplate.PROPERTY_CAPTION:
						return style.getStyleId();
				}
				return null;
		});
		styleSelector.setRecordToStringFunction(style -> style.getStyleName());
		styleSelector.setShowClearButton(true);
		styleSelector.onValueChanged.addListener(style -> {

			model.setIconStyle(style);
		});
		layout.addLabelAndField(AntuIcon.ACTION.DRAW_BRUSH_24, "Icon Style", styleSelector);
		verticalLayout.addComponentFillRemaining(component);
		return verticalLayout;
	}

	public static void main(String[] args) throws Exception {
		SimpleWebController controller = new SimpleWebController(sessionContext -> {
			IconViewerDemo iconViewerDemo = new IconViewerDemo(sessionContext);
			iconViewerDemo.handleDemoSelected();
			return iconViewerDemo.getRootComponent();
		});
		controller.addAdditionalIconProvider(new AntuIconProvider());

		new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
	}

}
