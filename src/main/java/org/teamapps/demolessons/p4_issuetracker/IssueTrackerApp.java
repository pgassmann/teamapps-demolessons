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
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.AbstractField;
import org.teamapps.ux.component.field.Fields;
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

public class IssueTrackerApp implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    private TextField typeField;
    private TextField priorityField;
    private TextField stateField;
    private TextField summaryField;
    private TextField descriptionField;
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

    private void createDemoData() {
        if (Issue.getCount() == 0) {
            Issue.create()
                    .setSummary("VerticalLayout is not Scrollable")
                    .save();
            Issue.create()
                    .setSummary("Finish implementation of issue tracker")
                    .save();
            Issue.create()
                    .setSummary("WorkspaceLayout: Remove Panel")
                    .save();
        }
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

    @Override
    public void handleDemoSelected() {
    }

    public Component getRootComponent() {
        return rootComponent;
    }

    private Component createUI() {
        //create a responsive application that will run on desktops as well as on smart phones
        ResponsiveApplication application = ResponsiveApplication.createApplication();

        //create perspective with default layout
        Perspective perspective = Perspective.createPerspective();
        application.addPerspective(perspective);
        issueTableModel = new IssueTableModel();
        issueTable = createIssueTable(issueTableModel);
        View tableView = View.createView(StandardLayout.CENTER, MaterialIcon.REPORT_PROBLEM, "Issue List", issueTable);
        perspective.addView(tableView);
        tableView.getPanel().setRightHeaderField(createSearchTextField());

        Component issueForm = createIssueDetailForm();
        View formView = View.createView(StandardLayout.RIGHT, MaterialIcon.REPORT_PROBLEM, "Issue Details", issueForm);
        formView.addLocalButtonGroup(createFormToolbarButtonGroup());
        perspective.addView(formView);

        // perspective.addWorkspaceButtonGroup(issueMasterDetail.getWorkspaceButtonGroup());
        issueTable.onRowSelected.addListener(issue -> {
            displayedIssue.set(issue);
            formView.focus();
        });

        displayedIssue.bindWritingTo(this::updateForm);

        application.showPerspective(perspective);
        return application.getUi();
    }

    private TextField createSearchTextField() {
        TextField searchField = new TextField();
        searchField.setEmptyText("Search...");
        searchField.onTextInput.addListener(s -> issueTableModel.setFullTextFilter(s));
        return searchField;
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
        displayedIssue.bindWritingTo(person -> {
            deleteButton.setVisible(person != null);
            saveButton.setVisible(person != null);
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

    private Component createIssueDetailForm() {
        ResponsiveForm<Issue> form = new ResponsiveForm<>(100, 150, 0);
        ResponsiveFormLayout formLayout = form.addResponsiveFormLayout(450);

        typeField = new TextField();
        priorityField = new TextField();
        stateField = new TextField();
        summaryField = new TextField();
        descriptionField = new TextField();
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

        formLayout.addSection(MaterialIcon.PERSON, "Person");
        formLayout.addLabelAndField(null, "Type", typeField);
        formLayout.addLabelAndField(null, "Priority", priorityField);
        formLayout.addLabelAndField(null, "State", stateField);
        formLayout.addLabelAndField(null, "Summary", summaryField);
        formLayout.addLabelAndField(null, "Description", descriptionField);
        formLayout.addLabelAndField(null, "assigned to", assignedToField);
        formLayout.addLabelAndField(null, "Reporter", reporterField);
        return form;
    }

    private Table<Issue> createIssueTable(IssueTableModel tableModel) {
        Table<Issue> table = new Table<>();
        table.setModel(tableModel);
        table.addColumn(new TableColumn<>("type", "Type", new TextField()));
        table.addColumn(new TableColumn<>("priority", "Priority", new TextField()));
        table.addColumn(new TableColumn<>("state", "State", new TextField()));
        table.addColumn(new TableColumn<>("summary", "Summary", new TextField()));
        table.addColumn(new TableColumn<>("description", "Description", new TextField()));
        table.addColumn(new TableColumn<>("assignedTo", "assigned to", new TextField()));
        table.addColumn(new TableColumn<>("reporter", "Reporter", new TextField()));

        table.setForceFitWidth(true);
        table.setDisplayAsList(true);
        return table;
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
