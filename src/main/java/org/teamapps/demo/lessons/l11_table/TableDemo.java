package org.teamapps.demo.lessons.l11_table;

import com.google.common.io.Files;
import org.teamapps.data.extract.BeanPropertyExtractor;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.Button;
import org.teamapps.ux.component.field.CheckBox;
import org.teamapps.ux.component.field.FieldEditingMode;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.datetime.LocalDateField;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.table.ListTableModel;
import org.teamapps.ux.component.table.Table;
import org.teamapps.ux.component.table.TableColumn;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateRecord;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TableDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    public TableDemo(SessionContext context) {
        this.context = context;

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Table Demo");
        rootComponent = panel;

        Table<Friend> table = new Table<>();
        table.addColumn(new TableColumn("firstName", MaterialIcon.PERSON, "Name", new TextField())
                .setDefaultWidth(150));

        TextField lastNameTextField = new TextField();
        lastNameTextField.setEditingMode(FieldEditingMode.READONLY);
        table.addColumn(new TableColumn("lastName", MaterialIcon.PERSON, "Surname", lastNameTextField));

        TextField streetTextField = new TextField();
        streetTextField.setEditingMode(FieldEditingMode.DISABLED);
        table.addColumn(new TableColumn("street", MaterialIcon.PERSON, "Street", streetTextField));

        table.addColumn(new TableColumn("active", MaterialIcon.PERSON, "Active", new CheckBox()));
        table.addColumn(new TableColumn("birthDate", MaterialIcon.PERSON, "Birth Date", new LocalDateField())
                .setDefaultWidth(200));

        List<Meal> meals = Arrays.asList(
                new Meal(MaterialIcon.CAKE, "Cake", "100 kcal"),
                new Meal(MaterialIcon.GESTURE, "Spaghetti", "203 kcal"),
                new Meal(MaterialIcon.CHILD_CARE, "Fries", "300 kcal"),
                new Meal(MaterialIcon.AC_UNIT, "Icecream", "430 kcal"),
                new Meal(MaterialIcon.BLUR_CIRCULAR, "Pizza", "540 kcal"),
                new Meal(MaterialIcon.LOCAL_CAFE, "Coffee", "623 kcal"),
                new Meal(MaterialIcon.LANDSCAPE, "Chocolate", "700 kcal")
        );
        ComboBox<Meal> mealComboBox = new ComboBox<>(meals);
        mealComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
        mealComboBox.setAutoComplete(true);

        BeanPropertyExtractor<Meal> extractor = new BeanPropertyExtractor<>();
        extractor.addProperty("caption", meal -> meal.getName());
        extractor.addProperty("description", meal -> meal.getCalories());
        mealComboBox.setPropertyExtractor(extractor);
        mealComboBox.setRecordToStringFunction(Meal::getName);

        table.addColumn(new TableColumn("favouriteMeal", "Favourite Meal", mealComboBox));

        table.setForceFitWidth(true);

        ListTableModel<Friend> model = new ListTableModel<>(Arrays.asList(
                new Friend("Matt", "Berry", "Capway", true, LocalDate.of(1977, 6, 25), meals.get(0)),
                new Friend("Janis", "Massy", "Main Str.", true, LocalDate.of(1983, 6, 25), meals.get(1)),
                new Friend("Johnny", "Thirdy", "Burger Str.", true, LocalDate.of(1990, 1, 1), meals.get(3))
        ));
        for (int i = 1; i < 10_000; i++) {
            model.addRecord(new Friend("First " + i, "Last " + i, "Street " + i, i % 2 == 0, LocalDate.of(1990, 1, 1), meals.get(4)));
        }

        table.setModel(model);

        table.setEditable(true);

        Button<BaseTemplateRecord> saveButton = Button.create(MaterialIcon.SAVE, "Save Records");
        panel.setRightHeaderField(new TextField().setEmptyText("..."));

        saveButton.onValueChanged.addListener(aBoolean -> {
            List<Friend> changedRecords = table.getRecordsWithChangedCellValues();
            for (Friend friend : changedRecords) {
                Map<String, Object> changedCellValues = table.getChangedCellValues(friend);
                changedCellValues.forEach((propertyName, value) -> {
                    if (propertyName.equals("firstName")) {
                        friend.setFirstName((String) value);
                    } else if (propertyName.equals("lastName")) {
                        friend.setLastName((String) value);
                    } else if (propertyName.equals("street")) {
                        friend.setStreet((String) value);
                    } else if (propertyName.equals("isActive")) {
                        friend.setActive((Boolean) value);
                    } else if (propertyName.equals("birthDate")) {
                        friend.setBirthDate((LocalDate) value);
                    }
                });
                System.out.println(friend);
            }
            table.clearChangeBuffer();
            table.clearAllCellMarkings();
        });

        table.onCellValueChanged.addListener(data -> {
            table.setCellMarked(data.getRecord(), data.getPropertyName(), true);
        });

        panel.setContent(table);

    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() {   }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            TableDemo textFieldDemo = new TableDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}