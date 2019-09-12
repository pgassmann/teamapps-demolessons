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
import org.teamapps.ux.component.field.Label;
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

        VerticalLayout verticalLayout = new VerticalLayout();
        panel.setContent(verticalLayout);

        /* ComboBox of Strings*/
        verticalLayout.addComponent(new Label("ComboBox of Strings"));
        ComboBox<String> stringComboBox = new ComboBox<>(Arrays.asList("Cake", "Spaghetti", "Fries", "Icecream", "Pizza", "Chocolate"));
        stringComboBox.onValueChanged.addListener(s -> context.showNotification(MaterialIcon.ARROW_DROP_DOWN, s));
        stringComboBox.setShowClearButton(true);
        verticalLayout.addComponent(stringComboBox);

        /* ComboBox of Objects*/
        verticalLayout.addComponent(new Label("ComboBox of Objects"));
        ComboBox<Meal> mealComboBox = new ComboBox<>(Arrays.asList(
                new Meal(MaterialIcon.CAKE, "Cake", "100 kcal"),
                new Meal(MaterialIcon.GESTURE, "Spaghetti", "100 kcal"),
                new Meal(MaterialIcon.CHILD_CARE, "Fries", "100 kcal"),
                new Meal(MaterialIcon.AC_UNIT, "Icecream", "100 kcal"),
                new Meal(MaterialIcon.BLUR_CIRCULAR, "Pizza", "100 kcal"),
                new Meal(MaterialIcon.LOCAL_CAFE, "Coffee", "100 kcal"),
                new Meal(MaterialIcon.LANDSCAPE, "Chocolate", "100 kcal")
        ));
        verticalLayout.addComponent(mealComboBox);

        /* Display objects using a Template */
        mealComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
        /* BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES uses icon, caption and description */

        /* Map Caption and Description to properties of Meal */
        /* Bean Property Extractor automatically maps Fields with the same Name.
        Meal already has an Icon Property.
        Other Properties can be mapped manually:  */
        BeanPropertyExtractor<Meal> extractor = new BeanPropertyExtractor<>();
        extractor.addProperty("caption", meal -> meal.getName());
        extractor.addProperty("description", meal -> meal.getCalories());
        mealComboBox.setPropertyExtractor(extractor);

        /* Define string representation of object for search and autocomplete */
        mealComboBox.setRecordToStringFunction(meal -> meal.getName());
        mealComboBox.setAutoComplete(true);
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