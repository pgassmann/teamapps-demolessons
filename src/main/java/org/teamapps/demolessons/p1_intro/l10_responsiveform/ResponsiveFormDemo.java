package org.teamapps.demolessons.p1_intro.l10_responsiveform;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.field.CheckBox;
import org.teamapps.ux.component.field.FieldEditingMode;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.field.datetime.InstantDateTimeField;
import org.teamapps.ux.component.field.datetime.LocalDateField;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.time.LocalDate;

public class ResponsiveFormDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    private Friend saved_friend;
    private ResponsiveForm<Friend> form;

    public ResponsiveFormDemo(SessionContext context) {
        this.context = context;

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Responsive Form Demo");
        rootComponent = panel;

        // New Component: ResponsiveForm
        this.form = new ResponsiveForm<Friend>(100,200,0);
        panel.setContent(form);

        ResponsiveFormLayout layout = form.addResponsiveFormLayout(400);
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
        Button<BaseTemplateRecord> saveButton = Button.create(MaterialIcon.SAVE, "Save");
        layout.addLabelAndComponent(saveButton);
        saveButton.onClicked.addListener(aBoolean -> {
            saved_friend = new Friend();
            form.applyFieldValuesToRecord(saved_friend);
            context.showNotification(MaterialIcon.SAVE, "Successfully saved your Friend " + saved_friend.getFirstName());
        });

        // Button to load the values from the instance variable saved_friend
        Button<BaseTemplateRecord> loadButton = Button.create(MaterialIcon.FILE_UPLOAD, "Load previously saved Friend");
        layout.addLabelAndComponent(loadButton);
        loadButton.onClicked.addListener(aBoolean -> {
            if (saved_friend != null) {
                form.applyRecordValuesToFields(saved_friend);
            } else {
                context.showNotification(MaterialIcon.ERROR, "No saved Friend found");
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


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            ResponsiveFormDemo textFieldDemo = new ResponsiveFormDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}