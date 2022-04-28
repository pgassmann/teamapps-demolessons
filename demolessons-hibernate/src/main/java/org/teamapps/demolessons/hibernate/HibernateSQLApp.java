package org.teamapps.demolessons.hibernate;

import org.hibernate.Session;
import org.teamapps.data.value.SortDirection;
import org.teamapps.databinding.TwoWayBindableValue;
import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.demolessons.hibernate.models.Customer;
import org.teamapps.demolessons.hibernate.models.Invoice;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.*;
import org.teamapps.ux.component.field.datetime.LocalDateField;
import org.teamapps.ux.component.field.datetime.LocalDateTimeField;
import org.teamapps.ux.component.flexcontainer.HorizontalLayout;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.format.Spacing;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.table.Table;
import org.teamapps.ux.component.table.TableColumn;
import org.teamapps.ux.css.CssAlignItems;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class HibernateSQLApp implements DemoLesson {

	private final TwoWayBindableValue<Invoice> displayedInvoice = TwoWayBindableValue.create();
	private final Component rootComponent;
	private final SessionContext context;
	private final Session dbSession;
	private TextField detailIdentifier;
	private NumberField detailSum;
	private LocalDateTimeField detailCreated;
	private TextField detailForename;
	private TextField detailSurname;
	private TextField detailEmail;
	private List<AbstractField<?>> detailFields;
	private Table<Invoice> invoicesTable;
	private Component filterForm;
	private InvoiceTableModel invoiceTableModel;
	private ResponsiveApplication HibernateSqlApplication;
	private Perspective invoicePerspective;

	// Constructor, only set session context instance variable
	public HibernateSQLApp(SessionContext context) {
		this.context = context;
		HibernateSQLDb hibernateSQLDb = HibernateSQLDb.getInstance();
		hibernateSQLDb.ensureInitialized();
		dbSession = hibernateSQLDb.createSession();
		this.rootComponent = createUI();
	}


	public Component getRootComponent() {
		return rootComponent;
	}

	private Component createUI() {
		HibernateSqlApplication = ResponsiveApplication.createApplication();

		invoicePerspective = createInvoicesPerspective();
		HibernateSqlApplication.addPerspective(invoicePerspective);

		HibernateSqlApplication.showPerspective(invoicePerspective);
		return HibernateSqlApplication.getUi();
	}

	private Perspective createInvoicesPerspective() {
		Perspective invoicePerspective = Perspective.createPerspective();
		invoiceTableModel = new InvoiceTableModel(dbSession);

		filterForm = createFilterForm(invoiceTableModel);
		invoicesTable = createInvoicesTable(invoiceTableModel);

		VerticalLayout tableLayout = new VerticalLayout();
		tableLayout.addComponent(filterForm);
		tableLayout.addComponentFillRemaining(invoicesTable);
		View tableView = View.createView(StandardLayout.CENTER, MaterialIcon.ACCOUNT_BALANCE, "Invoices", tableLayout);
		invoicePerspective.addView(tableView);

		Component detailForm = createInvoiceDetailForm();
		View formView = View.createView(StandardLayout.RIGHT, MaterialIcon.DETAILS, "Details", detailForm);
		invoicePerspective.addView(formView);

		// displayedInvoice is a TwoWayBindableValue<Invoice>
		// we can add listeners to this Object that will fire every time we update the content.
		displayedInvoice.bindWritingTo(this::updateForm);

		// When a Invoice is selected in the table,
		// update displayedInvoice which will call updateForm(invoice)
		invoicesTable.onSingleRowSelected.addListener(invoice -> {
			displayedInvoice.set(invoice);
			// setting focus is helper for the mobile view where only one view is shown at a time.
			// show the form when an element is selected in the list.
			formView.focus();
		});
		return invoicePerspective;
	}

	private <T> ComponentField createSelectableFilter(AbstractField<T> filterField, Consumer<T> listener, boolean isActive) {
		filterField.onValueChanged.addListener(listener);

		CheckBox filterCB = new CheckBox();
		filterCB.setValue(isActive);
		filterCB.setMargin(new Spacing(0, 5, 0, 0));
		filterCB.onValueChanged.addListener(active -> listener.accept(active ? filterField.getValue() : null));

		HorizontalLayout filterLayout = new HorizontalLayout();
		filterLayout.addComponent(filterCB);
		filterLayout.addComponentFillRemaining(filterField);
		filterLayout.setAlignItems(CssAlignItems.CENTER);

		ComponentField filterComponentField = new ComponentField(filterLayout);
		filterComponentField.setBordered(false);

		listener.accept(isActive ? filterField.getValue() : null);
		return filterComponentField;
	}

	private ResponsiveForm<Void> createFilterForm(InvoiceTableModel tableModel) {
		ResponsiveForm<Void> form = new ResponsiveForm<>();
		ResponsiveFormLayout formLayout = form.addResponsiveFormLayout(450);

		LocalDateField dateCreatedFrom = new LocalDateField();
		dateCreatedFrom.setValue(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
		ComponentField dateCreatedFromComponent = createSelectableFilter(dateCreatedFrom, date -> tableModel.setFilterCreatedFrom(date == null ? null : date.atStartOfDay()), true);

		LocalDateField dateCreatedUntil = new LocalDateField();
		dateCreatedUntil.setValue(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
		ComponentField dateCreatedUntilComponent = createSelectableFilter(dateCreatedUntil, date -> tableModel.setFilterCreatedUntil(date == null ? null : date.atTime(LocalTime.MAX)), true);

		TextField identifier = new TextField();
		identifier.setEmptyText("99/1111");
		identifier.onTextInput.addListener(searchString -> tableModel.setTextFilter(Invoice.FIELD_IDENTIFIER, searchString));

		formLayout.addSection(MaterialIcon.SEARCH, "Filter").setPadding(new Spacing(5));
		formLayout.addLabelAndField(null, "From", dateCreatedFromComponent);
		formLayout.addLabelAndField(null, "Identifier", identifier, false);
		formLayout.addLabelAndField(null, "Until", dateCreatedUntilComponent);
		return form;
	}

	private Table<Invoice> createInvoicesTable(InvoiceTableModel tableModel) {
		Table<Invoice> table = new Table<>();
		table.setModel(tableModel);
		table.addColumn(new TableColumn<>(Invoice.FIELD_CREATED, "Created", new LocalDateTimeField()));
		table.addColumn(new TableColumn<>(Invoice.FIELD_IDENTIFIER, "Identifier", new TextField()));
		table.addColumn(new TableColumn<>(Invoice.FIELD_SUM, "Sum", new NumberField(2)));

		table.setForceFitWidth(true);
		table.setDisplayAsList(true);
		table.setSorting(Invoice.FIELD_CREATED, SortDirection.DESC);

		return table;
	}

	// Define form on the right with editable Fields
	private ResponsiveForm<Invoice> createInvoiceDetailForm() {
		ResponsiveForm<Invoice> form = new ResponsiveForm<>();
		form.setMargin(Spacing.px(5));
		ResponsiveFormLayout formLayout = form.addResponsiveFormLayout(400);

		detailIdentifier = new TextField();
		detailSum = new NumberField(2);
		detailCreated = new LocalDateTimeField();

		detailForename = new TextField();
		detailSurname = new TextField();
		detailEmail = new TextField();

		detailFields = Arrays.asList(
				detailIdentifier,
				detailSum,
				detailCreated,
				detailForename,
				detailSurname,
				detailEmail
		);

		detailFields.forEach(field -> field.setEditingMode(FieldEditingMode.READONLY));
		formLayout.addSection(MaterialIcon.ACCOUNT_BALANCE, "Invoice");
		formLayout.addLabelAndField(null, "Identifier", detailIdentifier);
		formLayout.addLabelAndField(null, "Crated", detailCreated, false);
		formLayout.addLabelAndField(null, "Sum", detailSum);

		formLayout.addSection(MaterialIcon.PERSON, "Customer");
		formLayout.addLabelAndField(null, "Forename", detailForename);
		formLayout.addLabelAndField(null, "Surname", detailSurname, false);
		formLayout.addLabelAndField(null, "E-Mail", detailEmail);
		return form;
	}

	private void updateForm(Invoice invoice) {
		detailFields.forEach(field -> field.setValue(null));
		if (invoice != null) {
			detailIdentifier.setValue(invoice.getIdentifier());
			detailSum.setValue(invoice.getSum());
			detailCreated.setValue(invoice.getCreated());

			Customer customer = invoice.getCustomer();
			if (customer != null) {
				detailForename.setValue(customer.getForename());
				detailSurname.setValue(customer.getSurname());
				detailEmail.setValue(customer.getEmail());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		WebController controller = sessionContext -> {
			RootPanel rootPanel = new RootPanel();
			sessionContext.addRootPanel(null, rootPanel);

			// create new instance of the Demo Class
			HibernateSQLApp app = new HibernateSQLApp(sessionContext);

			rootPanel.setContent(app.getRootComponent());

			// show Background Image
			String defaultBackground = "/resources/backgrounds/default-bl.jpg";
			sessionContext.registerBackgroundImage("default", defaultBackground, defaultBackground);
			sessionContext.setBackgroundImage("default", 0);
		};
		new TeamAppsJettyEmbeddedServer(controller, 8083).start();
	}
}
