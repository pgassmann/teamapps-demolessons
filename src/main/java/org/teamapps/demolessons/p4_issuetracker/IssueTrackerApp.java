package org.teamapps.demolessons.p4_issuetracker;

import com.google.common.io.Files;
import org.teamapps.databinding.TwoWayBindableValue;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.demolessons.issuetracker.model.SchemaInfo;
import org.teamapps.demolessons.issuetracker.model.issuetrackerdb.Issue;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.universaldb.UniversalDB;
import org.teamapps.ux.application.ResponsiveApplication;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.absolutelayout.Length;
import org.teamapps.ux.component.field.AbstractField;
import org.teamapps.ux.component.field.Fields;
import org.teamapps.ux.component.field.MultiLineTextField;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.table.Table;
import org.teamapps.ux.component.table.TableColumn;
import org.teamapps.ux.component.toolbar.ToolbarButton;
import org.teamapps.ux.component.toolbar.ToolbarButtonGroup;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class IssueTrackerApp implements DemoLesson {

    private Component rootComponent;
    private SessionContext context;

    private TextField typeField;
    private TextField priorityField;
    private TextField stateField;
    private TextField summaryField;
    private MultiLineTextField descriptionField;
    private TextField assignedToField;
    private TextField reporterField;
    private List<AbstractField<?>> formFields;

    private final TwoWayBindableValue<Issue> displayedIssue = TwoWayBindableValue.create();
    private Table<Issue> issueTable;
    private IssueTableModel issueTableModel;

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
        if (Issue.getCount() == 0) {
            Issue.create()
                    .setType("BUG")
                    .setSummary("VerticalLayout scroll")
                    .setDescription("VerticalLayout is not Scrollable")
                    .save();
            Issue.create()
                    .setType("TODO")
                    .setPriority("HIGH")
                    .setState("NEW")
                    .setSummary("issueTracker")
                    .setDescription("Finish implementation of issue tracker")
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("LOW")
                    .setSummary("WorkspaceLayout: Remove Panel")
                    .setDescription("This feature is missing")
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("LOW")
                    .setSummary("IssueTracker: Use Enums")
                    .setDescription("Type, Priority should use ENUMs")
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("MEDIUM")
                    .setSummary("IssueTracker: References")
                    .setDescription("Assignee, Reporter")
                    .save();
            Issue.create()
                    .setType("FEATURE")
                    .setPriority("MEDIUM")
                    .setState("DONE")
                    .setSummary("IssueTracker: MultiLine")
                    .setDescription("MultiLine Field for Description")
                    .save();
        }
    }

    // Design of the IssueTracker Application
    private Component createUI() {
        // create a responsive application that will run on desktops as well as on smart phones
        ResponsiveApplication application = ResponsiveApplication.createApplication();

        // create perspective with default layout
        Perspective perspective = Perspective.createPerspective();
        application.addPerspective(perspective);

        // Issue Table with a Table Model for Query and Filtering
        issueTableModel = new IssueTableModel();
        issueTable = createIssueTable(issueTableModel);
        View tableView = View.createView(StandardLayout.CENTER, MaterialIcon.REPORT_PROBLEM, "Issue List", issueTable);
        perspective.addView(tableView);

        // Add Global Search Field to Panel
        tableView.getPanel().setRightHeaderField(createFilterTextField(s -> issueTableModel.setFullTextFilter(s)));

        // Add Issue Detail Form View
        Component issueForm = createIssueDetailForm();
        View formView = View.createView(StandardLayout.RIGHT, MaterialIcon.BUG_REPORT, "Issue Details", issueForm);
        formView.addLocalButtonGroup(createFormToolbarButtonGroup());
        perspective.addView(formView);

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

        application.showPerspective(perspective);
        return application.getUi();
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
        table.addColumn(new TableColumn<>("assignedTo", "assigned to", new TextField()));
        table.addColumn(new TableColumn<>("reporter", "Reporter", new TextField()));

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
        assignedToField = new TextField();
        reporterField = new TextField();

        summaryField.setRequired(true);
        descriptionField.setRequired(true);

        formFields = Arrays.asList(
                typeField,
                priorityField,
                stateField,
                summaryField,
                descriptionField,
                assignedToField,
                reporterField);

        formLayout.addSection(MaterialIcon.INFO, "Issue");
        formLayout.addLabelAndField(null, "Type", typeField);
        formLayout.addLabelAndField(null, "Priority", priorityField);
        formLayout.addLabelAndField(null, "State", stateField);
        formLayout.addLabelAndField(null, "Summary", summaryField);
        formLayout.addLabelAndField(null, "Description", descriptionField);
        formLayout.addLabelAndField(null, "assigned to", assignedToField);
        formLayout.addLabelAndField(null, "Reporter", reporterField);
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
            issue.save();
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
            String assignees = "TODO";
            assignedToField.setValue(assignees);

            if (issue.getReporter() != null ) {
                reporterField.setValue(issue.getReporter().getName());
            } else {
                reporterField.setValue(null);
            }

        }
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
