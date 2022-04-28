package org.teamapps.demolessons.basics.p2_application.l05_iconviewer;

import org.teamapps.common.format.Color;
import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icon.antu.AntuIconBrowser;
import org.teamapps.icon.antu.AntuIconStyle;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.TemplateField;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.infiniteitemview.InfiniteItemView2;
import org.teamapps.ux.component.infiniteitemview.ListInfiniteItemViewModel;
import org.teamapps.ux.component.notification.Notification;
import org.teamapps.ux.component.notification.NotificationPosition;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.component.template.Template;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.util.List;
import java.util.stream.Collectors;

public class IconViewerDemo implements DemoLesson {

	private SessionContext sessionContext;
	private Template itemTemplate = BaseTemplate.ITEM_VIEW_ITEM;
	private AntuIconStyle iconStyle;
	private final ListInfiniteItemViewModel<AntuIcon> iconViewModel = new ListInfiniteItemViewModel<>(AntuIcon.getIcons());

	public IconViewerDemo(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	@Override
	public Component getRootComponent() {
		Panel panel = new Panel();
		Component iconFinder = createIconFinder();
		panel.setContent(iconFinder);
        panel.setTitle("Icon Viewer");
        panel.setIcon(AntuIcon.APP_GCSTAR_48);
		return panel;
	}

	protected Component createIconFinder() {
		Panel iconViewComponent = createIconView2Component();

		VerticalLayout verticalLayout = new VerticalLayout();
		// New Component: ResponsiveForm
		ResponsiveForm responsiveForm = new ResponsiveForm<>(100,200,0);
		verticalLayout.addComponent(responsiveForm);

		ResponsiveFormLayout layout = responsiveForm.addResponsiveFormLayout(400);

		// Icon Search
		layout.addSection(AntuIcon.ACTION_VIEW_FILTER_24,"Filter Icons");
		TextField searchField = new TextField();
		layout.addLabelAndField(AntuIcon.ACTION_SEARCH_24, "Icon Name", searchField);
		searchField.setEmptyText("Search...");
		searchField.onTextInput.addListener(s ->iconViewModel.setRecords(AntuIcon.getIcons().stream().filter(icon -> s == null || icon.getIconId().contains(s.toUpperCase())).collect(Collectors.toList())));
		verticalLayout.addComponent(searchField);

		// Style Selector
		ComboBox<AntuIconStyle> styleSelector = ComboBox.createForList(List.of(AntuIconStyle.LIGHT, AntuIconStyle.DARK));
		styleSelector.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE);
		styleSelector.setPropertyExtractor((style, propertyName) -> {
				switch (propertyName) {
					case BaseTemplate.PROPERTY_ICON:
						return AntuIcon.ACTION_DRAW_BRUSH_24;
					case BaseTemplate.PROPERTY_CAPTION:
						if (style.equals(AntuIconStyle.DARK)){
							return "DARK";
						} else {
							return "LIGHT";
						}
				}
				return null;
		});
		styleSelector.setRecordToStringFunction(style -> style.getFolder());
		styleSelector.setShowClearButton(false);
		styleSelector.setValue(AntuIconStyle.LIGHT);
		iconViewComponent.setBodyBackgroundColor(Color.WHITE.withAlpha(0.8f));
		styleSelector.onValueChanged.addListener(style -> {
			iconStyle = style;
			iconViewModel.onAllDataChanged.fire();
			if (style.equals(AntuIconStyle.DARK)) {
				iconViewComponent.setBodyBackgroundColor(Color.BLACK.withAlpha(0.6f));
//				iconViewComponent.getContent().setCssStyle("background-color", "#777");
//				iconViewComponent.setCssStyle("background-color", "#777");
			} else {
//				iconViewComponent.getContent().setCssStyle("background-color", "#777");
//				iconViewComponent.setCssStyle("background-color", "inherit");
				iconViewComponent.setBodyBackgroundColor(Color.WHITE.withAlpha(0.8f));
			}
		});
		layout.addLabelAndField(AntuIcon.ACTION_DRAW_BRUSH_24, "Icon Style", styleSelector);
		verticalLayout.addComponentFillRemaining(iconViewComponent);
		return verticalLayout;
	}

	public Panel createIconView2Component() {
		InfiniteItemView2<AntuIcon> iconView = new InfiniteItemView2<>();
		iconView.setItemTemplate(BaseTemplate.LIST_ITEM_LARGE_ICON_SINGLE_LINE);
		iconView.setItemHeight(50);
		iconView.setItemWidth(300);
		iconView.setItemPropertyExtractor((antuIcon, propertyName) -> switch (propertyName) {
			case BaseTemplate.PROPERTY_ICON -> antuIcon.withStyle(iconStyle);
			case BaseTemplate.PROPERTY_CAPTION -> antuIcon.getIconId();
			default -> null;
		});
		AntuIconStyle iconStyle = AntuIconStyle.LIGHT;
		iconView.setModel(iconViewModel);
		Panel panel = new Panel(null, "Icons");
		panel.setContent(iconView);
		panel.setBodyBackgroundColor(Color.WHITE.withAlpha(0.96f));
		TextField searchField = new TextField();
		searchField.onTextInput.addListener(s -> iconViewModel.setRecords(AntuIcon.getIcons().stream().filter(icon -> s == null || icon.getIconId().contains(s.toUpperCase())).collect(Collectors.toList())));
		panel.setRightHeaderField(searchField);
		iconView.onItemClicked.addListener(iconItemClickedEventData -> {
			String iconId = iconItemClickedEventData.getRecord().toString();

			// Custom Notification with VERY LARGE ICON
			TemplateField<BaseTemplateRecord<Void>> templateField = new TemplateField<>(BaseTemplate.LIST_ITEM_EXTRA_VERY_LARGE_ICON_TWO_LINES);
			templateField.setValue(new BaseTemplateRecord<>(iconItemClickedEventData.getRecord(), iconItemClickedEventData.getRecord().getIconId(), iconItemClickedEventData.getRecord().getIconPath()));
			Notification iconNotification = new Notification();
			iconNotification.setContent(templateField);
			iconNotification.setShowProgressBar(false);
			iconNotification.setDisplayTimeInMillis(5000);
			sessionContext.showNotification(iconNotification, NotificationPosition.TOP_RIGHT);
		});
		return panel;
	}

	// main method to launch the Demo standalone
	public static void main(String[] args) throws Exception {
		WebController controller = sessionContext -> {
			RootPanel rootPanel = new RootPanel();
			sessionContext.addRootPanel(null, rootPanel);

			// create new instance of the Demo Class
			DemoLesson demo = new IconViewerDemo(sessionContext);

			// call the method defined in the DemoLesson Interface
			demo.handleDemoSelected();

			// rootPanel.setContent(demo.getRootComponent());
			rootPanel.setContent(new AntuIconBrowser(sessionContext).getUI());
		};
		new TeamAppsJettyEmbeddedServer(controller).start();
	}

}
