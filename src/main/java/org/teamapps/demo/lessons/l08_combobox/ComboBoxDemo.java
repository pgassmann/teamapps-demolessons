package org.teamapps.demo.lessons.l08_combobox;

import com.google.common.io.Files;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.teamapps.common.format.Color;
import org.teamapps.data.extract.BeanPropertyExtractor;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.CheckBox;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.util.Arrays;

public class ComboBoxDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    // Constructor, only set session context instance variable
    public ComboBoxDemo(SessionContext context) {
        this.context = context;

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "ComboBox Demo");
        rootComponent = panel;

        /* ComboBox of Strings*/
        ComboBox<String> stringComboBox = new ComboBox<>(Arrays.asList("Kuchen", "Spaghetti", "Fries", "Icecream", "Pizza", "Chocolate"));
        stringComboBox.onValueChanged.addListener(s -> context.showNotification(MaterialIcon.ARROW_DROP_DOWN, s));
        stringComboBox.setShowClearButton(true);


        /* ComboBox of Objects*/
        ComboBox<Meal> mealComboBox = new ComboBox<>(Arrays.asList(
                new Meal(MaterialIcon.CAKE, "Kuchen", "100 kcal"),
                new Meal(MaterialIcon.GESTURE, "Spaghetti", "100 kcal"),
                new Meal(MaterialIcon.CHILD_CARE, "Fries", "100 kcal"),
                new Meal(MaterialIcon.AC_UNIT, "Icecream", "100 kcal"),
                new Meal(MaterialIcon.BLUR_CIRCULAR, "Pizza", "100 kcal"),
                new Meal(MaterialIcon.LOCAL_CAFE, "Coffee", "100 kcal"),
                new Meal(MaterialIcon.LANDSCAPE, "Chocolate", "100 kcal")
        ));
        mealComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
        mealComboBox.setAutoComplete(true);

        BeanPropertyExtractor<Meal> extractor = new BeanPropertyExtractor<>();
        extractor.addProperty("caption", meal -> meal.getName());
        extractor.addProperty("description", meal -> meal.getCalories());
        mealComboBox.setPropertyExtractor(extractor);

        mealComboBox.setRecordToStringFunction(meal -> meal.getName());

//            mealComboBox.setPropertyExtractor((meal, propertyName) -> {
//                if (propertyName.equals(BaseTemplate.PROPERTY_CAPTION)) {
//                    return meal.getName();
//                } else if (propertyName.equals(BaseTemplate.PROPERTY_DESCRIPTION)) {
//                    return meal.getCalories();
//                } else {
//                    return null;
//                }
//            });


        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(stringComboBox);
        verticalLayout.addComponent(mealComboBox);
        panel.setContent(verticalLayout);

    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() { }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            ComboBoxDemo textFieldDemo = new ComboBoxDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}