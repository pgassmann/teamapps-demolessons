package org.teamapps.demo.lessons.l09_propertyextractor;

import com.google.common.io.Files;
import org.teamapps.data.extract.BeanPropertyExtractor;
import org.teamapps.demo.lessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.field.Label;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.util.Arrays;
import java.util.List;

public class PropertyExtractorDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    // Constructor, only set session context instance variable
    public PropertyExtractorDemo(SessionContext context) {
        this.context = context;

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "ComboBox Demo");
        rootComponent = panel;
        VerticalLayout verticalLayout = new VerticalLayout();
        panel.setContent(verticalLayout);

        /* COMBOBOX with Objects*/
        List<Meal> myMeals = Arrays.asList(
                new Meal(MaterialIcon.CAKE, "Cake", "100 kcal"),
                new Meal(MaterialIcon.GESTURE, "Spaghetti", "203 kcal"),
                new Meal(MaterialIcon.CHILD_CARE, "Fries", "300 kcal"),
                new Meal(MaterialIcon.AC_UNIT, "Icecream", "430 kcal"),
                new Meal(MaterialIcon.BLUR_CIRCULAR, "Pizza", "540 kcal"),
                new Meal(MaterialIcon.LOCAL_CAFE, "Coffee", "623 kcal"),
                new Meal(MaterialIcon.LANDSCAPE, "Chocolate", "700 kcal")
        );

        ComboBox<Meal> mealComboBox = new ComboBox<>(myMeals);
        mealComboBox.setShowClearButton(true);
        mealComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);

        /* Manual Property Mapping */
        mealComboBox.setPropertyExtractor((meal, propertyName) -> {
            if (propertyName.equals(BaseTemplate.PROPERTY_CAPTION)) {
                return meal.getName();
            } else if (propertyName.equals(BaseTemplate.PROPERTY_DESCRIPTION)) {
                return meal.getCalories();
            } else if (propertyName.equals(BaseTemplate.PROPERTY_ICON)) {
                return meal.getIcon();
            } else {
                return null;
            }
        });

        // Preselect first Meal
        mealComboBox.setValue(myMeals.get(1));

        /* Define string representation of object for search and autocomplete */
        mealComboBox.setRecordToStringFunction(meal -> meal.getName()+" ("+ meal.getCalories()+")");

        mealComboBox.onValueChanged.addListener(s -> {
            context.showNotification(s.getIcon(), s.toString());
        });
        verticalLayout.addComponent(new Label("ComboBox with manual Property mapping"));
        verticalLayout.addComponent(mealComboBox);

        /* Bean Property Extractor */
        ComboBox<Meal> mealComboBox2 = new ComboBox<>(myMeals);
        mealComboBox2.setShowClearButton(true);
        mealComboBox2.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);

        /* Map Caption and Description to properties of Meal */
        /* Bean Property Extractor automatically maps Fields with the same Name.
        Meal already has an Icon Property.
        Other Properties can be mapped manually:  */
        BeanPropertyExtractor<Meal> mealPropertyExtractor = new BeanPropertyExtractor<>();
        mealPropertyExtractor.addProperty("caption", meal -> meal.getName());
        mealPropertyExtractor.addProperty( BaseTemplate.PROPERTY_DESCRIPTION, Meal::getCalories);
        mealComboBox2.setPropertyExtractor(mealPropertyExtractor);

        /* Define string representation of object for search and autocomplete */
        mealComboBox2.setRecordToStringFunction(meal -> meal.getName()+" ("+ meal.getCalories()+")");

        verticalLayout.addComponent(new Label("ComboBox with BeanPropertyExtractor"));
        verticalLayout.addComponent(mealComboBox2);


        ComboBox<Meal> mealComboBox3 = new ComboBox<>(myMeals);
        verticalLayout.addComponent(new Label("ComboBox with dynamic Content. Enter your favourite Meal"));
        verticalLayout.addComponent(mealComboBox3);

        /* The Objects of the Combobox are dynamically created */
        /* In real world application, this could be any query to Database/Backend etc. */
        mealComboBox3.setModel(queryString -> Arrays.asList(
                new Meal(MaterialIcon.CAKE, "Free "+ queryString, "100 kcal"),
                new Meal(MaterialIcon.LANDSCAPE, "Premium "+ queryString+ " with Chocolate", "500kcal")
        ));
        mealComboBox3.setRecordToStringFunction(meal -> meal.getName()+" ("+ meal.getCalories()+")");
        mealComboBox3.setTemplate(BaseTemplate.LIST_ITEM_LARGE_ICON_TWO_LINES);
        mealComboBox3.setPropertyExtractor(mealPropertyExtractor);

    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() { }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            PropertyExtractorDemo textFieldDemo = new PropertyExtractorDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}