package org.teamapps.demolessons.p4_issuetracker;

import com.google.common.io.Files;
import org.jetbrains.annotations.NotNull;
import org.teamapps.databinding.TwoWayBindableValue;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.demolessons.issuetracker.model.SchemaInfo;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.Issue;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.User;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.universaldb.UniversalDB;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.absolutelayout.Length;
import org.teamapps.ux.component.field.*;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.combobox.TagComboBox;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.itemview.SimpleItemGroup;
import org.teamapps.ux.component.itemview.SimpleItemView;
import org.teamapps.ux.component.table.Table;
import org.teamapps.ux.component.table.TableColumn;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroupPosition;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IssueTrackerApp implements DemoLesson {

    private Component rootComponent;
    private SessionContext context;

    private TextField typeField;
    private TextField priorityField;
    private TextField stateField;
    private TextField summaryField;
    private MultiLineTextField descriptionField;
    private TagComboBox<User> assignedToTagComboBox;
    private ComboBox<User> reporterComboBox;
    private List<AbstractField<?>> formFields;

    private final TwoWayBindableValue<Issue> displayedIssue = TwoWayBindableValue.create();
    private Table<Issue> issueTable;
    private IssueTableModel issueTableModel;
    private ResponsiveApplication issueTrackerApplication;
    private Perspective userPerspective;
    private Perspective issuePerspective;

    // Constructor, only set session context instance variable
    public IssueTrackerApp(SessionContext context){
        this.context = context;
        startDb();
        createDemoData();
        this.rootComponent = createUI();
    }

    // Method called from DemoLessons App
    @Override
    public void handleDemoSelected() {
    }

    public Component getRootComponent() {
        return rootComponent;
    }


    private static void startDb() {
        File storagePath = new File("./server-data/db-storage");
        if (! storagePath.exists()) {
            storagePath.mkdirs();
        }
        try {
            UniversalDB.createStandalone(storagePath, SchemaInfo.create());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDemoData() {
        if (User.getCount() == 0) {
            User user1 = User.create()
                    .setName("Major First")
                    .setEmail("majorfirst@example.com")
                    .save();
            User user2 = User.create()
                    .setName("John Second")
                    .setEmail("john@example.com")
                    .save();
            User user3 = User.create()
                    .setName("John Maier")
                    .setEmail("johnmaier@example.com")
                    .save();
            User user4 = User.create()
                    .setName("Tim Meier")
                    .setEmail("meiertim@example.com")
                    .save();
            User user5 = User.create()
                    .setName("Micky Meyer")
                    .setEmail("mickmeyer@example.com")
                    .save();
            User user6 = User.create()
                    .setName("Nick Johnson")
                    .setEmail("nickj@example.com")
                    .save();

            Issue.create()
                    .setType("BUG")
                    .setSummary("VerticalLayout scroll")
                    .setDescription("VerticalLayout is not Scrollable")
                    .setReporter(user1)
                    .setAssignedTo(user5, user2, user3)
                    .save();
            Issue.create()
                    .setType("TODO")
                    .setPriority("HIGH")
                    .setState("NEW")
                    .setSummary("issueTracker")
                    .setDescription("Finish implementation of issue tracker")
                    .setAssignedTo(user1)
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("LOW")
                    .setSummary("WorkspaceLayout: Remove Panel")
                    .setDescription("This feature is missing")
                    .setReporter(user2)
                    .setAssignedTo(user5, user6, user1, user4)
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("LOW")
                    .setSummary("IssueTracker: Use Enums")
                    .setDescription("Type, Priority should use ENUMs")
                    .setReporter(user3)
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("MEDIUM")
                    .setState("DONE")
                    .setSummary("IssueTracker: References")
                    .setDescription("Assignee, Reporter")
                    .setReporter(user3)
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("MEDIUM")
                    .setState("DONE")
                    .setSummary("IssueTracker: Reference Editor")
                    .setDescription("Select Assignee, Reporter with (Tag)ComboBox")
                    .setReporter(null)
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("MEDIUM")
                    .setState("DONE")
                    .setSummary("IssueTracker: MultiLine")
                    .setDescription("MultiLine Field for Description")
                    .setReporter(user5)
                    .save();
        }
    }

    // Design of the IssueTracker Application
    private Component createUI() {
        // create a responsive application that will run on desktops as well as on smart phones
        issueTrackerApplication = ResponsiveApplication.createApplication();

        // create perspective with default layout
        issuePerspective = createIssuePerspective();
        issueTrackerApplication.addPerspective(issuePerspective);
        issuePerspective.addWorkspaceButtonGroup(createPerspectiveSwitcher());

        userPerspective = new UserPerspective().getPerspective();
        issueTrackerApplication.addPerspective(userPerspective);
        userPerspective.addWorkspaceButtonGroup(createPerspectiveSwitcher());

        issueTrackerApplication.showPerspective(issuePerspective);
        issueTrackerApplication.showPerspective(issuePerspective);
        return issueTrackerApplication.getUi();
    }

    private ToolbarButtonGroup createPerspectiveSwitcher() {
        ToolbarButtonGroup group = new ToolbarButtonGroup();
        group.setPosition(ToolbarButtonGroupPosition.FIRST);
        ToolbarButton switchPerspective = ToolbarButton.createSmall(MaterialIcon.SWAP_VERTICAL_CIRCLE, "Switch Perspective", "");
        group.addButton(switchPerspective);
        // DropDown Menu for Perspectives
        SimpleItemView<Object> perspectiveMenu = new SimpleItemView<>();
        switchPerspective.setDropDownComponent(perspectiveMenu);
        SimpleItemGroup<Object> perspectiveMenuGroup = perspectiveMenu.addSingleColumnGroup(MaterialIcon.SWAP_VERT, "Perspectives");
        perspectiveMenuGroup.addItem(MaterialIcon.PEOPLE, "User Perspective", "manage Users")
                .onClick.addListener(s -> issueTrackerApplication.showPerspective(userPerspective));
        perspectiveMenuGroup.addItem(MaterialIcon.BUG_REPORT, "Issue Perspective", "manage Issues")
                .onClick.addListener(s -> issueTrackerApplication.showPerspective(issuePerspective));


//        switchPerspective.onClick.addListener(() -> {
//            if (issueTrackerApplication.getActivePerspective().equals(issuePerspective)) {
//                issueTrackerApplication.showPerspective(userPerspective);
//            } else {
//                issueTrackerApplication.showPerspective(issuePerspective);
//            }
//        });

        return group;
    }

    @NotNull
    private Perspective createIssuePerspective() {
        Perspective issuePerspective = Perspective.createPerspective();
        // Issue Table with a Table Model for Query and Filtering
        issueTableModel = new IssueTableModel();
        issueTable = createIssueTable(issueTableModel);
        View tableView = View.createView(StandardLayout.CENTER, MaterialIcon.REPORT_PROBLEM, "Issue List", issueTable);
        issuePerspective.addView(tableView);

        // Add Global Search Field to Panel
        tableView.getPanel().setRightHeaderField(createFilterTextField(s -> issueTableModel.setFullTextFilter(s)));

        // Add Issue Detail Form View
        Component issueForm = createIssueDetailForm();
        View formView = View.createView(StandardLayout.RIGHT, MaterialIcon.BUG_REPORT, "Issue Details", issueForm);
        formView.addLocalButtonGroup(createFormToolbarButtonGroup());
        issuePerspective.addView(formView);

        issuePerspective.addWorkspaceButtonGroup(createWorkspaceToolbar());

        // displayedIssue is a TwoWayBindableValue<Issue>
        // we can add listeners to this Object that will fire every time we update the content.
        displayedIssue.bindWritingTo(this::updateForm);

//        // bindWritingTo is the short form of
//        // update Form now
//        updateForm(displayedIssue.get());
//        // update form when displayedIssue changes
//        displayedIssue.onChanged().addListener(issue -> updateForm(issue));

        // When an issue is selected in the table,
        // update displayedIssue which will call updateForm(issue)
        issueTable.onRowSelected.addListener(issue -> {
            displayedIssue.set(issue);
            // setting focus is helper for the mobile view where only one view is shown at a time.
            // show the form when an element is selected in the list.
            formView.focus();
        });
        return issuePerspective;
    }


    // Define Table / List of Issues in the Center view
    private Table<Issue> createIssueTable(IssueTableModel tableModel) {
        Table<Issue> table = new Table<>();
        table.setModel(tableModel);
        table.addColumn(new TableColumn<Issue>("type", "Type", new TextField()).setMaxWidth(80));
        table.addColumn(new TableColumn<Issue>("priority", "Priority", new TextField()).setMaxWidth(80));
        table.addColumn(new TableColumn<Issue>("state", "State", new TextField()).setMaxWidth(80));
        table.addColumn(new TableColumn<>("summary", "Summary", new TextField()));
        table.addColumn(new TableColumn<>("description", "Description", new TextField()));

        // Reporter Column with custom Value Extractor to convert referenced User to a String.
        table.addColumn(new TableColumn<Issue>("reporter", "Reporter", new TextField())
                .setValueExtractor(issue -> {
                    if (issue.getReporter() != null) {
                        return issue.getReporter().getName();
                    } else {
                        return null;
                    }
                }));

        // Assigned To Column with custom value extractor that maps referenced users to a single, comma-separated string
        table.addColumn(new TableColumn<Issue>("assignedTo", "assigned to", new TextField())
                .setValueExtractor(issue -> {
                    return issue.getAssignedTo().stream()
                            .limit(3)
                            .map(user -> user.getName())
                            .collect(Collectors.joining(", "));

        }));

        table.setForceFitWidth(true);
        table.setDisplayAsList(true);

        // Add Filter Fields above every column.
        // When their value changes, in the tableModel, the filter for the corresponding field is updated
        table.setShowHeaderRow(true);
        table.setHeaderRowField("type", createFilterTextField(searchString -> tableModel.setTextFilter("type", searchString), "..."));
        table.setHeaderRowField("priority", createFilterTextField(searchString -> tableModel.setTextFilter("priority", searchString),"..."));
        table.setHeaderRowField("state", createFilterTextField(searchString -> tableModel.setTextFilter("state", searchString)));
        table.setHeaderRowField("summary", createFilterTextField(searchString -> tableModel.setTextFilter("summary", searchString)));
        table.setHeaderRowField("description", createFilterTextField(searchString -> tableModel.setTextFilter("description", searchString)));

        return table;
    }

    // Define form on the right with editable Fields
    private ResponsiveForm<Issue> createIssueDetailForm() {
        ResponsiveForm<Issue> form = new ResponsiveForm<>(100, 150, 0);
        ResponsiveFormLayout formLayout = form.addResponsiveFormLayout(450);

        typeField = new TextField();
        priorityField = new TextField();
        stateField = new TextField();
        summaryField = new TextField();
        descriptionField = new MultiLineTextField();
        descriptionField.setMinHeight(Length.ofPixels(200));
        reporterComboBox = new ComboBox<>(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
        reporterComboBox.setModel(s -> User.filter().parseFullTextFilter(s).execute());
        reporterComboBox.setShowClearButton(true);
        reporterComboBox.setPropertyExtractor((user, propertyName) -> {
            switch (propertyName) {
                case BaseTemplate.PROPERTY_ICON:
                    return MaterialIcon.PERSON;
                case BaseTemplate.PROPERTY_CAPTION:
                    return user.getName();
                case BaseTemplate.PROPERTY_DESCRIPTION:
                    return user.getEmail();
                default:
                    return null;
            }
        });
        reporterComboBox.setRecordToStringFunction(user -> user.getName());

        assignedToTagComboBox = new TagComboBox<User>();
        assignedToTagComboBox.setTemplate(BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);
        assignedToTagComboBox.setShowClearButton(true);
        assignedToTagComboBox.setModel(s -> User.filter().parseFullTextFilter(s).execute());
        assignedToTagComboBox.setPropertyExtractor((user, propertyName) -> {
            switch (propertyName) {
                case BaseTemplate.PROPERTY_ICON:
                    return MaterialIcon.PERSON;
                case BaseTemplate.PROPERTY_CAPTION:
                    return user.getName();
                case BaseTemplate.PROPERTY_DESCRIPTION:
                    return user.getEmail();
                default:
                    return null;
            }
        });
        assignedToTagComboBox.setRecordToStringFunction(user -> user.getName());

        summaryField.setRequired(true);
        descriptionField.setRequired(true);

        formFields = Arrays.asList(
                typeField,
                priorityField,
                stateField,
                summaryField,
                descriptionField,
                assignedToTagComboBox,
                reporterComboBox);

        formLayout.addSection(MaterialIcon.INFO, "Issue");
        formLayout.addLabelAndField(null, "Type", typeField);
        formLayout.addLabelAndField(null, "Priority", priorityField);
        formLayout.addLabelAndField(null, "State", stateField);
        formLayout.addLabelAndField(null, "Summary", summaryField);
        formLayout.addLabelAndField(null, "Description", descriptionField);
        formLayout.addLabelAndField(null, "assigned to", assignedToTagComboBox);
        formLayout.addLabelAndField(null, "Reporter", reporterComboBox);
        return form;
    }

    private ToolbarButtonGroup createFormToolbarButtonGroup() {
        ToolbarButtonGroup group = new ToolbarButtonGroup();
        // Save
        ToolbarButton saveButton = ToolbarButton.createSmall(MaterialIcon.SAVE, "Save", "Save Issue");
        group.addButton(saveButton);
        saveButton.onClick.addListener(toolbarButtonClickEvent -> {
            saveForm(displayedIssue.get());
        });

        // New
        ToolbarButton newButton = ToolbarButton.createSmall(MaterialIcon.ADD, "New", "Create Issue");
        group.addButton(newButton);
        newButton.onClick.addListener(toolbarButtonClickEvent -> {
            displayedIssue.set(Issue.create());
        });
        // Delete
        ToolbarButton deleteButton = ToolbarButton.createSmall(MaterialIcon.DELETE, "Delete", "Delete issue");
        group.addButton(deleteButton);
        deleteButton.onClick.addListener(toolbarButtonClickEvent -> {
            deleteIssue(displayedIssue.get());
        });
        // show save and delete button only when an issue is displayed
        displayedIssue.bindWritingTo(issue -> {
            deleteButton.setVisible(issue != null);
            saveButton.setVisible(issue != null);
        });
        return group;
    }

    private ToolbarButtonGroup createWorkspaceToolbar() {
        ToolbarButtonGroup group = new ToolbarButtonGroup();
        group.setPosition(ToolbarButtonGroupPosition.CENTER);

        // New
        ToolbarButton newButton = ToolbarButton.createSmall(MaterialIcon.ADD, "New", "Create Issue");
        group.addButton(newButton);
        newButton.onClick.addListener(toolbarButtonClickEvent -> {
            displayedIssue.set(Issue.create());
        });

        // Delete
        ToolbarButton deleteButton = ToolbarButton.createSmall(MaterialIcon.DELETE, "Delete", "Delete issue");
        group.addButton(deleteButton);
        deleteButton.onClick.addListener(toolbarButtonClickEvent -> {
            deleteIssue(displayedIssue.get());
        });

        // show delete button only when an issue is displayed
        displayedIssue.bindWritingTo(issue -> {
            deleteButton.setVisible(issue != null);
        });
        return group;
    }

    private void deleteIssue(Issue issue) {
        if (issue != null) {
            issue.delete();
            issueTable.refreshData();
            displayedIssue.set(null);
        }
    }

    // Save values from formFields to Issue in Database
    private void saveForm(Issue issue) {
        if (issue != null && Fields.validateAll(formFields)){
            issue.setType(typeField.getValue());
            issue.setPriority(priorityField.getValue());
            issue.setState(stateField.getValue());
            issue.setSummary(summaryField.getValue());
            issue.setDescription(descriptionField.getValue());
            issue.setAssignedTo(assignedToTagComboBox.getValue());
            issue.setReporter(reporterComboBox.getValue());
            issue.save();
            context.showNotification(MaterialIcon.INFO, "Issue successfully saved");
            issueTable.refreshData();
            issueTable.selectSingleRow(issue,true);
        }
    }

    // Update Form field values from issue
    private void updateForm(Issue issue) {
        if (issue != null) {
            typeField.setValue(issue.getType());
            priorityField.setValue(issue.getPriority());
            stateField.setValue(issue.getState());
            summaryField.setValue(issue.getSummary());
            descriptionField.setValue(issue.getDescription());
            assignedToTagComboBox.setValue(issue.getAssignedTo());
            reporterComboBox.setValue(issue.getReporter());

        } else {
            formFields.forEach(field -> field.setValue(null));
        }
        FieldEditingMode editingMode = issue != null ? FieldEditingMode.EDITABLE : FieldEditingMode.DISABLED;
        formFields.forEach(field -> field.setEditingMode(editingMode));
    }

    // FilterTextField which calls the Consumer onTextInput
    private TextField createFilterTextField(Consumer<String> searchTextHandler, String emptyText) {
        TextField searchField = new TextField();
        searchField.setEmptyText(emptyText);
        searchField.onTextInput.addListener(searchTextHandler);
        return searchField;
    }
    private TextField createFilterTextField(Consumer<String> searchTextHandler) {
        return createFilterTextField(searchTextHandler, "Search...");
    }

    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            IssueTrackerApp issueTrackerApp = new IssueTrackerApp(context);
            issueTrackerApp.handleDemoSelected();
            return issueTrackerApp.getRootComponent();
        });
        controller.setShowBackgroundImage(true);
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }

}
