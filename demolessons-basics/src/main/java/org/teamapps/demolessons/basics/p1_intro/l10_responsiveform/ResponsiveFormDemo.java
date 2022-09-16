package org.teamapps.demolessons.basics.p1_intro.l10_responsiveform;

import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.*;
import org.teamapps.ux.component.field.datetime.InstantDateTimeField;
import org.teamapps.ux.component.field.datetime.LocalDateField;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.time.LocalDate;

public class ResponsiveFormDemo implements DemoLesson {

    private final Component rootComponent;

    private Friend saved_friend;
    private final ResponsiveForm<Friend> form;

    public ResponsiveFormDemo(SessionContext sessionContext) {

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Responsive Form Demo");
        rootComponent = panel;

        // New Component: ResponsiveForm
        // reference in field, so it's accessible in other methods
        this.form = new ResponsiveForm<>(100, 200, 0);
        panel.setContent(this.form);

        ResponsiveFormLayout layout = this.form.addResponsiveFormLayout(400);
        layout.addSection(MaterialIcon.FOLDER,"Account Data");
        // the third (optional) Argument is a propertyName of the Friend Class
        layout.addLabelAndField(MaterialIcon.PERSON, "Name","firstName", new TextField());
        layout.addLabelAndField(MaterialIcon.PERSON_ADD, "Surname",  "lastName",new TextField());
        layout.addLabelAndField(MaterialIcon.HOME, "Street", "street", new TextField());
        layout.addLabelAndField(MaterialIcon.CHAT, "Active", "isActive", new CheckBox());
        layout.addLabelAndField(MaterialIcon.CHILD_FRIENDLY, "Birth Date", "birthDate", new LocalDateField());

        InstantDateTimeField createdAtField = new InstantDateTimeField();
        createdAtField.setEditingMode(FieldEditingMode.DISABLED);
        layout.addLabelAndField(MaterialIcon.ACCESS_TIME, "Created at", "createdAt", createdAtField);

        // Button to save the Values of the Form to the instance variable saved_friend
        @SuppressWarnings("rawtypes")
        Button<BaseTemplateRecord> saveButton = Button.create(MaterialIcon.SAVE, "Save");
        layout.addLabelAndComponent(saveButton);
        saveButton.onClicked.addListener(aBoolean -> {
            saved_friend = new Friend();
            this.form.applyFieldValuesToRecord(saved_friend);
            sessionContext.showNotification(MaterialIcon.SAVE, "Successfully saved your Friend " + saved_friend.getFirstName());
        });

        // Button to load the values from the instance variable saved_friend
        @SuppressWarnings("rawtypes")
        Button<BaseTemplateRecord> loadButton = Button.create(MaterialIcon.FILE_UPLOAD, "Load previously saved Friend");
        layout.addLabelAndComponent(loadButton);
        loadButton.onClicked.addListener(aBoolean -> {
            if (saved_friend != null) {
                this.form.applyRecordValuesToFields(saved_friend);
            } else {
                sessionContext.showNotification(MaterialIcon.ERROR, "No saved Friend found");
            }
        });

    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() {
        // Every time the Demo is displayed, we fill the form with a newly created Friend
        Friend john = new Friend("John", "Doe", "Sea Street", true, LocalDate.of(1977, 6, 25));
        form.applyRecordValuesToFields(john);

    }


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            DemoLesson demo = new ResponsiveFormDemo(sessionContext);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}
